package com.yesitlabs.jumballapp.repository

import com.yesitlabs.jumballapp.network.NetworkResult


interface MainRepository {

    suspend fun signUpModel(successCallback: (response: NetworkResult<String>) -> Unit, emailOrPhone: String, password: String)

    suspend fun userSingUpOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String)
    suspend fun userSingUp(successCallback: (response: NetworkResult<String>) -> Unit, name: String, email: String, pass: String)
    suspend fun forgotPasswordOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String)

    suspend fun setting(successCallback: (response: NetworkResult<String>) -> Unit)
    suspend fun getProfile(successCallback: (response: NetworkResult<String>) -> Unit)
    suspend fun getTermAndCondition(successCallback: (response: NetworkResult<String>) -> Unit)

    suspend fun getPrivacyAndPolicy(successCallback: (response: NetworkResult<String>) -> Unit)

    suspend fun logOut(successCallback: (response: NetworkResult<String>) -> Unit)

    suspend fun profileDelete(successCallback: (response: NetworkResult<String>) -> Unit)
    suspend fun statisticsRequest(successCallback: (response: NetworkResult<String>) -> Unit)
    suspend fun getSticker(successCallback: (response: NetworkResult<String>) -> Unit)

    suspend fun socialLogin(
        successCallback: (response: NetworkResult<String>) -> Unit,
        name: String,
        email: String,
        fcmToken: String
    )

    suspend fun sendProfileData(
        successCallback: (response: NetworkResult<String>) -> Unit,
        surname: String,
        countryId: String,
        skillLevel: String,
        autoPosition: String,
        autoPlay: String,
        worldCupId: String,
        filepath: String
    )

    suspend fun login(
        successCallback: (response: NetworkResult<String>) -> Unit,
        email: String,
        pass: String
    )

    suspend fun getCaricature(successCallback: (response: NetworkResult<String>) -> Unit, matchNo: String)
    suspend fun saveScoreList(successCallback: (response: NetworkResult<String>) -> Unit, total_goal: String, total_goal_console: String, match_status: String,captianId:String,total_defence: String, opponent_guessed: String, my_guesses: String)
    suspend fun getGuessPlayerList(successCallback: (response: NetworkResult<String>) -> Unit, defender : String, midFielder : String, attacker : String, userCaptainId : String, cpuCaptainId : String,match_no:String)

    suspend fun getTeam(successCallback: (response: NetworkResult<String>) -> Unit, is_first: String)


    suspend fun soundEffectStatusChange(
        successCallback: (response: NetworkResult<String>) -> Unit,
        sound: String
    )

    suspend fun musicStatusChange(
        successCallback: (response: NetworkResult<String>) -> Unit,
        sound: String
    )

    suspend fun resetPassword(
        successCallback: (response: NetworkResult<String>) -> Unit,
        email: String,
        pass: String,
        cnfPass: String
    )


}