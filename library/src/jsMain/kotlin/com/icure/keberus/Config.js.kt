package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Config")
class ConfigJs(
    val id: String,
    val salts: Array<String>,
    val difficultyFactor: Int
)