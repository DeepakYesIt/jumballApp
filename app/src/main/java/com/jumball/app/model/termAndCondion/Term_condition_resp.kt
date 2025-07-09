package com.jumball.app.model.termAndCondion

data class Term_condition_resp(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)