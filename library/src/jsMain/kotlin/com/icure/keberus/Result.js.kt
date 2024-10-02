package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Result")
public class ResultJs(
    public val id: String,
    internal val nonces: Array<String>,
)
