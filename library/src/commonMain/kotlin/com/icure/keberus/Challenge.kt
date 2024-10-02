package com.icure.keberus

import com.icure.kryptom.crypto.defaultCryptoService
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

class Challenge(
    val configId: String,
    private val salt: String,
    private val serializedInput: String,
    private val difficultyFactor: Int
) {


    companion object {
        fun fromConfig(config: Config, serializedInput: String): List<Challenge> {
            return config.salts.map { salt ->
                Challenge(config.id, salt, serializedInput, config.difficultyFactor)
            }
        }
    }

    private val prefix: ByteArray
        get() = salt.encodeToByteArray() + serializedInput.encodeToByteArray()

    private val difficulty: BigInteger
        get() {
            val maxValue = BigInteger(2).pow(128).subtract(BigInteger.ONE)
            return maxValue.subtract(maxValue.divide(BigInteger.fromLong(difficultyFactor.toLong())))
        }

    private fun firstBytesAsBigInteger(bytes: ByteArray): BigInteger {
        // Take the first 16 bytes (128 bits)
        val first16Bytes = bytes.sliceArray(0 until 16)
        return BigInteger.fromByteArray(first16Bytes, Sign.POSITIVE)
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

    private suspend fun calculate(nonce: Long): BigInteger {
        val prefixHash = sha256(prefix)
        return score(prefixHash, nonce)
    }

    suspend fun proveWork(): ProofOfWork {
        val prefixHash = sha256(prefix)
        var n: Long = 0
        var result = BigInteger.ZERO
        while (result < difficulty) {
            n += 1
            result = score(prefixHash, n)
        }
        return ProofOfWork(n.toString())
    }

    suspend fun isValidProof(nonce: Long): Boolean {
        val calculatedResult = calculate(nonce)
        return calculatedResult >= difficulty
    }
}