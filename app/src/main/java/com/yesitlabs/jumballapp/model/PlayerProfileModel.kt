package com.yesitlabs.jumballapp.model

import com.google.gson.annotations.SerializedName

data class PlayerProfileModel(

   val code: Int,
   val data: DataModel?,
   val success: Boolean,
   val message: String,


)

data class DataModel(
   @SerializedName("skill_level")
   val skillLevel:MutableList<SkillLevel>?,
   @SerializedName("countries")
   val countries: MutableList<CountriesModel>?,
   @SerializedName("user")
   val user:UserModel,
   @SerializedName("position")
   val position: MutableList<SkillLevel>?,
   @SerializedName("style_play")
   val stylePlay :MutableList<SkillLevel>?,
   @SerializedName("world_cup")
   val worldCup:MutableList<WorldCupModel>?
)
