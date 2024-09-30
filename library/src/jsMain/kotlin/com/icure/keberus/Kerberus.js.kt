package com.icure.keberus

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.Promise

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("genPow")
fun genPowJs(salt: String, phrase: ByteArray, difficultyFactor: Int): Promise<ProofOfWork> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(genPow(salt, phrase, difficultyFactor))
    }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("isValidPoW")
fun isValidPoWJs(pow: ProofOfWork, target: ByteArray, salt: String, targetDifficulty: Int): Promise<Boolean> = Promise { resolve, _ ->
    GlobalScope.launch {
        resolve(isValidPoW(pow, target, salt, targetDifficulty))
    }
}