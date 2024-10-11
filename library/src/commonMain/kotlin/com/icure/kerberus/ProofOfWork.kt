package com.icure.kerberus

import kotlinx.serialization.Serializable

@Serializable
internal data class ProofOfWork(
    val nonce: String,
)