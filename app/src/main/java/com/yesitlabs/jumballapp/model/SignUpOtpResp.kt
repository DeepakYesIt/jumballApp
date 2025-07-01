package com.yesitlabs.jumballapp.model

data class SignupOtpResp (
    val code: Int,
    val data: OtpData,
    val success: Boolean,
    val message: String
)

data class OtpData (
    val request: Request,
    val otp: Int
)

data class Request (
    val email: String
)
