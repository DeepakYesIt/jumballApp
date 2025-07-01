package com.yesitlabs.jumballapp.network.repository

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
import com.yesitlabs.jumballapp.network.WebServices
import retrofit2.Response

class UserRepository(private var webService: WebServices = WebServices()) {


    suspend fun getGuessPlayerList(token: String, defender : String, midFielder : String, attacker : String, userCaptainId : String, cpuCaptainId : String,match_no:String): Response<GuessPlayerListResp>? {
        return webService.getGuessPlayerList(token,defender ,midFielder, attacker,userCaptainId,cpuCaptainId, match_no)
    }

    suspend fun getPrivacyAndPolicy(token: String): Response<PrivacyPolicyResp>? {
        return webService.getPrivacyAndPolicy(token)
    }

    suspend fun getTermAndCondition(token: String): Response<Term_condition_resp>? {
        return webService.getTermAndCondition(token)
    }

    suspend fun saveScore(token: String, totalGoal: String, totalGoalConsole: String, matchStatus: String, captianId: String,total_defence: String, opponent_guessed: String, my_guesses: String): Response<SaveScoreResp>? {
        return webService.saveScore(token ,totalGoal , totalGoalConsole ,matchStatus,captianId,total_defence,opponent_guessed,my_guesses)
    }
    suspend fun worldCupWon(token: String,count:String): Response<SaveScoreResp>? {
        return webService.worldCupWon(token,count )
    }

    suspend fun getScoreBoard(token: String): Response<ScoreBoardResp>? {
        return webService.getScoreBoard(token )
    }

    suspend fun getSticker(token: String): Response<stickerResp>? {
        return webService.getSticker(token)
    }

    suspend fun getCaricature(token: String,match_no:String): Response<cericatureResp>? {
        return webService.getCaricature(token,match_no)
    }

    suspend fun getTeam(token: String,is_first:String): Response<GetGroupDetailResp>? {
        return webService.getTeam(token,is_first)
    }

    suspend fun login(email: String, pass: String): Response<LoginResp>? {
        return webService.login(email,pass)
    }

   suspend fun socialLogin(name: String, email: String, fcmToken: String): Response<LoginResp>? {
       return webService.socialLogin(name,email,fcmToken)
    }

    suspend fun userSingUp(name: String, email: String, pass: String): Response<SingUpResp>? {
        return webService.userSingUp(name,email,pass)
    }

   suspend fun userSingUpOtp(email: String): Response<SignupOtpResp>? {
       return webService.userSingUpOtp(email)   }


}