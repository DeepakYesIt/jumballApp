package com.yesitlabs.jumballapp.model

import com.google.gson.annotations.SerializedName

data class WorldCupModel(
    @SerializedName("id") val id:Int,
    @SerializedName("country_id")var countryId :Int,
    @SerializedName("year")var year :String,
    @SerializedName("created_at") val createdAt:String,
    @SerializedName("updated_at")val updatedAt:String,
    @SerializedName("country_worldcup_flag") val countryWorldCupFlag :String,
    @SerializedName("country") val country:CountriesModel
    )
