package com.example.game2048

import kotlinx.serialization.Serializable

@Serializable
data class GameRepository(
    val bestScore: Int = 0,
    val savedField: List<List<Int>> = emptyList(),
    val savedScore: Int = 0
)
