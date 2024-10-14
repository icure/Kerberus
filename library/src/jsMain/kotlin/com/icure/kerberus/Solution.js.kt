package com.icure.kerberus

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("Solution")
public external interface SolutionJs {
    public val id: String
    public val nonces: Array<String>
}
