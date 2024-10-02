package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
class Config(
    val id: String,
    val salts: List<String>,
    val difficultyFactor: Int
)
