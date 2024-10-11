package com.icure.kerberus

import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.crypto.external.XCryptoService
import com.icure.kryptom.crypto.external.adaptExternalCryptoService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.Promise

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("resolveChallenge")
public fun resolveChallengeJs(config: ChallengeJs, serializedInput: String, cryptoService: XCryptoService? = null, onProgress: (Double) -> Unit = {}): Promise<SolutionJs> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(
            resolveChallenge(
                config = config.toConfig(),
                serializedInput = serializedInput,
                cryptoService = cryptoService ?.let { adaptExternalCryptoService(it) } ?: defaultCryptoService,
                onProgress = onProgress
            ).toSolutionJs()
        )
    }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("validateSolution")
public fun validateSolutionJs(config: ChallengeJs, result: SolutionJs, serializedInput: String, cryptoService: XCryptoService? = null): Promise<Boolean> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(
            validateSolution(
            config = config.toConfig(),
            result = result.toSolution(),
            serializedInput = serializedInput,
            cryptoService = cryptoService ?.let { adaptExternalCryptoService(it) } ?: defaultCryptoService)
        )
    }
}

private fun SolutionJs.toSolution(): Solution = Solution(
    id = id,
    nonces = nonces.toList()
)

private fun Solution.toSolutionJs(): SolutionJs = SolutionJs(
    id = id,
    nonces = nonces.toTypedArray()
)

private fun ChallengeJs.toConfig(): Challenge = Challenge(
    id = id,
    salts = salts.toList(),
    difficultyFactor = difficultyFactor
)