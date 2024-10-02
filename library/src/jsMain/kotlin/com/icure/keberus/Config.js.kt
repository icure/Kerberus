package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Config")
public class ConfigJs(
    public val id: String,
    internal val salts: Array<String>,
    internal val difficultyFactor: Int
)