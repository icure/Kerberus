package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
public class Solution(
    public val id: String,
    public val nonces: List<String>,
)
