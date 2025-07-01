package com.yesitlabs.jumballapp.fragment.manfragment


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.yesitlabs.jumballapp.AppConstant.Companion.MEDIA_URL
import com.yesitlabs.jumballapp.MediaPath.getPath
import com.yesitlabs.jumballapp.R

import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentProfileBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.CountriesModel
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.model.WorldCupModel
import com.yesitlabs.jumballapp.viewmodeljumball.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var filepath = ""
    private lateinit var viewModel: ProfileViewModel
    lateinit var sessionManager: SessionManager
    var imageLoad = false
    private lateinit var binding: FragmentProfileBinding
    private var cupmodelList:MutableList<WorldCupModel> = mutableListOf()
    private var countryList:MutableList<CountriesModel> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }


    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding.btnBack.setOnClickListener(this)
        binding.btnEdit.setOnClickListener(this)
        binding.editImg.setOnClickListener(this)
        binding.uplaodImg.setOnClickListener(this)

        isCheck(true)

        if (sessionManager.getCheckLogin().equals("yes",true)) {
            getProfileData()
        }

    }

    // This function is used for check profile editable or non editable
    @SuppressLint("SetTextI18n")
    private fun isCheck(check: Boolean) {
        if (check) {
            binding.editImg.visibility = View.GONE
            binding.tvHeaderTitle.text = " M Y   P R O F I L E"
            binding.edProfileName.isEnabled = false
            binding.spinnerCountry.isEnabled = false
            binding.spinnerSkillLevel.isEnabled = false
            binding.spinnerPosition.isEnabled = false
            binding.spinnerStyleOfPlay.isEnabled = false
            binding.spinnerWorldCupSmall.isEnabled = false
            binding.spinnerCountry.endIconDrawable = null
            binding.spinnerSkillLevel.endIconDrawable = null
            binding.spinnerPosition.endIconDrawable = null
            binding.spinnerStyleOfPlay.endIconDrawable = null
            binding.spinnerWorldCupSmall.endIconDrawable = null
            binding.editImg.visibility = View.GONE
            binding.uplaodImg.visibility = View.GONE
        } else {
            binding.editImg.visibility = View.VISIBLE
            binding.tvHeaderTitle.text = " E D I T   P R O F I L E"
            binding.edProfileName.isEnabled = true
            binding.spinnerCountry.isEnabled = true
            binding.spinnerSkillLevel.isEnabled = true
            binding.spinnerPosition.isEnabled = true
            binding.spinnerStyleOfPlay.isEnabled = true
            binding.spinnerWorldCupSmall.isEnabled = true
            binding.spinnerCountry.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.bxs_down_arrow)
            binding.spinnerSkillLevel.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.bxs_down_arrow)
            binding.spinnerPosition.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.bxs_down_arrow)
            binding.spinnerStyleOfPlay.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.bxs_down_arrow)
            binding.spinnerWorldCupSmall.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.bxs_down_arrow)

            if (imageLoad){
                binding.editImg.visibility = View.VISIBLE
                binding.uplaodImg.visibility = View.GONE
            }else{
                binding.editImg.visibility = View.GONE
                binding.uplaodImg.visibility = View.VISIBLE
            }
        }
    }

    // This function is used for get profile from server
    @SuppressLint("SetTextI18n")
    private fun getProfileData() {
        if (sessionManager.isNetworkAvailable()) {
            sessionManager.showMe(requireContext())
            lifecycleScope.launch {
                viewModel.getProfile {
                    sessionManager.dismissMe()
                    when (it) {
                        is NetworkResult.Success -> {
                            try {
                                val gson = Gson()
                                val model = gson.fromJson(it.data, PlayerProfileModel::class.java)
                                if (model.code == 200 && model.success) {
                                    try {
                                        model.data?.let { data->

                                            data.user.name?.let { name->
                                                binding.edProfileName.setText(name)
                                            }

                                            data.user.country?.let { name->
                                                binding.autoCountry.setText(name.name)
                                            }

                                            data.user.skill_level?.let { name->
                                                binding.autoSkillLevel.setText(name)
                                            }

                                            data.user.profile_images?.let { name->
                                                Glide.with(requireContext())
                                                    .load("$MEDIA_URL$name")
                                                    .apply(RequestOptions().error(R.drawable.user_pic))
                                                    .placeholder(R.drawable.user_pic)
                                                    .listener(object : RequestListener<Drawable> {
                                                        override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
                                                            imageLoad = false
                                                            return false
                                                        }
                                                        override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                                                            imageLoad = true
                                                            return false
                                                        }
                                                    })
                                                    .into(binding.ivProfile)
                                            }

                                            data.user.position?.let { name->
                                                binding.autoPosition.setText(name)
                                            }
                                            data.user.play_style?.let { name->
                                                binding.autoStyleOfPlay.setText(name)
                                            }
                                            data.user.world_cup?.let { name->
                                                binding.autoWorldCupSmall.setText(name.year+"-"+name.country.name)
                                            }

                                            data.skillLevel?.let { list->
                                                if (list.size>0){
                                                    val skillLevels = list.map { it.name }
                                                    val skillLevelAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, skillLevels)
                                                    binding.autoSkillLevel.setAdapter(skillLevelAdapter)
                                                }
                                            }

                                            data.countries?.let { list->
                                                if (list.size>0){
                                                    countryList.addAll(list)
                                                }
                                            }

                                            if (countryList.size>0){
                                                val countryData = countryList.map { it.name }
                                                val countryDataAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, countryData)
                                                binding.autoCountry.setAdapter(countryDataAdapter)
                                            }

                                            data.position?.let { list->
                                                if (list.size>0){
                                                    val postionData = list.map { it.name }
                                                    val postionAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, postionData)
                                                    binding.autoPosition.setAdapter(postionAdapter)
                                                }
                                            }

                                            data.stylePlay?.let { list->
                                                if (list.size>0){
                                                    val styleOfPlayData = list.map { it.name }
                                                    val styleOfPlayDataAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, styleOfPlayData)
                                                    binding.autoStyleOfPlay.setAdapter(styleOfPlayDataAdapter)
                                                }
                                            }

                                            cupmodelList.clear()
                                            data.worldCup?.let { list->
                                                if (list.size>0){
                                                    cupmodelList.addAll(list)
                                                }
                                            }

                                            if (cupmodelList.size>0){
                                                val worldCupData = cupmodelList.map { it.year+"-"+it.country.name }
                                                val worldCupDataAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, worldCupData)
                                                binding.autoWorldCupSmall.setAdapter(worldCupDataAdapter)
                                            }

                                        }
                                    } catch (e: Exception) {
                                        Log.d("signup", "message:---" + e.message)
                                    }
                                } else {
                                    sessionManager.alertError(model.message)
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
        } else {
            sessionManager.alertError(ErrorMessage.netWorkError)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(item: View?) {
        sessionManager.playClickSound()
        when (item!!.id) {
            R.id.btn_back -> {
                findNavController().navigateUp()
            }

            R.id.edit_img -> {
                uploadImage()
            }

            R.id.uplaod_img -> {
                uploadImage()
            }

            R.id.btn_edit -> {
                if (sessionManager.getCheckLogin().equals("yes",true)) {
                    if (binding.tvEdit.text.toString().equals("Edit",true)) {
                        binding.tvEdit.text = "Update"
                        isCheck(false)
                    } else {
                        if (isValidation()) {
                            val surname: String = binding.edProfileName.text.toString()
                            val countryId = countryList.firstOrNull { it.name == binding.autoCountry.text.toString() }?.id.toString()
                            val skillLevel = binding.autoSkillLevel.text.toString()
                            val autoPosition = binding.autoPosition.text.toString()
                            val autoPlay = binding.autoStyleOfPlay.text.toString()
                            val worldCupId = cupmodelList.firstOrNull { binding.autoWorldCupSmall.text.toString().contains(it.year, ignoreCase = true) }?.id.toString()
                            Log.e("SurName",surname )
                            Log.e("countryId",countryId)
                            Log.e("skillLevel",skillLevel )
                            Log.e("autoPosition",autoPosition )
                            Log.e("worldCupId",worldCupId)
                            sendProfileDataWithImage(surname, countryId, skillLevel, autoPosition, autoPlay, worldCupId)
                        }
                    }
                } else {
                    sessionManager.alertError("Login First")
                }
            }
        }
    }

    // This function is used for update profile in server
    @SuppressLint("SetTextI18n")
    private fun sendProfileDataWithImage(surname: String, countryId: String?, skillLevel: String, autoPosition: String, autoPlay: String, worldCupId: String) {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.sendProfileData({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, LoginResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    binding.editImg.visibility = View.GONE
                                    binding.uplaodImg.visibility = View.GONE
                                    binding.tvEdit.text = "Edit"
                                    isCheck(true)
                                    Toast.makeText(context, model.message, Toast.LENGTH_LONG).show()
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
            }, surname,countryId.toString(),skillLevel,autoPosition,autoPlay,worldCupId,filepath)
        }

    }

    // This function is used for check all validation of all filed
    private fun isValidation(): Boolean {
        if (binding.edProfileName.text.toString().isEmpty()) {
            Toast.makeText(activity,ErrorMessage.nameError, Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.autoCountry.text.toString().equals("COUNTRY",true)) {
            Toast.makeText(activity,ErrorMessage.countryError, Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.autoSkillLevel.text.toString().equals("SKILL LEVEL",true)) {
            Toast.makeText(activity,ErrorMessage.skillLevelError, Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.autoPosition.text.toString().equals("POSITION",true)) {
            Toast.makeText(activity,ErrorMessage.postionError, Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.autoStyleOfPlay.text.toString().equals("STYLE OF PLAY",true)) {
            Toast.makeText(activity,ErrorMessage.styleOfPlayError, Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.autoWorldCupSmall.text.toString().equals("World cup",true)) {
            Toast.makeText(activity, ErrorMessage.worldCupError, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // This function is used for upload image from gallery
    private fun uploadImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data!!
            filepath = getPath(requireContext(), fileUri)!!
            Glide.with(requireContext())
                .load(filepath)
                .into(binding.ivProfile)
        }
    }
}