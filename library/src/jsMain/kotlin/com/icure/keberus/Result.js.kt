package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Result")
class ResultJs(
    val id: String,
    val nonces: Array<String>,
)
