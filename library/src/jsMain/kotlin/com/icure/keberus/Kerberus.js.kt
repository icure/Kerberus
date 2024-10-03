package com.icure.keberus

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.Promise

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("resolveChallenge")
public fun resolveChallengeJs(config: ChallengeJs, serializedInput: String, onProgress: (Double) -> Unit = {}): Promise<SolutionJs> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(
            resolveChallenge(
                config = config.toConfig(),
                serializedInput = serializedInput,
                onProgress = onProgress
            ).toSolutionJs()
        )
    }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("validateSolution")
public fun validateSolutionJs(config: ChallengeJs, result: SolutionJs, serializedInput: String): Promise<Boolean> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(validateSolution(config.toConfig(), result.toSolution(), serializedInput))
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