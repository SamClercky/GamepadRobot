package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.robot.Key

// TODO: should be replaced later to JSON
class MinecraftGamepad {

    val mouseSensitvity: Float = 50f

    val buttons: HashMap<String, Key> = hashMapOf(
            "0" to Key.Q,
            "0 2" to Key.D,
            "1" to Key.Z,
            "1 2" to Key.S,
            "2" to Key.MOUSEMOVEX,
            "2 2" to Key.MOUSEMOVEX,
            "3" to Key.MOUSEMOVEY,
            "3 2" to Key.MOUSEMOVEY,
            "4" to Key.F6,
            "4 2" to Key.F7,
            "4 3" to Key.F8,
            "4 4" to Key.F5,
            "5" to Key.MOUSELEFT,
            "5 2" to Key.MOUSERIGHT,
            "5 3" to Key.SHIFT,
            "5 4" to Key.F9,
            "6" to Key.E,
            "6 2" to Key.ESC,
            "6 3" to Key.SPACE,
            "6 4" to Key.SPACE,
            "7" to Key.F2,
            "7 2" to Key.F1,
            "7 3" to Key.F4,
            "7 4" to Key.F3
    )

    fun  getCode(btn: String): Key {
        return buttons.get(btn) ?: Key.NOTHING
    }

    fun getUnMappedBtn(): Array<UnMappedBtn> {
        return arrayOf(
                UnMappedBtn("0", arrayOf(-1, 1)),
                UnMappedBtn("1", arrayOf(-1, 1)),
                UnMappedBtn("4", arrayOf(1, 256, 65536, 16777216)),
                UnMappedBtn("5", arrayOf(1, 256, 65536, 16777216)),
                UnMappedBtn("6", arrayOf(1, 256, 65536, 16777216)),
                UnMappedBtn("7", arrayOf(1, 256, 65536, 16777216))
        )
    }
}

data class UnMappedBtn(val btn: String, val values: Array<Int>)
