package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.robot.Key
import be.samclercky.gamepadrobot.robot.Robot
import javax.swing.JFrame

class App: JFrame() {

    /*val minecraftGamepad = MinecraftGamepad()
    val robot = Robot()
    val passedUnMappedBtn = Array<PassedUnMappedBtn>(minecraftGamepad.getUnMappedBtn().size, {
        PassedUnMappedBtn(minecraftGamepad.getUnMappedBtn()[it], false, "")
    })

    init {
        createUI()
        produceEvents()
    }

    fun createUI() {

    }

    fun produceEvents() {
        while (true) {
            val controllers = ControllerEnvironment.getDefaultEnvironment().controllers

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
            }
        }
    }

    fun mapToMovement(gameEvent: GameEvent) {
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

    fun excecuter(gameData: GameData) {
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
    }*/
}

data class GameEvent(val analog: Boolean, val value: Float, val btn: String)
data class GameData(val value: Float, val key: Key, val originalKey: Key = key, val multiplyer: Float = 1f)
data class PassedUnMappedBtn(val unMappedBtn: UnMappedBtn, var justPasted: Boolean, var prevBtn: String)