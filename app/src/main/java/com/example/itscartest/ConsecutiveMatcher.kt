package com.example.itscartest

/**
 * 같은 값이 연속으로 [requiredCount]번 들어올 때만 그 값을 확정한다.
 *
 * OCR은 프레임마다 결과가 흔들리고, 간판·차량 번호처럼 주변 숫자를 한 번 잘못 읽기도 한다.
 * 한 프레임만 보고 건물 카드를 띄우지 않도록, 같은 건물 번호가 연속 프레임에서 반복될 때만 표시한다.
 * Android API를 쓰지 않으므로 JVM 단위 테스트로 검증할 수 있다.
 */
class ConsecutiveMatcher(private val requiredCount: Int) {
    init {
        require(requiredCount >= 1) { "requiredCount는 1 이상이어야 합니다" }
    }

    private var candidate: String? = null
    private var count = 0

    /** 이번 프레임의 값(없으면 null)을 넣고, 확정되면 그 값을, 아니면 null을 돌려준다. */
    fun update(value: String?): String? {
        if (value == null) {
            reset()
            return null
        }
        if (value == candidate) {
            count += 1
        } else {
            candidate = value
            count = 1
        }
        return if (count >= requiredCount) value else null
    }

    fun reset() {
        candidate = null
        count = 0
    }
}
