package com.icure.keberus

suspend fun genPow(salt: String, phrase: ByteArray, difficultyFactor: Int): ProofOfWork {
    val config = Config(salt)
    return config.proveWork(phrase, difficultyFactor)
}

suspend fun isValidPoW(pow: ProofOfWork, target: ByteArray, salt: String, targetDifficulty: Int): Boolean {
    val config = Config(salt)
    return config.isValidProof(pow, target, targetDifficulty)
}