package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import be.samclercky.gamepadrobot.json.JSONGamepad
import be.samclercky.gamepadrobot.json.UnMappedBtn
import be.samclercky.gamepadrobot.robot.Key
import be.samclercky.gamepadrobot.robot.Robot
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.swing.JFrame
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Controls all the imput from the gamepad and sends commands to the keyboard and mouse
 */
class App(val gamepadConfig: JSONGamepad): JFrame() {

    private val robot = Robot()
    private val passedUnMappedBtn = Array<PassedUnMappedBtn>(gamepadConfig.unMappedBtn.size, {
        PassedUnMappedBtn(gamepadConfig.unMappedBtn[it], false, "")
    })

    private val controller = Controller()

    init {
        createUI()
        val events = produceEvents()
        val keys = mapToMovement(events)
        excecuter(keys, Robot.workContext)

        launch {
            while (isActive) {
                withContext(Robot.workContext) {
                    robot.updateMouse()
                }
                delay(5)
            }
        }

        while (!events.isClosedForReceive || !keys.isClosedForReceive) {
            // keep jvm alive
        }
    }

    private fun createUI() {

    }

    /**
     * Takes the events and starts the chain
     */
    private fun produceEvents() = produce<GameEvent> {
        while (true) {
            if (controller.firstControllerId == -1) {
                println("Geen controllers gevonden")
                println("Excecute in bash to see all found controllers: gamerobot --list-all")
                System.exit(0)
            }

            val newData = controller.pollNewData()

            for (data in newData) {
                val gameEvent = GameEvent(data.analog, data.value, data.key.toString())
                //mapToMovement(gameEvent)
                send(gameEvent)
            }
        }
    }

    /**
     * Maps all the events to a Key that is used to send a command to keyboard and mouse
     * @param gameEvent the event to map
     */
    private fun mapToMovement(gameEvents: ReceiveChannel<GameEvent>) = produce<GameData> {
        mainloop@ for (data in gameEvents) {
            println(data)
            var finalBtn = data.btn

            for (passedBtn in passedUnMappedBtn) { // set other mapped buttons to 0
                if (passedBtn.unMappedBtn.btn == data.btn && passedBtn.justPasted) {
                    for (value in passedBtn.unMappedBtn.values) {
                        val id = passedBtn.unMappedBtn.values.indexOf(value)+1
                        var resetBtn = passedBtn.unMappedBtn.btn
                        if (id > 1) {
                            resetBtn += " $id"
                        }

                        send(GameData(0, gamepadConfig.getCode(resetBtn)))
                    }
                    passedBtn.justPasted = false
                }
            }

            val unMappedBtn = gamepadConfig.unMappedBtn
            for (btn in unMappedBtn) {
                if (btn.btn == data.btn && finalBtn == data.btn) {
                    if (data.analog) {
                        // turn mapped analog data to digital data
                        if (data.value != 1 && data.value != -1 && data.value != 0) break@mainloop
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
            val key = gamepadConfig.getCode(finalBtn)
            println("$key: code: ${key.keyCode}")
            println(passedUnMappedBtn)

            // pack everything in GameData object
            val gameData = GameData(data.value, key, multiplyer = gamepadConfig.mouseSensitive)
            send(gameData)
        }
    }

    /**
     * Excecutes the data
     * @param gameData Data to be excecuted
     * @param ctx Context where the coroutine should run on
     */
    private fun excecuter(gameDatas: ReceiveChannel<GameData>, ctx: CoroutineContext = CommonPool) = launch(ctx) {
        for (gameData in gameDatas) {
            println("${gameData.value}; ${gameData.key}")
            if (robot.isClick(gameData.key)) {
                if (gameData.value == 0) {
                    robot.clickRelease(gameData.key)
                } else {
                    robot.clickPress(gameData.key)
                }
            } else if (robot.isMouseMovement(gameData.key)) {
                robot.pushMouseMovement(gameData)
            } else {
                if (gameData.value == 0) {
                    robot.keyRelease(gameData.key.keyCode)
                } else {
                    robot.keyPress(gameData.key.keyCode)
                }
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
data class GameEvent(val analog: Boolean, val value: Int, val btn: String)

/**
 * Transports the mapped event data
 * @param value The value of the command
 * @param key The mapped name of the event
 * @param originalKey The unmapped name of the event
 * @param multiplyer Gives the speed of the control -> used for the speed of the mouse
 */
data class GameData(val value: Int, val key: Key, val originalKey: Key = key, val multiplyer: Int = 1)

/**
 * Stores the unmapped data from the controller-class
 * @param unMappedBtn The data of the unmapped event
 * @param justPasted Wether if the event has just passed
 * @param prevBtn The name of the last mapped event
 */
data class PassedUnMappedBtn(val unMappedBtn: UnMappedBtn, var justPasted: Boolean, var prevBtn: String)