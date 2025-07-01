package com.yesitlabs.jumballapp.network.repository

import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.network.WebServices
import retrofit2.Call
import retrofit2.Response

class ForgotPasswordRepository(private val webService: WebServices = WebServices())  {

    fun forgotPasswordOtp(successCallback: (response: NetworkResult<Pair<String,Int>>) -> Unit, email:String){
        webService.forgotPasswordOtp(email).enqueue(object:retrofit2.Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    response.body()?.let { resp ->
                        val success= resp.get("success")
                        if (success.asBoolean) {
                            val data: JsonObject = resp.get("data")as JsonObject
                            val email:String=  data.get("email").asString
                            val otp:Int = data.get("otp").asInt
                            val result = NetworkResult.Success(Pair(email,otp))
                            successCallback(result)

                        } else {

                            val result = NetworkResult.Error<Pair<String,Int>>(resp.get("message").toString())
                            successCallback(result)
                        }
                    }?:successCallback(NetworkResult.Error("There was an unknown error. Check your connection and try again."))
                }
                catch (exception: java.lang.Exception) {
                    val result = NetworkResult.Error<Pair<String,Int>>(exception.message.toString())
                    successCallback(result)
                }

            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                val result = NetworkResult.Error<Pair<String,Int>>(t.message.toString())
                successCallback(result)
            }

        })
    }

    fun resetPassword(successCallback: (response: NetworkResult<Int>) -> Unit, email:String,password:String,
    confirmPassword:String) {

        webService.resetPassword(email, password, confirmPassword)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        response.body()?.let { resp ->
                            val success= resp.get("success")
                            if (success.asBoolean) {

                                val result = NetworkResult.Success(resp.get("code").asInt)
                                successCallback(result)

                            } else {

                                val result = NetworkResult.Error<Int>(resp.get("message").toString())
                                successCallback(result)
                            }
                        }?:successCallback(NetworkResult.Error("There was an unknown error. Check your connection and try again."))
                    }
                    catch (exception: java.lang.Exception) {
                        val result = NetworkResult.Error<Int>(exception.message.toString())
                        successCallback(result)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val result = NetworkResult.Error<Int>(t.message.toString())
                    successCallback(result)
                }
            })

        }

    companion object {
        @Volatile
        private var instance: ForgotPasswordRepository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ForgotPasswordRepository().also { instance = it }
        }
    }

}