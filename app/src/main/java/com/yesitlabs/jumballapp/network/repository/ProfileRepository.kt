package com.yesitlabs.jumballapp.network.repository


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.WebServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


class ProfileRepository(private val webService: WebServices = WebServices()) {

    fun getProfileData(successCallback: (response: NetworkResult<PlayerProfileModel>) -> Unit, token:String) {
        webService.getProfileData(token)
            .enqueue(object : retrofit2.Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        response.body()?.let { respApi ->
                            val success= respApi.get("success")
                            if (success.asBoolean) {
                                val data: JsonObject= respApi.get("data")as JsonObject
                                val gson = Gson()
                                val profile=gson.fromJson(data, PlayerProfileModel::class.java)
                                val result = NetworkResult.Success<PlayerProfileModel>(profile)
                                successCallback(result)
                            } else {
                                val result = NetworkResult.Error<PlayerProfileModel>(respApi.get("message").toString())
                                successCallback(result)
                            }
                        }?: successCallback(NetworkResult.Error("There was an unknown error"))
                    } catch (exception: java.lang.Exception) {
                        val result = NetworkResult.Error<PlayerProfileModel>(exception.message.toString())
                        successCallback(result)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val result = NetworkResult.Error<PlayerProfileModel>(t.message.toString())
                    successCallback(result)
                }
            })

       }



    fun sendProfileDataWithImage(successCallback: (response: NetworkResult<JsonObject>) -> Unit,
                                 token:String, name:String, countryId:Int, skillLevel:String,
                                 position:String,
                                 playStyle:String,
                                 worldCupId:Int,
                                 profileImage:String){
        var part:MultipartBody.Part?=null
        if (profileImage != ""){
            val file = File(profileImage)
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("image_profile", file.path, requestBody)
        }
        val reqName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val reqCountryId = countryId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val reqSkillLevel = skillLevel.toRequestBody("text/plain".toMediaTypeOrNull())
        val reqPosition = position.toRequestBody("text/plain".toMediaTypeOrNull())
        val reqPlayStyle = playStyle.toRequestBody("text/plain".toMediaTypeOrNull())
        val reqWorldCupId = worldCupId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        webService.sendProfileDataWithImage(token,reqName,reqCountryId,
            reqSkillLevel,reqPosition,reqPlayStyle,reqWorldCupId,part).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    response.body()?.let { respApi ->
                        val success= respApi.get("success")
                        if (success.asBoolean) {
                            val result = NetworkResult.Success(respApi)
                            successCallback(result)
                        } else {
                            val result = NetworkResult.Error<JsonObject>(respApi.get("message").toString())
                            successCallback(result)
                        }
                    }?: successCallback(NetworkResult.Error("There was an unknown error"))
                } catch (exception: java.lang.Exception) {
                    val result = NetworkResult.Error<JsonObject>(exception.message.toString())
                    successCallback(result)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                val result = NetworkResult.Error<JsonObject>(t.message.toString())
                successCallback(result)
            }
        })


    }


    companion object {
        @Volatile
        private var instance: ProfileRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ProfileRepository().also { instance = it }
        }
    }

}