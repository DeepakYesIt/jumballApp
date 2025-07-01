package com.yesitlabs.jumballapp.fragment.manfragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.network.viewModel.SettingViewModel
import com.yesitlabs.jumballapp.activity.AuthActivity
import com.yesitlabs.jumballapp.MusicService
import com.yesitlabs.jumballapp.BaseApplication
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentSettingsBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.viewmodeljumball.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : Fragment(),View.OnClickListener{

    lateinit var base: BaseApplication
    private lateinit var settingViewModel: SettingViewModel

    lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        base= BaseApplication()
        super.onViewCreated(view, savedInstanceState)
        settingViewModel = ViewModelProvider(this)[SettingViewModel::class.java]
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        sessionManager = SessionManager(requireContext())


        setting()

        binding.btnBack.setOnClickListener(this)
        binding.layMyprofile.setOnClickListener(this)

        binding.eventMusic.setOnClickListener {
            val music = if (binding.eventMusic.isChecked) "1" else "0"
            Log.e("Music", music)
            val intent2 = Intent("com.example.andy.startstopservice")
            if (music.equals("1",true)){
                intent2.putExtra("finished", false)
                startMusic()
            }else{
                intent2.putExtra("finished", true)
                stopMusic()
            }
            context?.let { LocalBroadcastManager.getInstance(it).sendBroadcast(intent2) }
            if (sessionManager.getCheckLogin() == "yes") {
                if(sessionManager.isNetworkAvailable()){
                    sessionManager.setMusic(music.toInt())
                    musicStatusChange(music)
                }else{
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }
            }else{
                sessionManager.setMusic(music.toInt())
            }
        }

        binding.soundEffect.setOnClickListener {
            val sound = if (binding.soundEffect.isChecked) "1" else "0"
            if (sessionManager.getCheckLogin().equals("yes", true)) {
                if (sessionManager.isNetworkAvailable()) {
                    soundEffectStatusChange(sound)
                } else {
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }
            } else {
                sessionManager.setSoundEffect(sound.toInt())
            }
        }

        binding.laySound.setOnClickListener(this)
        binding.layTac.setOnClickListener(this)
        binding.layPp.setOnClickListener(this)
        binding.layDelete.setOnClickListener(this)
        binding.layLogout.setOnClickListener(this)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    // This function is used for start the music
    private fun startMusic() {
        val intent = Intent(requireActivity(), MusicService::class.java)
        intent.putExtra("value",0)
        requireActivity().startService(intent)
    }

    // This function is used for stop the music
    private fun stopMusic() {
        requireActivity().stopService(Intent(requireActivity(), MusicService::class.java))
    }

    override fun onClick(item: View?) {
        sessionManager.playClickSound()
        when(item!!.id){
            R.id.btn_back->{
                findNavController().navigateUp()
            }
            R.id.lay_myprofile->{
                findNavController().navigate(R.id.profileFragment)
            }

            R.id.lay_sound->{

            }
            R.id.lay_tac->{
                findNavController().navigate(R.id.termsAndConditionFragment)
            }
            R.id.lay_pp->{
                findNavController().navigate(R.id.privacyPolicyFragment)
            }
            R.id.lay_delete->{
                deleteAccountAlert()
            }
            R.id.lay_logout->{
                logoutAlertBox()
            }

        }
    }

    // This function is used for display alert dialog of logout account
    private fun logoutAlertBox() {
        val dialog= Dialog(requireContext(),R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_logout)
        dialog.window!!.setDimAmount(0f)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val btnCross:LinearLayout=dialog.findViewById(R.id.btn_cross)
        val btnYes:LinearLayout=dialog.findViewById(R.id.btn_yes)
        val btnNo:LinearLayout=dialog.findViewById(R.id.btn_no)
        btnCross.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            dialog.dismiss()
            if (sessionManager.getCheckLogin().equals("yes")){
                if(sessionManager.isNetworkAvailable()){
                    userLogout()
                }else{
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }
            }else{
                moveToScreen("1")
            }

        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    // This function is used for display alert dialog of delete account
    private fun deleteAccountAlert() {
        val dialog= Dialog(requireContext(),R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_delete)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val btnCross:LinearLayout=dialog.findViewById(R.id.btn_cross)
        val btnYes:LinearLayout=dialog.findViewById(R.id.btn_yes)
        val btnNo:LinearLayout=dialog.findViewById(R.id.btn_no)
        btnCross.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            if (sessionManager.getCheckLogin() == "yes"){
                if(sessionManager.isNetworkAvailable()){
                    deleteAccount()
                }else{
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }

            }else{
                moveToScreen("1")
            }

            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // This function is used for music status in server
    private fun musicStatusChange(music:String) {
//        binding.progressSetting.visibility =View.VISIBLE
//
//        val token: String = "Bearer "+ sessionManager.fetchAuthToken()
//
//        settingViewModel.musicStatusChange ({
//            when (it) {
//                is NetworkResult.Success -> {
//                    binding.progressSetting.visibility = View.GONE
//                }
//
//                is NetworkResult.Error -> {
//                    binding.progressSetting.visibility = View.GONE
//                    Toast.makeText(context,it.message.toString(), Toast.LENGTH_LONG).show()
//                }
//                else -> {
//                    binding.progressSetting.visibility = View.GONE
//                }
//            }
//        },token,music)


        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.musicStatusChange({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, LoginResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    sessionManager.setMusic(music.toInt())
                                }catch (e:Exception){
                                    Log.d("signup","message:---"+e.message)
                                }
                            } else {
                                sessionManager.alertError(model.message)
                            }
                        }catch (e:Exception){
                            Log.d("signup","message:---"+e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            },music)
        }


    }

    // This function is used for sound effect status in server
    private fun soundEffectStatusChange(sound:String) {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.soundEffectStatusChange({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, LoginResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    sessionManager.setSoundEffect(sound.toInt())
                                }catch (e:Exception){
                                    Log.d("signup","message:---"+e.message)
                                }
                            } else {
                                sessionManager.alertError(model.message)
                            }
                        }catch (e:Exception){
                            Log.d("signup","message:---"+e.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            },sound)
        }


    }

    // This function is used for delete the account of the user in server
    private fun deleteAccount() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.profileDelete {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, LoginResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    moveToScreen("2")
                                } catch (e: Exception) {
                                    Log.d("signup", "message:---" + e.message)
                                }
                            } else {
                                sessionManager.alertError(signUpModel.message)
                            }
                        } catch (e: Exception) {
                            Log.d("signup", "message:---" + e.message)
                        }
                    }

                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }
        }

    }

    // This function is used for logout the user in server
    private fun userLogout() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.logOut {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val signUpModel = gson.fromJson(it.data, LoginResp::class.java)
                            if (signUpModel.code == 200 && signUpModel.success) {
                                try {
                                    moveToScreen("2")
                                } catch (e: Exception) {
                                    Log.d("signup", "message:---" + e.message)
                                }
                            } else {
                                sessionManager.alertError(signUpModel.message)
                            }
                        } catch (e: Exception) {
                            Log.d("signup", "message:---" + e.message)
                        }
                    }

                    is NetworkResult.Error -> {
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }
        }
    }


    private fun moveToScreen(type: String) {
        sessionManager.deleteWholeData()
        if (type.equals("2",true)){
            sessionManager.setSoundEffect(1)
        }
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }




    // This function is used for get setting data from session manager
    private fun setting() {

        if (sessionManager.getMusic()  ==1){
            binding.eventMusic.isChecked = true
            Log.e("Music",sessionManager.getMusic().toString())
        }else{
            binding.eventMusic.isChecked = false
        }

        if (sessionManager.getSoundEffect() ==1){
            binding.soundEffect.isChecked = true
            Log.e("sound_effect",sessionManager.getSoundEffect().toString())
        }else{
            binding.soundEffect.isChecked = false
        }


    }


}