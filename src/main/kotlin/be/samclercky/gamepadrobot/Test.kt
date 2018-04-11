package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

val cmdOptions = arrayOf(
        Option("--list-all", "Lists all recognized joysticks"),
        Option("--test", "Tests the first joystick by reading every second the data"),
        Option("--help", "Shows this menu")
)

suspend fun test(){
    val c = Controller()

    while (true) {
        for (i in c.pollData()) {
            print("$i, ")
        }
        println()
        println("-----------------")
        delay(1000)
    }
}

fun listAll() {
    val c = Controller()
    val joys = c.allControllerNames

    for (i in 0..(joys.size-1)) {
        println("$i: ${joys[i]}")
    }
    println()
}

fun listOptions() {
    println("Options are:")
    for (cmd in cmdOptions) {
        println(">> ${cmd.name}: ${cmd.description}")
    }
    println()
}

data class Option(val name: String, val description: String)