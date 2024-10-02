package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Challenge")
public class ChallengeJs(
    public val id: String,
    internal val salts: Array<String>,
    internal val difficultyFactor: Int
)