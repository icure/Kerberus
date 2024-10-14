package com.icure.kerberus

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Challenge")
public external interface ChallengeJs {
    public val id: String
    public val salts: Array<String>
    public val difficultyFactor: Int
}