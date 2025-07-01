package com.yesitlabs.jumballapp.model.forgotmodel

data class ForgotApiModel(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)