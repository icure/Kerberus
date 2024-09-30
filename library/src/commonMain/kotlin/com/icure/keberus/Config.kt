package com.icure.keberus

import com.icure.kryptom.crypto.defaultCryptoService
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

data class Config(
    val salt: String
) {

    suspend fun proveWork(prefix: ByteArray, difficulty: Int): ProofOfWork {
        val prefixHash = sha256(salt.encodeToByteArray() + prefix)
        var n: Long = 0
        var result = BigInteger.ZERO
        val targetDifficulty = getDifficulty(difficulty)
        while (result < targetDifficulty) {
            n += 1
            result = score(prefixHash, n)
        }
        return ProofOfWork(n.toString())
    }

    private suspend fun calculate(nonce: Long, phrase: ByteArray): BigInteger {
        val prefixHash = sha256(salt.encodeToByteArray() + phrase)
        return score(prefixHash, nonce)
    }

    suspend fun isValidProof(pow: ProofOfWork, t: ByteArray, targetDifficulty: Int): Boolean {
        val calculatedResult = calculate(pow.nonce.toLong(), t)
        return calculatedResult >= getDifficulty(targetDifficulty)
    }

    private suspend fun sha256(input: ByteArray): ByteArray {
        return defaultCryptoService.digest.sha256(input)
    }

    private suspend fun score(prefixHash: ByteArray, nonce: Long): BigInteger {
        val nonceBytes = nonce.toString().encodeToByteArray()
        val hashInput = prefixHash + nonceBytes
        val hash = sha256(hashInput)
        return firstBytesAsBigInteger(hash)
    }




    private fun firstBytesAsBigInteger(bytes: ByteArray): BigInteger {
        // Take the first 16 bytes (128 bits)
        val first16Bytes = bytes.sliceArray(0 until 16)
        return BigInteger.fromByteArray(first16Bytes, Sign.POSITIVE)
    }

    private fun getDifficulty(difficultyFactor: Int): BigInteger {
        val maxValue = BigInteger(2).pow(128).subtract(BigInteger.ONE)
        return maxValue.subtract(maxValue.divide(BigInteger.fromLong(difficultyFactor.toLong())))
    }
}