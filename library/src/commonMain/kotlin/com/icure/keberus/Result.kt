package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
public class Result(
    public val id: String,
    internal val nonces: List<String>,
)
