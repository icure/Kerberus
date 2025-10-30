package com.icure.kerberus

import java.math.BigInteger

internal actual class PlatformBigInt private constructor (private val javaBigInt: BigInteger) {
	actual fun div(other: PlatformBigInt): PlatformBigInt =
		PlatformBigInt(javaBigInt / other.javaBigInt)

	actual fun approximateToDouble(): Double =
		javaBigInt.toDouble()

	actual fun minus(other: PlatformBigInt): PlatformBigInt =
		PlatformBigInt(javaBigInt - other.javaBigInt)

	actual operator fun compareTo(other: PlatformBigInt): Int =
		javaBigInt.compareTo(other.javaBigInt)

	actual constructor(intValue: Int) : this(intValue.toBigInteger())

	actual constructor(bytes: ByteArray) : this(BigInteger(bytes))
}