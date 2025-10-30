package com.icure.kerberus

internal expect class PlatformBigInt {
	constructor(intValue: Int)
	constructor(bytes: ByteArray)
	fun div(other: PlatformBigInt): PlatformBigInt
	fun minus(other: PlatformBigInt): PlatformBigInt
	operator fun compareTo(other: PlatformBigInt): Int
	fun approximateToDouble(): Double
}