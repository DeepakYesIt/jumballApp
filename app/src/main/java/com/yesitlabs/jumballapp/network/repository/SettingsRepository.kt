package com.yesitlabs.jumballapp.network.repository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.Setting
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.WebServices
import retrofit2.Call
import retrofit2.Response


class SettingsRepository(private val webService: WebServices = WebServices()) {

    fun profileDelete(successCallback: (response: NetworkResult<JsonObject>) -> Unit, token:String) {
        webService.profileDelete(token)
            .enqueue(object : retrofit2.Callback<JsonObject> {
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
                        }
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

    fun userLogout(successCallback: (response: NetworkResult<JsonObject>) -> Unit, token:String) {
        webService.userLogout(token)
            .enqueue(object : retrofit2.Callback<JsonObject> {

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

    fun setting(successCallback: (response: NetworkResult<Setting>) -> Unit, token:String) {
        webService.setting(token)
            .enqueue(object : retrofit2.Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        response.body()?.let { respApi ->
                            val success= respApi.get("success")
                            if (success.asBoolean) {
                                val gson = Gson()
                                val setting=gson.fromJson(respApi, Setting::class.java)
                                val result = NetworkResult.Success<Setting>(setting)
                                successCallback(result)
                            } else {
                                val result = NetworkResult.Error<Setting>(respApi.get("message").toString())
                                successCallback(result)
                            }
                        }?: successCallback(NetworkResult.Error("There was an unknown error"))
                    } catch (exception: java.lang.Exception) {
                        val result = NetworkResult.Error<Setting>(exception.message.toString())
                        successCallback(result)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val result = NetworkResult.Error<Setting>(t.message.toString())
                    successCallback(result)
                }
            })

    }

    fun musicStatusChange(successCallback: (response: NetworkResult<JsonObject>) -> Unit, token:String,music:String) {
        webService.musicStatusChange(token,music)
            .enqueue(object : retrofit2.Callback<JsonObject> {

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

    fun soundEffectStatusChange(successCallback: (response: NetworkResult<JsonObject>) -> Unit, token:String, sound:String) {
        webService.soundEffectStatusChange(token,sound)
            .enqueue(object : retrofit2.Callback<JsonObject> {

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
        private var instance: SettingsRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: SettingsRepository().also { instance = it }
        }
    }

}