package com.yesitlabs.jumballapp.network

import android.util.Log
import androidx.multidex.BuildConfig
import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.GetGroupDetailResp
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.privacyPolicy.PrivacyPolicyResp
import com.yesitlabs.jumballapp.model.SaveScoreResp
import com.yesitlabs.jumballapp.model.ScoreBoardResp
import com.yesitlabs.jumballapp.model.SignupOtpResp
import com.yesitlabs.jumballapp.model.SingUpResp
import com.yesitlabs.jumballapp.viewModel.gameapi.stickerViewModel.cericatureResp
import com.yesitlabs.jumballapp.model.stickerResp
import com.yesitlabs.jumballapp.model.termAndCondion.Term_condition_resp
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class WebServices {

    private var api: ApiInterface

    init {
        val logging = HttpLoggingInterceptor { message -> Log.d("RetrofitLog", message) }
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }else{
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
      //  val logging = HttpLoggingInterceptor()
        val httpClient = OkHttpClient.Builder().apply {
            connectTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            writeTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            readTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            addInterceptor(Interceptor { chain ->
                val builder = chain.request().newBuilder()


                builder.header("Accept", "application/json")

                val response = chain.proceed(builder.build())
               Log.d("testing_token",response.code.toString())

                return@Interceptor chain.proceed(builder.build())
            })

            //logging.level = HttpLoggingInterceptor.Level.BODY
            addNetworkInterceptor(logging)
        }.build()
        httpClient.readTimeoutMillis

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jumball.tgastaging.com/api/").client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiInterface::class.java)

    }

    fun forgotPasswordOtp(email: String): Call<JsonObject> {
        return api.forgotPasswordOtp(email)
    }

    fun resetPassword(email: String, password: String, confirmPassword: String): Call<JsonObject> {
        return api.resetPassword(email, password, confirmPassword)
    }

    fun getProfileData(token: String): Call<JsonObject> {
        return api.getProfileData(token)
    }

    fun sendProfileData(
        token: String,
        name: String,
        countryId: Int,
        skillLevel: String,
        position: String,
        playStyle: String,
        worldCupId: Int
    ): Call<JsonObject> {

        return api.sendProfileData(
            token,
            name,
            countryId,
            skillLevel,
            position,
            playStyle,
            worldCupId
        )
    }

    fun sendProfileDataWithImage(
        token: String, name: RequestBody,
        countryId: RequestBody,
        skillLevel: RequestBody,
        position: RequestBody,
        playStyle: RequestBody,
        worldCupId: RequestBody,
        profileImage: MultipartBody.Part?
    ): Call<JsonObject> {

        return api.sendProfileDataWithImage(
            token, name, countryId, skillLevel, position, playStyle, worldCupId,
            profileImage
        )
    }

    fun profileDelete(token: String): Call<JsonObject> {
        return api.profileDelete(token)
    }

    fun userLogout(token: String): Call<JsonObject> {
        return api.userLogout(token)
    }

    fun setting(token: String): Call<JsonObject> {
        return api.setting(token)
    }

    fun musicStatusChange(token: String, music: String): Call<JsonObject> {
        return api.musicStatusChange(token, music)
    }


    fun soundEffectStatusChange(token: String, sound: String): Call<JsonObject> {
        return api.soundEffectStatusChange(token, sound)
    }

    suspend fun getGuessPlayerList(token: String, defender : String, midFielder : String,
                                   attacker : String, userCaptainId : String, cpuCaptainId : String,
                                   match_no:String): Response<GuessPlayerListResp>? {
        return api.getGuessPlayerList(token,defender ,midFielder, attacker,userCaptainId,cpuCaptainId,
            match_no)
    }

    suspend fun getPrivacyAndPolicy(token: String): Response<PrivacyPolicyResp>? {
        return api.getPrivacyAndPolicy(token)
    }

    suspend fun getTermAndCondition(token: String): Response<Term_condition_resp>? {
        return api.getTermAndCondition(token)
    }

    suspend fun saveScore(token: String, totalGoal: String, totalGoalConsole: String, matchStatus: String, captianId: String, total_defence: String, opponent_guessed: String, my_guesses: String): Response<SaveScoreResp>? {
        return api.saveScore(token ,totalGoal , totalGoalConsole ,matchStatus,captianId,total_defence,opponent_guessed,my_guesses)
    }

    suspend fun worldCupWon(token: String,count:String): Response<SaveScoreResp>? {
        return api.worldCupWon(token , count)
    }

    suspend fun getScoreBoard(token: String): Response<ScoreBoardResp>? {
        return api.getScoreBoard(token)
    }

    suspend fun getSticker(token: String): Response<stickerResp>? {
        return api.getSticker(token)
    }

    suspend fun getCaricature(token: String,match_no:String): Response<cericatureResp>? {
        return api.getCaricature(token,match_no)
    }

    suspend fun getTeam(token: String,is_first:String): Response<GetGroupDetailResp>? {
        return api.getTeam(token,is_first)
    }

    suspend fun login(email: String, pass: String): Response<LoginResp>? {
        return api.userLogin(email,pass)
    }

    suspend fun socialLogin(name: String, email: String, fcmTocken: String): Response<LoginResp>? {
        return api.socialLogin(name,email,fcmTocken)
    }

    suspend  fun userSingUp(name: String, email: String, pass: String): Response<SingUpResp>? {
        return api.userSingUp(name,email,pass)
    }

    suspend fun userSingUpOtp(email: String): Response<SignupOtpResp>? {
        return api.userSingUpOtp(email)
    }


    interface ApiInterface {

        @POST("forget_password")
        @FormUrlEncoded
        fun forgotPasswordOtp(@Field("email") email: String): Call<JsonObject>

        @POST("forget_reset_password")
        @FormUrlEncoded
        fun resetPassword(
            @Field("email") email: String, @Field("password") password: String,
            @Field("conform_password") confirmPassword: String
        ): Call<JsonObject>


        @Headers("Accept: application/json")
        @GET("player_profile_data")
        fun getProfileData(@Header("Authorization") token: String?): Call<JsonObject>

        @Headers("Accept: application/json")
        @POST("profile-update")
        @FormUrlEncoded
        fun sendProfileData(
            @Header("Authorization") token: String?,
            @Field("name") name: String,
            @Field("country_id") countryId: Int,
            @Field("skill_level") skillLevel: String,
            @Field("position") position: String,
            @Field("play_style") playStyle: String,
            @Field("world_cup_id") worldCupId: Int
        ): Call<JsonObject>

        @Headers("Accept: application/json")
        @POST("profile-update")
        @Multipart
        fun sendProfileDataWithImage(
            @Header("Authorization") token: String?,
            @Part("name") name: RequestBody,
            @Part("country_id") countryId: RequestBody,
            @Part("skill_level") skillLevel: RequestBody,
            @Part("position") position: RequestBody,
            @Part("play_style") playStyle: RequestBody,
            @Part("world_cup_id") worldCupId: RequestBody,
            @Part profileImage: MultipartBody.Part?
        ): Call<JsonObject>


        @Headers("Accept: application/json")
        @POST("profile_delete")
        fun profileDelete(@Header("Authorization") token: String?): Call<JsonObject>


        @Headers("Accept: application/json")
        @POST("user_logout")
        fun userLogout(@Header("Authorization") token: String?): Call<JsonObject>

        @Headers("Accept: application/json")
        @GET("setting")
        fun setting(@Header("Authorization") token: String?): Call<JsonObject>


        @Headers("Accept: application/json")
        @POST("music_status_change")
        @FormUrlEncoded
        fun musicStatusChange(
            @Header("Authorization") token: String?,
            @Field("music") music: String
        ): Call<JsonObject>

        @Headers("Accept: application/json")
        @POST("sound_effect_status_change")
        @FormUrlEncoded
        fun soundEffectStatusChange(
            @Header("Authorization") token: String?,
            @Field("sound_effect") music: String
        ): Call<JsonObject>

        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("login")
        suspend fun userLogin(
            @Field("email") email: String,
            @Field("password") password: String
        ): Response<LoginResp>?

        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("social_login")
        suspend fun socialLogin(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("fcm_tocken") fcmToken: String
        ): Response<LoginResp>?


        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("register")
        suspend fun userSingUp(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String
        ): Response<SingUpResp>?

        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("sign_up_otp")
        suspend fun userSingUpOtp(
            @Field("email") email: String,
        ): Response<SignupOtpResp>?

        @Headers("Accept: application/json")
        @GET("guess_player_list_data")
        suspend fun getGuessPlayerList(
            @Header("Authorization") token: String,
            @Query("defender") defender : String,
            @Query("midfilder") midFilder : String,
            @Query("attacker") attacker : String,
            @Query("userCatianId") userCatianId : String,
            @Query("cpuCatianId") cpuCatianId : String,
            @Query("match_no") match_no: String
        ): Response<GuessPlayerListResp>?

        @GET("privacypolicy")
        suspend fun getPrivacyAndPolicy(
            @Header("Authorization") token: String,
        ): Response<PrivacyPolicyResp>?

        @GET("termandcondition")
        suspend fun getTermAndCondition(
            @Header("Authorization") token: String,
        ): Response<Term_condition_resp>?

        @Headers("Accept: application/json")
        @FormUrlEncoded
        @POST("SaveScore")
        suspend fun saveScore(
            @Header("Authorization") token: String,
            @Field("total_goal") totalGoal: String,
            @Field("total_goal_console") totalGoalConsole: String,
            @Field("match_status") matchStatus: String,
            @Field("cpu_captain_id") captianId: String,
            @Field("total_defence") total_defence: String,
            @Field("opponent_guessed") opponent_guessed: String,
            @Field("my_guesses") my_guesses: String,
        ): Response<SaveScoreResp>?

        @FormUrlEncoded
        @Headers("Accept: application/json")
        @POST("WonWorldCup")
        suspend fun worldCupWon(
            @Header("Authorization") token: String,
            @Field("total_won") totalGoal: String
        ): Response<SaveScoreResp>?

        @GET("GetScroreBoard")
        suspend fun getScoreBoard(
            @Header("Authorization") token: String,
        ): Response<ScoreBoardResp>?

        @GET("getUserSticker")
        suspend fun getSticker(
            @Header("Authorization") token: String,
        ): Response<stickerResp>?

//        @GET("getSticker")
        @GET("get_randmally_sticker")
        suspend fun getCaricature(
            @Header("Authorization") token: String,
            @Query("match_no") match_no: String
        ): Response<cericatureResp>?


        @GET("getScoreBord")
        suspend fun getTeam(@Header("Authorization") token: String,
                            @Query("is_first") is_first: String): Response<GetGroupDetailResp>?


    }

}