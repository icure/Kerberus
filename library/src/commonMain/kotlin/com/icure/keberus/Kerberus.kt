package com.icure.keberus

public suspend fun resolveChallenge(config: Challenge, serializedInput: String): Solution {
    val challenges = ChallengePieceResolver.forChallenge(config, serializedInput)
    return Solution(
        id = config.id,
        nonces = challenges.map { it.resolve() }.map { it.nonce }
    )
}

public suspend fun validateSolution(config: Challenge, result: Solution, serializedInput: String): Boolean {
    val challenges = ChallengePieceResolver.forChallenge(config, serializedInput)
    return challenges.withIndex().all { (index, challenge) ->
        challenge.validate(result.nonces[index].toLong())
    }
}


