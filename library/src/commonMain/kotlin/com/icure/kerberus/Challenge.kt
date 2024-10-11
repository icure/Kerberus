package com.icure.kerberus

import kotlinx.serialization.Serializable

@Serializable
public class Challenge(
    public val id: String,
    public val salts: List<String>,
    public val difficultyFactor: Int
)
