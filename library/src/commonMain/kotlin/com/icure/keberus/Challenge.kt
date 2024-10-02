package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
public class Challenge(
    public val id: String,
    internal val salts: List<String>,
    internal val difficultyFactor: Int
)
