package com.yesitlabs.jumballapp.model

data class LoginResp (
    val code: Int,
    val data: DataLogin,
    val success: Boolean,
    val message: String
)

data class DataLogin (
    val name: String,
    val token: String,
    val isProfileUpdate: Int,
    val is_profile_update: Int
)
