package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
data class ProofOfWork(
    val nonce: String,
)