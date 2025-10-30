package com.icure.kerberus

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

internal actual class PlatformBigInt(private val kBigInt: BigInteger) {
	actual fun div(other: PlatformBigInt): PlatformBigInt =
		PlatformBigInt(kBigInt.divide(other.kBigInt))

	actual fun approximateToDouble(): Double =
		kBigInt.doubleValue(false)

	actual fun minus(other: PlatformBigInt): PlatformBigInt =
		PlatformBigInt(kBigInt.minus(other.kBigInt))

	actual operator fun compareTo(other: PlatformBigInt): Int =
		kBigInt.compareTo(other.kBigInt)

	actual constructor(intValue: Int) : this(BigInteger(intValue))

	actual constructor(bytes: ByteArray) : this(
		BigInteger.fromByteArray(bytes, if (bytes.first() >= 0) Sign.POSITIVE else Sign.NEGATIVE)
	)
}