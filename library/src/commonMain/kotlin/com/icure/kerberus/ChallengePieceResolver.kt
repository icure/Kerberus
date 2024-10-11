package com.icure.kerberus

import com.icure.kryptom.crypto.CryptoService
import com.icure.kryptom.crypto.DigestService
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

internal class ChallengePieceResolver(
    private val salt: String,
    private val serializedInput: String,
    private val difficultyFactor: Int,
    private val digestService: DigestService
) {

    companion object {
        val MAX_UINT_128 = BigInteger(2).pow(128).subtract(BigInteger.ONE)
        val MAX_UINT_128_AS_DOUBLE = MAX_UINT_128.doubleValue(false)

        fun forChallenge(config: Challenge, serializedInput: String, cryptoService: CryptoService): List<ChallengePieceResolver> {
            return config.salts.map { salt ->
                ChallengePieceResolver(salt, serializedInput, config.difficultyFactor, cryptoService.digest)
            }
        }
    }

    private val prefix: ByteArray
        get() = salt.encodeToByteArray() + serializedInput.encodeToByteArray()

    private val difficulty: BigInteger
        get() {
            val maxValue = MAX_UINT_128
            return maxValue.subtract(maxValue.divide(BigInteger.fromLong(difficultyFactor.toLong())))
        }

    private fun firstBytesAsBigInteger(bytes: ByteArray): BigInteger {
        // Take the first 16 bytes (128 bits)
        val first16Bytes = bytes.sliceArray(0 until 16)
        return BigInteger.fromByteArray(first16Bytes, Sign.POSITIVE)
    }

    private suspend fun sha256(input: ByteArray): ByteArray {
        return digestService.sha256(input)
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

    suspend fun resolve(onProgress: (Double) -> Unit): ProofOfWork {
        val prefixHash = sha256(prefix)

        val probabilityOfNotSuccessOnEachNonce = difficulty.doubleValue(false) / MAX_UINT_128_AS_DOUBLE
        var progressAccumulator = 1.0

        var nonce: Long = 0
        var result = BigInteger.ZERO
        while (result < difficulty) {
            nonce += 1
            result = score(prefixHash, nonce)

            progressAccumulator *= probabilityOfNotSuccessOnEachNonce
            onProgress(1 - progressAccumulator)
        }
        return ProofOfWork(nonce.toString())
    }

    suspend fun validate(nonce: Long): Boolean {
        val calculatedResult = calculate(nonce)
        return calculatedResult >= difficulty
    }
}