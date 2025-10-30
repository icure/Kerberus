package com.icure.kerberus

internal actual class PlatformBigInt private constructor (private val jsBigInt: BigInt) {
	actual fun div(other: PlatformBigInt): PlatformBigInt {
		val a = jsBigInt
		val b = other.jsBigInt
		@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
		return PlatformBigInt(js("a / b") as BigInt)
	}

	actual fun approximateToDouble(): Double {
		val a = jsBigInt
		return js("Number(a)")
	}

	actual fun minus(other: PlatformBigInt): PlatformBigInt {
		val a = jsBigInt
		val b = other.jsBigInt
		@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
		return PlatformBigInt(js("a - b") as BigInt)
	}

	actual operator fun compareTo(other: PlatformBigInt): Int {
		val a = jsBigInt
		val b = other.jsBigInt
		return js("a === b ? 0 : a < b ? -1 : 1")
	}

	actual constructor(intValue: Int) : this(BigInt(intValue))

	actual constructor(bytes: ByteArray) : this(BigInt("0x" + bytes.toHexString()))
}

private external interface BigInt
private external fun BigInt(i: Int): BigInt
private external fun BigInt(s: String): BigInt