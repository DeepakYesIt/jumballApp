package com.yesitlabs.jumballapp.network.repository


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.WebServices
import retrofit2.Call
import retrofit2.Response


class PlayerProfileRepository(private val webService: WebServices = WebServices()) {

    fun getProfileData(successCallback: (response: NetworkResult<PlayerProfileModel>) -> Unit, token:String) {
        webService.getProfileData(token)
            .enqueue(object : retrofit2.Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        response.body()?.let { resp ->
                            val success= resp.get("success")
                            if (success.asBoolean) {
                                val data: JsonObject= resp.get("data")as JsonObject
                                val gson = Gson()
                                val profile=gson.fromJson(data, PlayerProfileModel::class.java)
                                val result = NetworkResult.Success<PlayerProfileModel>(profile)
                                successCallback(result)
                            } else {
                                val result = NetworkResult.Error<PlayerProfileModel>(resp.get("message").toString())
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

    fun sendProfileData(successCallback: (response: NetworkResult<JsonObject>) -> Unit,
                        token:String, name:String, countryId:Int, skillLevel:String,
                        position:String,
                        playStyle:String,
                        worldCupId:Int){

        webService.sendProfileData(token,name,countryId,
            skillLevel,position,playStyle,worldCupId).enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    response.body()?.let { resp ->
                        val success= resp.get("success")
                        if (success.asBoolean) {
                            val result = NetworkResult.Success(resp)
                            successCallback(result)
                        } else {
                            val result = NetworkResult.Error<JsonObject>(resp.get("message").toString())
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
        private var instance: PlayerProfileRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PlayerProfileRepository().also { instance = it }
        }
    }

}