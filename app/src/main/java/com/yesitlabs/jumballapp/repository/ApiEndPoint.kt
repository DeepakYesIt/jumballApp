package com.yesitlabs.jumballapp.repository

import com.google.gson.JsonObject
import com.yesitlabs.jumballapp.model.GetGroupDetailResp
import com.yesitlabs.jumballapp.model.GuessPlayerListResp
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.SaveScoreResp
import com.yesitlabs.jumballapp.model.ScoreBoardResp
import com.yesitlabs.jumballapp.model.SignupOtpResp
import com.yesitlabs.jumballapp.model.SingUpResp
import com.yesitlabs.jumballapp.model.privacyPolicy.PrivacyPolicyResp
import com.yesitlabs.jumballapp.model.stickerResp
import com.yesitlabs.jumballapp.model.termAndCondion.Term_condition_resp
import com.yesitlabs.jumballapp.viewModel.gameapi.stickerViewModel.cericatureResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiEndPoint {

    @POST("forget_password")
    @FormUrlEncoded
    suspend fun forgotPasswordOtp(@Field("email") email: String): Response<JsonObject>

    @POST("forget_reset_password")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("email") email: String, @Field("password") password: String,
        @Field("conform_password") confirmPassword: String
    ): Response<JsonObject>


    @GET("player_profile_data")
    suspend fun getProfileData(): Response<JsonObject>


    @POST("profile-update")
    @FormUrlEncoded
    suspend fun sendProfileData(
        @Field("name") name: String,
        @Field("country_id") countryId: String,
        @Field("skill_level") skillLevel: String,
        @Field("position") position: String,
        @Field("play_style") playStyle: String,
        @Field("world_cup_id") worldCupId: String
    ): Response<JsonObject>

    @POST("profile-update")
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


    @POST("profile_delete")
    suspend fun profileDelete(): Response<JsonObject>


    @POST("user_logout")
    suspend fun userLogout(): Response<JsonObject>


    @GET(ApiNameEndPoint.setting)
    suspend fun setting(): Response<JsonObject>


    @POST("music_status_change")
    @FormUrlEncoded
    suspend fun musicStatusChange(
        @Field("music") music: String
    ):Response<JsonObject>

    @POST("sound_effect_status_change")
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
    @POST("register")
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

    @GET("guess_player_list_data")
    suspend fun getGuessPlayerList(
        @Query("defender") defender : String,
        @Query("midfilder") midFilder : String,
        @Query("attacker") attacker : String,
        @Query("userCatianId") userCatianId : String,
        @Query("cpuCatianId") cpuCatianId : String,
        @Query("match_no") match_no: String
    ): Response<JsonObject>

    @GET("privacypolicy")
    suspend fun getPrivacyAndPolicy(): Response<JsonObject>

    @GET("termandcondition")
    suspend fun getTermAndCondition(): Response<JsonObject>

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
    ): Response<JsonObject>

    @GET("getUserSticker")
    suspend fun getSticker(): Response<JsonObject>

    //        @GET("getSticker")
    @GET("get_randmally_sticker")
    suspend fun getCaricature(@Query("match_no") match_no: String): Response<JsonObject>


    @GET("getScoreBord")
    suspend fun getTeam(@Query("is_first") is_first: String): Response<JsonObject>

}