package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
class Result(
    val id: String,
    val nonces: List<String>,
)
