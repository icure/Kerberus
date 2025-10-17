package com.icure.kerberus

import kotlinx.coroutines.yield

internal actual suspend fun doYield() =
	yield()