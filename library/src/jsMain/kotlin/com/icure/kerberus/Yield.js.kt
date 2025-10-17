package com.icure.kerberus

import kotlinx.coroutines.await
import kotlin.js.Promise

internal actual suspend fun doYield() =
	yieldingPromise().await()

private fun yieldingPromise(): Promise<Unit> =
	Promise { resolve, _ ->
		setTimeout({ println("Timeout 0 done") ; resolve(Unit) }, 0)
	}

private external fun setTimeout(callback: () -> Unit, delay: Int)