package com.yesitlabs.jumballapp.model.privacyPolicy

data class PrivacyPolicyResp(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)