package be.samclercky.gamepadrobot.robot

import be.samclercky.gamepadrobot.GameData
import kotlinx.coroutines.experimental.newSingleThreadContext
import java.awt.MouseInfo
import java.awt.Robot
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

/**
 * Interacts with the Robot-class from java.awt.*
 */
class Robot {

    companion object {
        val workContext = newSingleThreadContext("workContext")
    }

    private val robot = Robot()
    private val movingMouse = mutableListOf<GameData>()
    private val pressedBtn: HashMap<Int, Boolean> by lazy {
        val result = hashMapOf<Int, Boolean>()

        for (key in Key.values()) {
            result.put(key.keyCode, false)
        }

        result
    }

    init {
        robot.autoDelay = 40
        robot.isAutoWaitForIdle = true
    }

    /**
     * Presses the left mouse button
     */
    fun leftClickPress() {
        if (pressedBtn.get(Key.MOUSELEFT.keyCode) == true) return
        robot.delay(40)
        robot.mousePress(InputEvent.BUTTON1_MASK)
        pressedBtn.set(Key.MOUSELEFT.keyCode, true)
    }

    /**
     * Releases the left mouse button
     */
    fun leftClickRelease() {
        if (!(pressedBtn.get(Key.MOUSELEFT.keyCode) == true)) return // only release when the key is pressed
        robot.delay(40)
        robot.mouseRelease(InputEvent.BUTTON1_MASK)
        pressedBtn.set(Key.MOUSELEFT.keyCode, false)
    }

    /**
     * Presses the right mouse button
     */
    fun rightClickPress() {
        if (pressedBtn.get(Key.MOUSERIGHT.keyCode) ?: true) return
        robot.delay(40)
        robot.mousePress(InputEvent.BUTTON3_MASK)
        pressedBtn.set(Key.MOUSERIGHT.keyCode, true)
    }

    /**
     * Releases the right mouse button
     */
    fun rightClickRelease() {
        if (!(pressedBtn.get(Key.MOUSERIGHT.keyCode) ?: true)) return // only release when the key is pressed
        robot.delay(40)
        robot.mouseRelease(InputEvent.BUTTON3_MASK)
        pressedBtn.set(Key.MOUSERIGHT.keyCode, false)
    }

    /**
     * Types a string via the keyboard
     * @param str String to write
     */
    fun type(str: String) {
        for (character in str) {
            var code = character as Int

            if (code !in 96..123) code -= 32

            robot.delay(40)
            robot.keyPress(code)
            robot.keyRelease(code)
        }
    }

    /**
     * Presses any key -> !IMPORTANT! Call always right after keyRelease(keyCode)
     * @param key keycode of the key
     */
    fun keyPress(key: Int) {
        if (pressedBtn.get(key) ?: true) return // The key pressed, so should not be pressed again
        try {
            robot.delay(40)
            robot.keyPress(key)
        } catch (ex: IllegalArgumentException) {
            println("Illegal argument: could not write keyCode: $key")
        }
        pressedBtn.set(key, true)
    }

    /**
     * Releases a key
     * @param key keycode of the key
     */
    fun keyRelease(key: Int) {
        if (!(pressedBtn.get(key) ?: true)) return
        try {
            robot.delay(40)
            robot.keyRelease(key)
        } catch (ex:IllegalArgumentException) {
            println("Illegal argument: could not write keyCode: $key")
        }
        pressedBtn.set(key, false)
    }

    /**
     * Detects if the command is a mousemovement
     * @param key Keycommand to inspect
     */
    fun isMouseMovement(key: Key): Boolean {
        when(key) {
            Key.MOUSEMOVEX -> return true
            Key.MOUSEMOVEY -> return true
            else -> return false
        }
    }

    /**
     * Moves the mouse relatively from the current place
     * @param dx Relative x-position
     * @param dy Relative y-position
     */
    fun mouseMove(dx: Int, dy: Int) {
        val cPosition = MouseInfo.getPointerInfo().location
        try {
            robot.delay(40)
            robot.mouseMove(cPosition.x + dx, cPosition.y + dy)
        } catch (ex: Exception) {
            println("Couldn't move mouse with these arguments: dx:$dx; dy:$dy")
        }
    }

