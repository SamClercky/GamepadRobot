package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking<Unit> {
    App()

    val c = Controller()

    Runtime.getRuntime().addShutdownHook(Thread {
        c.destroy()
    })

    while (true) {
        for (i in c.pollData()) {
            print("$i, ")
        }
        println("-----------------")
        delay(1000)
    }
}