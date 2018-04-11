package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * List of all available options
 */
val cmdOptions = arrayOf(
        Option("--list-all", "Lists all recognized joysticks"),
        Option("--test", "Tests the first joystick by reading every second the data"),
        Option("--help", "Shows this menu")
)

/**
 * Polls all data and shows it in the command line
 */
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

/**
 * List all recognized controllers
 */
fun listAll() {
    val c = Controller()
    val joys = c.allControllerNames

    for (i in 0..(joys.size-1)) {
        println("$i: ${joys[i]}")
    }
    println()
}

/**
 * List all availlable command options based on cmdOptions
 */
fun listOptions() {
    println("Options are:")
    for (cmd in cmdOptions) {
        println(">> ${cmd.name}: ${cmd.description}")
    }
    println()
}

/**
 * Quick data-class to store the availlable command options
 */
data class Option(val name: String, val description: String)