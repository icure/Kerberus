package com.icure.keberus

import kotlinx.serialization.Serializable

@Serializable
internal data class ProofOfWork(
    val nonce: String,
)