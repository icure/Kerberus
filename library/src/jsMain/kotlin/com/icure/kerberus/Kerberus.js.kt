package com.icure.kerberus

import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.crypto.external.XCryptoService
import com.icure.kryptom.crypto.external.adaptExternalCryptoService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise
import kotlin.js.json

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("resolveChallenge")
public fun resolveChallengeJs(config: ChallengeJs, serializedInput: String, cryptoService: XCryptoService? = null, onProgress: (Double) -> Unit = {}): Promise<SolutionJs> = GlobalScope.promise {
    resolveChallenge(
        config = config.toConfig(),
        serializedInput = serializedInput,
        cryptoService = cryptoService ?.let { adaptExternalCryptoService(it) } ?: defaultCryptoService,
        onProgress = onProgress
    ).toSolutionJs()
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("validateSolution")
public fun validateSolutionJs(config: ChallengeJs, result: SolutionJs, serializedInput: String, cryptoService: XCryptoService? = null): Promise<Boolean> = GlobalScope.promise {
    validateSolution(
        config = config.toConfig(),
        result = result.toSolution(),
        serializedInput = serializedInput,
        cryptoService = cryptoService ?.let { adaptExternalCryptoService(it) } ?: defaultCryptoService
    )
}

private fun SolutionJs.toSolution(): Solution = Solution(
    id = id,
    nonces = nonces.toList()
)

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
private fun Solution.toSolutionJs(): SolutionJs = json(
    "id" to id,
    "nonces" to nonces.toTypedArray()
) as SolutionJs

private fun ChallengeJs.toConfig(): Challenge = Challenge(
    id = id,
    salts = salts.toList(),
    difficultyFactor = difficultyFactor
)