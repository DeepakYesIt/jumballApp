package com.jumball.app.repository

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiEndPoint {

    @POST(ApiNameEndPoint.forgetPassword)
    @FormUrlEncoded
    suspend fun forgotPasswordOtp(@Field("email") email: String): Response<JsonObject>

    @POST(ApiNameEndPoint.forgetResetPassword)
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("email") email: String, @Field("password") password: String,
        @Field("conform_password") confirmPassword: String
    ): Response<JsonObject>

    @GET(ApiNameEndPoint.playerProfileData)
    suspend fun getProfileData(): Response<JsonObject>

    @POST(ApiNameEndPoint.profileUpdate)
    @Multipart
    suspend fun sendProfileDataWithImage(
        @Part("name") name: RequestBody,
        @Part("country_id") countryId: RequestBody,
        @Part("skill_level") skillLevel: RequestBody,
        @Part("position") position: RequestBody,
        @Part("play_style") playStyle: RequestBody,
        @Part("world_cup_id") worldCupId: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): Response<JsonObject>


    @POST(ApiNameEndPoint.profileDelete)
    suspend fun profileDelete(): Response<JsonObject>


    @POST(ApiNameEndPoint.userLogout)
    suspend fun userLogout(): Response<JsonObject>


    @GET(ApiNameEndPoint.setting)
    suspend fun setting(): Response<JsonObject>


    @POST(ApiNameEndPoint.musicStatusChange)
    @FormUrlEncoded
    suspend fun musicStatusChange(
        @Field("music") music: String
    ):Response<JsonObject>

    @POST(ApiNameEndPoint.soundEffectStatusChange)
    @FormUrlEncoded
    suspend fun soundEffectStatusChange(
        @Field("sound_effect") music: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST(ApiNameEndPoint.login)
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<JsonObject>


    @FormUrlEncoded
    @POST(ApiNameEndPoint.socialLogin)
    suspend fun socialLogin(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("fcm_tocken") fcmToken: String
    ): Response<JsonObject>



    @FormUrlEncoded
    @POST(ApiNameEndPoint.register)
    suspend fun userSingUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<JsonObject>


    @FormUrlEncoded
    @POST(ApiNameEndPoint.signUpOtp)
    suspend fun userSingUpOtp(
        @Field("email") email: String,
    ): Response<JsonObject>

    @GET(ApiNameEndPoint.guessPlayerListData)
    suspend fun getGuessPlayerList(
        @Query("defender") defender : String,
        @Query("midfilder") midFilder : String,
        @Query("attacker") attacker : String,
        @Query("userCatianId") userCatianId : String,
        @Query("cpuCatianId") cpuCatianId : String,
        @Query("match_no") match_no: String
    ): Response<JsonObject>

    @GET(ApiNameEndPoint.privacypolicy)
    suspend fun getPrivacyAndPolicy(): Response<JsonObject>

    @GET(ApiNameEndPoint.termandcondition)
    suspend fun getTermAndCondition(): Response<JsonObject>


    @FormUrlEncoded
    @POST(ApiNameEndPoint.SaveScore)
    suspend fun saveScore(
        @Field("total_goal") totalGoal: String,
        @Field("total_goal_console") totalGoalConsole: String,
        @Field("match_status") matchStatus: String,
        @Field("cpu_captain_id") captianId: String,
        @Field("total_defence") total_defence: String,
        @Field("opponent_guessed") opponent_guessed: String,
        @Field("my_guesses") my_guesses: String,
        @Field("match_no") matchNumber: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST(ApiNameEndPoint.WonWorldCup)
    suspend fun worldCupWon(
        @Field("total_won") totalGoal: String
    ):Response<JsonObject>

    @GET(ApiNameEndPoint.GetScroreBoard)
    suspend fun getScoreBoard(
    ): Response<JsonObject>

    @GET(ApiNameEndPoint.getUserSticker)
    suspend fun getSticker(): Response<JsonObject>

    //        @GET("getSticker")
    @GET(ApiNameEndPoint.getRandmallySticker)
    suspend fun getCaricature(@Query("match_no") match_no: String): Response<JsonObject>


    @GET(ApiNameEndPoint.getScoreBord)
    suspend fun getTeam(@Query("is_first") is_first: String): Response<JsonObject>

}