    /**
     * Moves the mouse relatively from the current place
     * @param gameData The data that describes the movement
     */
    fun mouseMove(gameData: GameData) {
        if (isMouseMovement(gameData.key)) {
            when (gameData.key) {
                Key.MOUSEMOVEX -> mouseMove((gameData.value * gameData.multiplyer).toInt(), 0)
                Key.MOUSEMOVEY -> mouseMove(0, (gameData.value * gameData.multiplyer).toInt())
            }
        }
    }

    /**
     * For longer movements, you can call this function -> !IMPORTANT! Call this function also with a 0 movement to stop it
     * @param gameData The data that describes the movement
     */
    fun pushMouseMovement(gameData: GameData) {
        for (data in movingMouse) {
            if (data.key == gameData.key) {
                if (gameData.value == 0) {
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

    /**
     * For pushMouseMovement() to work, this function updates the current position of the mouse
     */
    fun updateMouse() {
        for (data in movingMouse) {
            mouseMove(data)
            println("mouse updated")
        }
    }

    /**
     * Maps the Key to a keyCode
     * @param key Key to be converted
     * @return Corresponding keycode
     */
    @Deprecated("Use keyCode-property of Key instead")
    fun KeyToKeyEvent(key: Key): Int = key.keyCode

    /**
     * Checks if the command is a key
     * @param key The command that has to be checked
     * @return True if it is a click-command
     */
    fun isClick(key: Key): Boolean {
        when(key) {
            Key.MOUSELEFT -> return true
            Key.MOUSERIGHT -> return true
            else -> return false
        }
    }

    /**
     * Simplified version of all *click-functions
     * @param key Command that has to be excecuted
     */
    fun click(key: Key) {
        if (isClick(key)) {
            when(key) {
                Key.MOUSERIGHT -> {
                    rightClickPress()
                    rightClickRelease()
                }
                Key.MOUSELEFT -> {
                    leftClickPress()
                    leftClickRelease()
                }
            }
        }
    }
    fun clickPress(key: Key) {
        if (isClick(key)) {
            when(key) {
                Key.MOUSERIGHT -> {
                    rightClickPress()
                }
                Key.MOUSELEFT -> {
                    leftClickPress()
                }
            }
        }
    }
    fun clickRelease(key: Key) {
        if (isClick(key)) {
            when(key) {
                Key.MOUSERIGHT -> {
                    rightClickRelease()
                }
                Key.MOUSELEFT -> {
                    leftClickRelease()
                }
            }
        }
    }
}

/**
 * All supported commands
 */
enum class Key(val keyCode: Int) {
    MOUSEMOVEX(-1),
    MOUSEMOVEY(-2),
    MOUSELEFT(-3),
    MOUSERIGHT(-4),
    A(KeyEvent.VK_A),
    Z(KeyEvent.VK_Z),
    E(KeyEvent.VK_E),
    R(KeyEvent.VK_R),
    T(KeyEvent.VK_T),
    Y(KeyEvent.VK_Y),
    U(KeyEvent.VK_U),
    I(KeyEvent.VK_I),
    O(KeyEvent.VK_O),
    P(KeyEvent.VK_P),
    Q(KeyEvent.VK_Q),
    S(KeyEvent.VK_S),
    D(KeyEvent.VK_D),
    F(KeyEvent.VK_F),
    G(KeyEvent.VK_G),
    H(KeyEvent.VK_H),
    J(KeyEvent.VK_J),
    K(KeyEvent.VK_K),
    L(KeyEvent.VK_L),
    M(KeyEvent.VK_M),
    W(KeyEvent.VK_W),
    X(KeyEvent.VK_X),
    C(KeyEvent.VK_C),
    V(KeyEvent.VK_V),
    B(KeyEvent.VK_B),
    N(KeyEvent.VK_N),
    SHIFT(KeyEvent.VK_SHIFT),
    CONTROL(KeyEvent.VK_CONTROL),
    F1(KeyEvent.VK_1),
    F2(KeyEvent.VK_2),
    F3(KeyEvent.VK_3),
    F4(KeyEvent.VK_4),
    F5(KeyEvent.VK_5),
    F6(KeyEvent.VK_6),
    F7(KeyEvent.VK_7),
    F8(KeyEvent.VK_8),
    F9(KeyEvent.VK_9),
    F0(KeyEvent.VK_0),
    SPACE(KeyEvent.VK_SPACE),
    ESC(KeyEvent.VK_ESCAPE),
    NOTHING(KeyEvent.VK_UNDEFINED)
}