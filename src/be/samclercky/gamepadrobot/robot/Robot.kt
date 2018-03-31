package be.samclercky.gamepadrobot.robot

import be.samclercky.gamepadrobot.GameData
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

class Robot {
    val robot = Robot()
    val movingMouse = mutableListOf<GameData>()

    init {
        robot.autoDelay = 40
        robot.isAutoWaitForIdle = true
    }
    fun leftClick() {
        robot.mousePress(InputEvent.BUTTON1_MASK)
        robot.delay(40)
        robot.mouseRelease(InputEvent.BUTTON1_MASK)
        robot.delay(40)
    }
    fun rightClick() {
        robot.mousePress(InputEvent.BUTTON3_MASK)
        robot.delay(40)
        robot.mouseRelease(InputEvent.BUTTON3_MASK)
        robot.delay(40)
    }
    fun type(str: String) {
        for (character in str) {
            var code = character as Int

            if (code !in 96..123) code -= 32

            robot.delay(40)
            robot.keyPress(code)
            robot.keyRelease(code)
        }
    }
    fun keyPress(key: Int) {
        try {
            robot.delay(40)
            robot.keyPress(key)
        } catch (ex: IllegalArgumentException) {
            println("Illegal argument: could not write keyCode: $key")
        }
    }
    fun keyRelease(key: Int) {
        try {
            robot.delay(40)
            robot.keyRelease(key)
        } catch (ex:IllegalArgumentException) {
            println("Illegal argument: could not write keyCode: $key")
        }
    }
    fun isMouseMovement(key: Key): Boolean {
        when(key) {
            Key.MOUSEMOVEX -> return true
            Key.MOUSEMOVEY -> return true
            else -> return false
        }
    }
    fun mouseMove(dx: Int, dy: Int) {
        val cPosition = MouseInfo.getPointerInfo().location
        try {
            robot.delay(40)
            robot.mouseMove(cPosition.x + dx, cPosition.y + dy)
        } catch (ex: Exception) {
            println("Couldn't move mouse with these arguments: dx:$dx; dy:$dy")
        }
    }
    fun mouseMove(gameData: GameData) {
        if (isMouseMovement(gameData.key)) {
            when (gameData.key) {
                Key.MOUSEMOVEX -> mouseMove((gameData.value * gameData.multiplyer).toInt(), 0)
                Key.MOUSEMOVEY -> mouseMove(0, (gameData.value * gameData.multiplyer).toInt())
            }
        }
    }
    fun pushMouseMovement(gameData: GameData) {
        for (data in movingMouse) {
            if (data.key == gameData.key) {
                if (gameData.value == 0f) {
                    movingMouse.remove(data)
                } else {
                    movingMouse.remove(data)
                    movingMouse.add(gameData)
                }
                return // done
            }
        }
        movingMouse.add(gameData) // not yet in movingMouse -> adding now
    }
    fun updateMouse() {
        for (data in movingMouse) {
            mouseMove(data)
            println("mouse updated")
        }
    }

    fun KeyToKeyEvent(key: Key): Int {
        when(key) {
            Key.A -> return KeyEvent.VK_A
            Key.Z -> return KeyEvent.VK_Z
            Key.E -> return KeyEvent.VK_E
            Key.R -> return KeyEvent.VK_R
            Key.T -> return KeyEvent.VK_T
            Key.Y -> return KeyEvent.VK_Y
            Key.U -> return KeyEvent.VK_U
            Key.I -> return KeyEvent.VK_I
            Key.O -> return KeyEvent.VK_O
            Key.P -> return KeyEvent.VK_P
            Key.Q -> return KeyEvent.VK_Q
            Key.S -> return KeyEvent.VK_S
            Key.D -> return KeyEvent.VK_D
            Key.F -> return KeyEvent.VK_F
            Key.G -> return KeyEvent.VK_G
            Key.H -> return KeyEvent.VK_H
            Key.J -> return KeyEvent.VK_J
            Key.K -> return KeyEvent.VK_K
            Key.L -> return KeyEvent.VK_L
            Key.M -> return KeyEvent.VK_M
            Key.W -> return KeyEvent.VK_W
            Key.X -> return KeyEvent.VK_X
            Key.C -> return KeyEvent.VK_C
            Key.V -> return KeyEvent.VK_V
            Key.B -> return KeyEvent.VK_B
            Key.N -> return KeyEvent.VK_N
            Key.SHIFT -> return KeyEvent.VK_SHIFT
            Key.CONTROL -> return KeyEvent.VK_CONTROL
            Key.F1 -> return KeyEvent.VK_1
            Key.F2 -> return KeyEvent.VK_2
            Key.F3 -> return KeyEvent.VK_3
            Key.F4 -> return KeyEvent.VK_4
            Key.F5 -> return KeyEvent.VK_5
            Key.F6 -> return KeyEvent.VK_6
            Key.F7 -> return KeyEvent.VK_7
            Key.F8 -> return KeyEvent.VK_8
            Key.F9 -> return KeyEvent.VK_9
            Key.F0 -> return KeyEvent.VK_0
            Key.SPACE -> return KeyEvent.VK_SPACE
            Key.ESC -> return KeyEvent.VK_ESCAPE
            else -> return KeyEvent.VK_UNDEFINED
        }
    }

    fun isClick(key: Key): Boolean {
        when(key) {
            Key.MOUSELEFT -> return true
            Key.MOUSERIGHT -> return true
            else -> return false
        }
    }
    fun click(key: Key) {
        if (isClick(key)) {
            when(key) {
                Key.MOUSERIGHT -> rightClick()
                Key.MOUSELEFT -> leftClick()
            }
        }
    }
}

enum class Key {
    MOUSEMOVEX, MOUSEMOVEY, A, Z, E, R, T, Y, U, I, O, P, Q, S, D, F, G, H, J, K, L, M, W, X, C, V, B, N, SHIFT, CONTROL, F1, F2, F3, F4, F5, F6, F7, F8, F9, F0, SPACE,
    MOUSELEFT, MOUSERIGHT, ESC, NOTHING
}