package com.yesitlabs.jumballapp.di

import android.content.Context
import android.util.Log
import androidx.multidex.BuildConfig
import com.yesitlabs.jumballapp.repository.ApiEndPoint
import com.yesitlabs.jumballapp.repository.MainRepository
import com.yesitlabs.jumballapp.repository.MainRepositoryImpl
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun jumBallApiInterFace(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient): ApiEndPoint {
        return  retrofit
            .client(okHttpClient)
            .build()
            .create(ApiEndPoint::class.java)
    }

    @Provides
    @Singleton
    fun jumBallRepository(api:ApiEndPoint): MainRepository {
        return MainRepositoryImpl(api)
    }


    @Singleton
    @Provides
    fun provideAuthAuthenticator(@ApplicationContext context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Singleton
    @Provides
    fun jumBallOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d("RetrofitLog", message) }


        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }


        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain -> // Corrected lambda syntax
                val response = chain.proceed(chain.request())
                Log.d("@@@@@@@@", "response: ${response.code}")
                if (response.code == 401) {
                    SessionEventBus.emitSessionExpired()
//                    // Return an empty 204-like response
//                    return@addInterceptor Response.Builder()
//                        .code(200)
//                        .request(chain.request())
//                        .message("No Content")
//                        .protocol(Protocol.HTTP_1_1)
//                        .body(ResponseBody.create(null, "")) // requires `MediaType?` as first param
//                        .build()
                }
                response // Return the response
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun customerRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://jumBall.tgastaging.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
}
