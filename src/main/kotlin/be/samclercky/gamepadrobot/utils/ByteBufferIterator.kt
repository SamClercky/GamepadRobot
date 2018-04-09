package be.samclercky.gamepadrobot.utils

import java.nio.ByteBuffer

class ByteBufferIterator(val byteBuffer: ByteBuffer): Iterator<Int> {
    override fun hasNext(): Boolean = byteBuffer.hasRemaining()

    override fun next(): Int = byteBuffer.getInt()

    init {
        byteBuffer.position(0)
    }
}

operator fun ByteBuffer.iterator() = ByteBufferIterator(this)