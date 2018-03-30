package be.samclercky.gamepadrobot

import be.samclercky.gamepadrobot.robot.Key

// TODO: should be replaced later to JSON
class MinecraftGamepad {

    val mouseSensitvity: Float = 10f

    fun getCode(btn: String): Key {
        when(btn) {
            "Trigger" -> return Key.F6
            "Thumb" -> return Key.F7
            "Thumb 2" -> return Key.F8
            "Top" -> return Key.F5
            "Top 2" -> return Key.MOUSELEFT
            "Pinkie" -> return Key.MOUSERIGHT
            "Base" -> return Key.SPACE
            "Base 2" -> return Key.F9
            "x" -> return Key.Q
            "x 2" -> return Key.D
            "y" -> return Key.Z
            "y 2" -> return Key.S
            "z" -> return Key.MOUSEMOVEX
            "z 2" -> return Key.MOUSEMOVEX
            "rz" -> return Key.MOUSEMOVEY
            "rz 2" -> return Key.MOUSEMOVEY
            "pov" -> return Key.F2
            "pov 2" -> return Key.F1
            "pov 3" -> return Key.F4
            "pov 4" -> return Key.F3
            "Base 3" -> return Key.E
            "Base 4" -> return Key.SHIFT
            "Base 5" -> return Key.ESC
            "Base 6" -> return Key.ESC
        }
        return Key.NOTHING
    }

    fun getUnMappedBtn(): Array<UnMappedBtn> {
        return arrayOf(
                UnMappedBtn("pov", arrayOf<Float>(0.25f, 0.5f, 0.75f, 1f)),
                UnMappedBtn("x", arrayOf<Float>(-1f, 1f)),
                UnMappedBtn("y", arrayOf<Float>(-1f, 1f)),
                UnMappedBtn("z", arrayOf<Float>(-1f, 1f)),
                UnMappedBtn("rz", arrayOf<Float>(-1f, 1f))
        )
    }
}

data class UnMappedBtn(val btn: String, val values: Array<Float>)