package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.input.Controller
import be.samclercky.gamepadrobot.json.JSONGamepad
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

/**
 * Stores the current version of the program
 */
val VERSION = "0.0.1"

/**
 * Entry point of the program
 */
fun main(args: Array<String>) = runBlocking<Unit> {
    forloop@ for (arg in args) {
        println(arg)
        when(arg) {
            "--test" -> {
                test()
                break@forloop
            }
            "--list-all" -> {
                listAll()
                break@forloop
            }
            "--help" -> {
                listOptions()
                break@forloop
            }
            "" -> break@forloop
            else -> {
                App(JSONGamepad(File(arg)))
                break@forloop
            }
        }
    }
    // if nothing is there -> Show error
    println("Invalid option")
    listOptions()
}