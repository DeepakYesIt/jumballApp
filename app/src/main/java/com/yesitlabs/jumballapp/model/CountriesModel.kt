package com.yesitlabs.jumballapp.model

import com.google.gson.annotations.SerializedName

data class CountriesModel(
    @SerializedName("id")   var id :Int,
    @SerializedName("name") var name:String,
    @SerializedName("country_worldcup_flag") var country_worldcup_flag:String,
    @SerializedName("created_at")var created_at:String,
    @SerializedName("updated_at")var updated_at:String
    )
