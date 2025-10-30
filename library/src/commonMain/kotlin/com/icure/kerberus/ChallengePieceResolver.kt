package com.icure.kerberus

import com.icure.kryptom.crypto.CryptoService
import org.kotlincrypto.hash.sha2.SHA256

internal class ChallengePieceResolver(
    private val salt: String,
    private val serializedInput: String,
    private val difficultyFactor: Int,
    private val sha256: suspend (data: ByteArray) -> ByteArray
) {

    companion object {
        private val ZERO = PlatformBigInt(0)
        private val MAX_UINT_128 = PlatformBigInt(ByteArray(17) { if (it == 0) 0 else -1 })
        private val MAX_UINT_128_AS_DOUBLE = MAX_UINT_128.approximateToDouble()

        @Suppress("RedundantSuspendModifier") // Must be suspend for compatibility with Kryptom sha256
        private suspend fun pureKotlinSha256(data: ByteArray): ByteArray {
            val digest = SHA256()
            digest.update(data)
            return digest.digest()
        }

        fun forChallenge(
            config: Challenge,
            serializedInput: String,
            cryptoService: CryptoService?
        ): List<ChallengePieceResolver> {
            val doSha = cryptoService?.run { digest::sha256 } ?: kotlin.run {
                Logger.w {
                    "Kerberus is computing captchas using a non-native hashing method.\n" +
                    "For better performances provide a crypto service matching your environment (e.g. on ReactNative use @icure/nitro-kryptom)"
                }
                ::pureKotlinSha256
            }
            return config.salts.map { salt ->
                ChallengePieceResolver(salt, serializedInput, config.difficultyFactor, doSha)
            }
        }
    }

    private val prefix: ByteArray
        get() = salt.encodeToByteArray() + serializedInput.encodeToByteArray()

    private val difficulty by lazy {
        MAX_UINT_128.minus(MAX_UINT_128.div(PlatformBigInt(difficultyFactor)))
    }
    private val difficultyDouble by lazy {
        difficulty.approximateToDouble()
    }

    private fun firstBytesAsBigInteger(bytes: ByteArray): PlatformBigInt {
        return PlatformBigInt(byteArrayOf(0) + bytes.sliceArray(0 until 16))
    }

    private suspend fun score(prefixHash: ByteArray, nonce: Long): PlatformBigInt {
        return nonce.toString().encodeToByteArray().let { nonceBytes ->
            prefixHash + nonceBytes
        }.let { hashInput ->
            sha256(hashInput)
        }.let { hash ->
            firstBytesAsBigInteger(hash)
        }
    }

    private suspend fun calculate(nonce: Long): PlatformBigInt {
        val prefixHash = sha256(prefix)
        return score(prefixHash, nonce)
    }

    suspend fun resolve(onProgress: suspend (Double) -> Unit): ProofOfWork {
        val prefixHash = sha256(prefix)

        val probabilityOfNotSuccessOnEachNonce = difficultyDouble / MAX_UINT_128_AS_DOUBLE
        var progressAccumulator = 1.0

        var nonce: Long = 0
        var result = ZERO

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