package com.yesitlabs.jumballapp.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import com.yesitlabs.jumballapp.SessionManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(var context: Context) : Interceptor {

    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder: Request.Builder = chain.request().newBuilder()
        val token = getBearerToken()
        Log.d("token :- ", "*******$token")
        if (token.isNotEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            requestBuilder.addHeader("Accept", "application/json") // Removed extra space after "Accept"
        }
        return chain.proceed(requestBuilder.build())
    }
    private fun getBearerToken(): String {
        val sessionManagement = SessionManager(context)
        val token: String = sessionManagement.fetchAuthToken()!!
        return token
    }

}