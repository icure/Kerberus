package com.icure.keberus

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.Promise

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("genPow")
fun genPowJs(config: ConfigJs, serializedInput: String): Promise<ResultJs> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(genPow(config.toConfig(), serializedInput).toResultJs())
    }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("isValidPoW")
fun isValidPoWJs(config: ConfigJs, result: ResultJs, serializedInput: String): Promise<Boolean> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(isValidPow(config.toConfig(), result.toResult(), serializedInput))
    }
}

private fun ResultJs.toResult(): Result = Result(
    id = id,
    nonces = nonces.toList()
)

private fun Result.toResultJs(): ResultJs = ResultJs(
    id = id,
    nonces = nonces.toTypedArray()
)

private fun ConfigJs.toConfig(): Config = Config(
    id = id,
    salts = salts.toList(),
    difficultyFactor = difficultyFactor
)