package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

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