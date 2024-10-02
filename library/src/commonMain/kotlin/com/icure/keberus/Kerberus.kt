package com.icure.keberus

public suspend fun genPow(config: Config, serializedInput: String): Result {
    val challenge = Challenge.fromConfig(config, serializedInput)
    return Result(
        id = config.id,
        nonces = challenge.map { it.proveWork() }.map { it.nonce }
    )
}

public suspend fun isValidPow(config: Config, result: Result, serializedInput: String): Boolean {
    val challenges = Challenge.fromConfig(config, serializedInput)
    return challenges.withIndex().all { (index, challenge) ->
        challenge.isValidProof(result.nonces[index].toLong())
    }
}


