package com.icure.keberus

import com.icure.kryptom.crypto.CryptoService

public suspend fun resolveChallenge(config: Challenge, serializedInput: String, cryptoService: CryptoService, onProgress: (Double) -> Unit = {}): Solution {
    val challenges = ChallengePieceResolver.forChallenge(config, serializedInput, cryptoService)

    var lastSentProgress = 0.0

    return Solution(
        id = config.id,
        nonces = challenges.mapIndexed() { index, challenge ->
            challenge.resolve { challengeProgress ->
                val progress = (index + challengeProgress) / challenges.size.toDouble()
                if (progress - lastSentProgress > 0.01) {
                    lastSentProgress = progress
                    onProgress(progress)
                }
            }
        }.map { it.nonce }.toList()
    ).also {
        onProgress(1.0)
    }
}

public suspend fun validateSolution(config: Challenge, result: Solution, serializedInput: String, cryptoService: CryptoService): Boolean {
    val challenges = ChallengePieceResolver.forChallenge(config, serializedInput, cryptoService)
    return challenges.withIndex().all { (index, challenge) ->
        challenge.validate(result.nonces[index].toLong())
    }
}


