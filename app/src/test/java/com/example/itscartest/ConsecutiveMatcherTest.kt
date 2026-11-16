package com.example.itscartest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConsecutiveMatcherTest {

    @Test
    fun confirmsOnlyAfterRequiredConsecutiveFrames() {
        val matcher = ConsecutiveMatcher(3)
        assertNull(matcher.update("34"))
        assertNull(matcher.update("34"))
        assertEquals("34", matcher.update("34"))
    }

    @Test
    fun singleMisreadDoesNotConfirm() {
        val matcher = ConsecutiveMatcher(3)
        assertNull(matcher.update("7")) // 차량 번호 등을 한 번 잘못 읽음
        assertNull(matcher.update(null))
        assertNull(matcher.update("34"))
        assertNull(matcher.update("7"))
    }

    @Test
    fun differentNumberOrEmptyFrameRestartsTheCount() {
        val matcher = ConsecutiveMatcher(3)
        matcher.update("34")
        matcher.update("34")
        assertNull(matcher.update("39"))
        assertNull(matcher.update("39"))
        assertEquals("39", matcher.update("39"))

        matcher.update("2")
        matcher.update(null) // 숫자가 없는 프레임
        assertNull(matcher.update("2"))
    }

    @Test
    fun resetClearsProgress() {
        val matcher = ConsecutiveMatcher(2)
        matcher.update("51")
        matcher.reset()
        assertNull(matcher.update("51"))
        assertEquals("51", matcher.update("51"))
    }

    @Test
    fun requiredCountOfOneConfirmsImmediately() {
        assertEquals("1", ConsecutiveMatcher(1).update("1"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsInvalidRequiredCount() {
        ConsecutiveMatcher(0)
    }
}
