package com.yesitlabs.jumballapp.model

data class Setting(
    val code: Int,
    val `data`: DataSetting,
    val message: String,
    val success: Boolean
)

data class DataSetting(
    val setting: SettingX
)

data class SettingX(
    val created_at: String,
    val id: Int,
    val music: Int,
    val sound_effect: Int,
    val updated_at: String,
    val user_id: Int
)