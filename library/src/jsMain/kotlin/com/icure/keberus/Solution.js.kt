package com.icure.keberus

import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
@JsName("Solution")
public class SolutionJs(
    public val id: String,
    internal val nonces: Array<String>,
)
