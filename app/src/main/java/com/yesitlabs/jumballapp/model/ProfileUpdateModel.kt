package com.yesitlabs.jumballapp.model

data class ProfileUpdateModel(
    val country: String,
    val country_name: String,
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val fcm_token: Any,
    val id: Int,
    val is_profile_update: Int,
    val name: String,
    val otp: Any,
    val otp_time: Any,
    val play_style: String,
    val position: String,
    val profile_images: String,
    val role: Int,
    val skill_level: String,
    val status: Int,
    val updated_at: String,
    val world_cup: String
)