package com.yesitlabs.jumballapp.model

data class SingUpResp (
    val code: Int,
    val data: DataSignUp,
    val success: Boolean,
    val message: String
)

data class DataSignUp (
    val name: String,
    val token: String,
    val isProfileUpdate: Int
)
