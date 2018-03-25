package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.robot.Key
import be.samclercky.gamepadrobot.robot.Robot
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.ControllerEvent
import net.java.games.input.Event
import javax.swing.JFrame

class App: JFrame() {

    val minecraftGamepad = MinecraftGamepad()
    val robot = Robot()

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
            }
        }
    }

    fun mapToMovement(gameEvent: GameEvent) {
        val data = gameEvent
        println(data)
        var finalBtn = data.btn

        // check if 0 and break
        if (data.value == 0f) return

        val unMappedBtn = minecraftGamepad.getUnMappedBtn()
        for (btn in unMappedBtn) {
            if (btn.btn == data.btn) {
                if (data.analog) {
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
            }
        }

        // get maining
        val key = minecraftGamepad.getCode(finalBtn)
        println("$key: code: ${robot.KeyToKeyEvent(key)}")

        // pack everything in GameData object

        robot.keyPress(robot.KeyToKeyEvent(key))
    }

    fun excecuter(gameData: GameData) {
        println("${gameData.dx}; ${gameData.dy}")
    }
}

data class GameEvent(val analog: Boolean, val value: Float, val btn: String)
data class GameData(val dx: Int, val dy: Int, val key: Key)