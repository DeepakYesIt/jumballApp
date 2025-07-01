package com.yesitlabs.jumballapp.fragment.authfragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.adapter.WorldCupAdapter
import com.yesitlabs.jumballapp.databinding.FragmentPlayerProfileBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.CountriesModel
import com.yesitlabs.jumballapp.model.LoginResp
import com.yesitlabs.jumballapp.model.PlayerProfileModel
import com.yesitlabs.jumballapp.model.WorldCupModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PlayerProfileFragment : Fragment(),View.OnClickListener {


    private var isCheck:Boolean=true
    var adapter: WorldCupAdapter? = null
    lateinit var sessionManager: SessionManager
    var worldCupId = 0
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentPlayerProfileBinding
    private var cupmodelList:MutableList<WorldCupModel> = mutableListOf()
    private var countryList:MutableList<CountriesModel> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener(this)

        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(0,1)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                findNavController().navigate(R.id.signinFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        sessionManager.getName()?.let {
            binding.edName.setText(it)
        }

        loadApi()

        binding.dropWorldCup.setOnClickListener(this)
        binding.btnViewpager.setOnClickListener(this)

        binding.viewpagerData.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (cupmodelList.size>0){
                    worldCupId = cupmodelList[position].id
                    Log.e("Selected_Page", position.toString()+worldCupId)
                }

            }
        })
    }

    private fun loadApi(){
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
                                                binding.edName.setText(name)
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
                                                adapter = WorldCupAdapter(requireContext(),cupmodelList)
                                                binding.viewpagerData.adapter = adapter
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

    override fun onClick(item: View?) {
        when(item!!.id){
            R.id.btn_next->{
                if(sessionManager.isNetworkAvailable()){
                    if (isValidation()){
                        val surname = binding.edName.text.toString()
                        val countryId = countryList.firstOrNull { it.name == binding.autoCountry.text.toString() }?.id
                        val skillLevel = binding.autoSkillLevel.text.toString()
                        val autoPosition = binding.autoPosition.text.toString()
                        val autoPlay=binding.autoStyleOfPlay.text.toString()
                        sendProfileData(surname, countryId,skillLevel,autoPosition, autoPlay)
                    }
                }else{
                    Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
                }

            }

            R.id.drop_world_cup->{
                if (cupmodelList.size>0){
                    if (isCheck){
                        isCheck=false
                        binding.lay1.visibility=View.VISIBLE
                        binding.view1.visibility=View.VISIBLE
                    }else{
                        binding.lay1.visibility=View.GONE
                        binding.view1.visibility=View.GONE
                        isCheck=true
                    }
                }
            }
            R.id.btn_viewpager->{
                if (binding.viewpagerData.currentItem + 1 < adapter!!.itemCount) {
                    binding.viewpagerData.currentItem += 1
                    Log.d("*****currentItem",""+binding.viewpagerData.currentItem)
                }
            }
        }
    }

    private fun sendProfileData(surname: String, countryId: Int?, skillLevel: String, autoPosition: String, autoPlay: String) {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.sendProfileData(
                {
                    sessionManager.dismissMe()
                    when (it) {
                        is NetworkResult.Success -> {
                            try {
                                val gson = Gson()
                                val model = gson.fromJson(it.data, LoginResp::class.java)
                                if (model.code == 200 && model.success) {
                                    try {
                                        Toast.makeText(context, model.message, Toast.LENGTH_LONG).show()
                                        sessionManager.setCheckLogin("yes")
                                        val intent = Intent(requireContext(), MainActivity::class.java)
                                        startActivity(intent)
                                        requireActivity().finish()
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
                },
                surname,
                countryId.toString(),
                skillLevel,
                autoPosition,
                autoPlay,
                worldCupId.toString(),
                ""
            )
        }

    }

    // This function is used for check all validation in all filed
    private fun isValidation(): Boolean {
        if (binding.edName.text.toString().isEmpty()) {
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
        }
        return true
    }

}