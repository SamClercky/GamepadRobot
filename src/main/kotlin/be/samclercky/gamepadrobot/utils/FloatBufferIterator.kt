package be.samclercky.gamepadrobot.utils

import java.nio.FloatBuffer
import kotlin.math.roundToInt

class FloatBufferIterator(val floatBuffer: FloatBuffer): Iterator<Int> {
    override fun hasNext(): Boolean = floatBuffer.hasRemaining()

    override fun next(): Int = floatBuffer.get().roundToInt()

    init {
        floatBuffer.position(0)
    }
}

operator fun FloatBuffer.iterator() = FloatBufferIterator(this)