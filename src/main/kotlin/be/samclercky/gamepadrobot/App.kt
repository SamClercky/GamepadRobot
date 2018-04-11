package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import be.samclercky.gamepadrobot.robot.Key
import be.samclercky.gamepadrobot.robot.Robot
import javax.swing.JFrame

/**
 * Controls all the imput from the gamepad and sends commands to the keyboard and mouse
 */
class App: JFrame() {

    private val minecraftGamepad = MinecraftGamepad()
    private val robot = Robot()
    private val passedUnMappedBtn = Array<PassedUnMappedBtn>(minecraftGamepad.getUnMappedBtn().size, {
        PassedUnMappedBtn(minecraftGamepad.getUnMappedBtn()[it], false, "")
    })

    private val controller = Controller()

    init {
        createUI()
        produceEvents()
    }

    private fun createUI() {

    }

    /**
     * Takes the events and starts the chain
     */
    private fun produceEvents() {
        while (true) {
            /*val controllers = ControllerEnvironment.getDefaultEnvironment().controllers

            if (controllers.size == 0) {
                println("Geen controllers gevonden")
                System.exit(0)
            }
            //println("Controller gevonden")
            for (controller in controllers) {
                controller.poll()

                val queue = controller.eventQueue
                val event = Event()

                while (queue.getNextEvent(event)) {
                    val comp = event.component
                    val gameEvent = GameEvent(comp.isAnalog(), event.value, comp.name)
                    mapToMovement(gameEvent)
                }
                robot.updateMouse()
            }*/
            if (controller.firstControllerId == -1) {
                println("Geen controllers gevonden")
                println("Excecute in bash to see all found controllers: gamerobot --list-all")
                System.exit(0)
            }

            val newData = controller.pollNewData()

            for (data in newData) {
                val gameEvent = GameEvent(false, data.value.toFloat(), data.key.toString())
                mapToMovement(gameEvent)
            }
        }
    }

    /**
     * Maps all the events to a Key that is used to send a command to keyboard and mouse
     * @param gameEvent the event to map
     */
    private fun mapToMovement(gameEvent: GameEvent) {
        val data = gameEvent
        println(data)
        var finalBtn = data.btn

        for (passedBtn in passedUnMappedBtn) {
            if (passedBtn.unMappedBtn.btn == data.btn && passedBtn.justPasted) {
                for (value in passedBtn.unMappedBtn.values) {
                    val id = passedBtn.unMappedBtn.values.indexOf(value)+1
                    var resetBtn = passedBtn.unMappedBtn.btn
                    if (id > 1) {
                        resetBtn += " $id"
                    }
                    excecuter(GameData(0f, minecraftGamepad.getCode(resetBtn)))
                }
                passedBtn.justPasted = false
            }
        }

        val unMappedBtn = minecraftGamepad.getUnMappedBtn()
        for (btn in unMappedBtn) {
            if (btn.btn == data.btn && finalBtn == data.btn) {
                if (data.analog) {
                    // turn mapped analog data to digital data
                    if (data.value != 1f && data.value != -1f && data.value != 0f) return
                    if (data.value > 0) {
                        finalBtn += " 2"
                    }
                } else {
                    for (value in btn.values) {
                        if (value == data.value) {
                            val id = btn.values.indexOf(value)+1
                            if (id > 1) {
                                finalBtn += " $id"
                            }
                        }
                    }
                }

                // store the change
                for (passedBtn in passedUnMappedBtn) {
                    if (passedBtn.unMappedBtn.btn == data.btn) {
                        passedBtn.justPasted = true
                        passedBtn.prevBtn = finalBtn
                    }
                }
            }
        }
        // get maining
        val key = minecraftGamepad.getCode(finalBtn)
        println("$key: code: ${robot.KeyToKeyEvent(key)}")
        println(passedUnMappedBtn)

        // pack everything in GameData object
        val gameData = GameData(data.value, key, multiplyer = minecraftGamepad.mouseSensitvity)
        excecuter(gameData)
    }

    /**
     * Excecutes the data
     * @param gameData Data to be excecuted
     */
    private fun excecuter(gameData: GameData) {
        println("${gameData.value}; ${gameData.key}")
        if (robot.isClick(gameData.key)) {
            robot.click(gameData.key)
        } else if (robot.isMouseMovement(gameData.key)) {
            robot.pushMouseMovement(gameData)
        } else {
            if (gameData.value == 0f) {
                robot.keyRelease(robot.KeyToKeyEvent(gameData.key))
            } else {
                robot.keyPress(robot.KeyToKeyEvent(gameData.key))
            }
        }
    }
}

/**
 * Transports the event data
 * @param analog Is the clicked button an analog button
 * @param value The value of the event
 * @param btn The name of the event
 */
data class GameEvent(val analog: Boolean, val value: Float, val btn: String)

/**
 * Transports the mapped event data
 * @param value The value of the command
 * @param key The mapped name of the event
 * @param originalKey The unmapped name of the event
 * @param multiplyer Gives the speed of the control -> used for the speed of the mouse
 */
data class GameData(val value: Float, val key: Key, val originalKey: Key = key, val multiplyer: Float = 1f)

/**
 * Stores the unmapped data from the controller-class
 * @param unMappedBtn The data of the unmapped event
 * @param justPasted Wether if the event has just passed
 * @param prevBtn The name of the last mapped event
 */
data class PassedUnMappedBtn(val unMappedBtn: UnMappedBtn, var justPasted: Boolean, var prevBtn: String)