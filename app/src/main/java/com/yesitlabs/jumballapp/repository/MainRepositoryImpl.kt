package com.yesitlabs.jumballapp.repository


import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.network.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(private val api: ApiEndPoint) : MainRepository {

    override suspend fun signUpModel(successCallback: (response: NetworkResult<String>) -> Unit, emailOrPhone: String, password: String) {

    }

    override suspend fun userSingUpOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String) {
        try {
            api.userSingUpOtp(email).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun userSingUp(successCallback: (response: NetworkResult<String>) -> Unit, name: String, email: String, pass: String) {
        try {
            api.userSingUp(name,email,pass).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun forgotPasswordOtp(successCallback: (response: NetworkResult<String>) -> Unit, email: String) {
        try {
            api.forgotPasswordOtp(email).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun setting(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.setting().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }
    override suspend fun getProfile(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.getProfileData().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }
    override suspend fun getTermAndCondition(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.getTermAndCondition().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun getPrivacyAndPolicy(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.getPrivacyAndPolicy().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun logOut(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.userLogout().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun profileDelete(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.profileDelete().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }


    }

    override suspend fun statisticsRequest(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.getScoreBoard().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun getSticker(successCallback: (response: NetworkResult<String>) -> Unit) {
        try {
            api.getSticker().apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }


    override suspend fun socialLogin(
        successCallback: (response: NetworkResult<String>) -> Unit,
        name: String,
        email: String,
        fcmToken: String
    ) {
        try {
            api.socialLogin(name,email,fcmToken).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun sendProfileData(
        successCallback: (response: NetworkResult<String>) -> Unit,
        surname: String,
        countryId: String,
        skillLevel: String,
        autoPosition: String,
        autoPlay: String,
        worldCupId: String,
        filepath: String) {
        try {
            var part: MultipartBody.Part?=null
            if (!filepath.equals("",true)){
                val file = File(filepath)
                val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                part = MultipartBody.Part.createFormData("image_profile", file.path, requestBody)
            }
            val reqName = surname.toRequestBody("text/plain".toMediaTypeOrNull())
            val reqCountryId = countryId.toRequestBody("text/plain".toMediaTypeOrNull())
            val reqSkillLevel = skillLevel.toRequestBody("text/plain".toMediaTypeOrNull())
            val reqPosition = autoPosition.toRequestBody("text/plain".toMediaTypeOrNull())
            val reqPlayStyle = autoPlay.toRequestBody("text/plain".toMediaTypeOrNull())
            val reqWorldCupId = worldCupId.toRequestBody("text/plain".toMediaTypeOrNull())

            api.sendProfileDataWithImage(reqName,reqCountryId,reqSkillLevel,reqPosition,reqPlayStyle,reqWorldCupId,part).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun login(
        successCallback: (response: NetworkResult<String>) -> Unit,
        email: String,
        pass: String
    ) {
        try {
            api.userLogin(email,pass).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }


    }

    override suspend fun getCaricature(
        successCallback: (response: NetworkResult<String>) -> Unit, matchNo: String) {
        try {
            api.getCaricature(matchNo).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }


    }

    override suspend fun getGuessPlayerList(
        successCallback: (response: NetworkResult<String>) -> Unit,
        defender: String,
        midFielder: String,
        attacker: String,
        userCaptainId: String,
        cpuCaptainId: String,
        match_no: String
    ) {
        try {
            api.getGuessPlayerList(defender ,midFielder, attacker,userCaptainId,cpuCaptainId,match_no).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun getTeam(
        successCallback: (response: NetworkResult<String>) -> Unit, is_first: String) {
        try {
            api.getTeam(is_first).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }


    }

    override suspend fun soundEffectStatusChange(
        successCallback: (response: NetworkResult<String>) -> Unit,
        sound: String
    ) {
        try {
            api.soundEffectStatusChange(sound).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun musicStatusChange(
        successCallback: (response: NetworkResult<String>) -> Unit,
        sound: String
    ) {
        try {
            api.musicStatusChange(sound).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }

    override suspend fun resetPassword(
        successCallback: (response: NetworkResult<String>) -> Unit,
        email: String,
        pass: String,
        cnfPass: String
    ) {
        try {
            api.resetPassword(email,pass,cnfPass).apply {
                if (isSuccessful) {
                    body()?.let {
                        successCallback(NetworkResult.Success(it.toString()))
                    } ?: successCallback(NetworkResult.Error(ErrorMessage.apiError))
                } else {
                    successCallback(NetworkResult.Error(ErrorMessage.serverError))
                }
            }
        } catch (e: Exception) {
            successCallback(NetworkResult.Error(ErrorMessage.serverError))
        }
    }


}