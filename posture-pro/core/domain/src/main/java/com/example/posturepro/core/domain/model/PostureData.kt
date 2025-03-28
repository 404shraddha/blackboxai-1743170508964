package com.example.posturepro.core.domain.model

data class PostureData(
    val angle: Float, // Tilt angle in degrees
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getPostureStatus(sensitivity: Int): PostureStatus {
        return when {
            angle < -sensitivity -> PostureStatus.Poor
            angle > sensitivity -> PostureStatus.Poor
            angle < -sensitivity/2 -> PostureStatus.Fair
            angle > sensitivity/2 -> PostureStatus.Fair
            else -> PostureStatus.Good
        }
    }
}

enum class PostureStatus {
    Good, Fair, Poor
}