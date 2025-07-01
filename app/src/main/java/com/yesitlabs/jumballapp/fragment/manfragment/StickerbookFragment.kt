package com.yesitlabs.jumballapp.fragment.manfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.adapter.StickerbookAdapter
import com.yesitlabs.jumballapp.databinding.FragmentStickerbookBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.stickerResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StickerbookFragment : Fragment(), View.OnClickListener {

    private var stickerList = ArrayList<String>()
    private lateinit var stickerbookAdapter: StickerbookAdapter

    lateinit var sessionManager: SessionManager
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var binding: FragmentStickerbookBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStickerbookBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[StatisticsViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        if (sessionManager.getCheckLogin() == "yes") {
            if(sessionManager.isNetworkAvailable()){
                getSticker()
            }else{
                Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
            }

        }else{
            setStickerList()
        }

        binding.btnBack.setOnClickListener(this)

    }

    // This function is used for get the earned sticker from server
    private fun getSticker() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.getSticker {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, stickerResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    // Remove duplicates
                                    val uniqueData = model.data?.distinctBy { it.sticker_id } // Ensures uniqueness
                                    for (i in uniqueData!!.indices){
                                        if (i==0 || i==2){
                                            stickerList.add("")
                                            stickerList.add(uniqueData[i].image)
                                        }else{
                                            stickerList.add(uniqueData[i].image)
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
                        setStickerList()
                    }
                    is NetworkResult.Error -> {
                        setStickerList()
                        sessionManager.alertError(it.message.toString())
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }
        }
    }

    // This function is used for set the earned sticker in  screen
    private fun setStickerList() {

        var diff = 0
        if (stickerList.size < 20){
            diff = 19 - stickerList.size
        }
        for (i in 0..diff){
            stickerList.add("")
        }
        stickerbookAdapter = StickerbookAdapter(stickerList, requireActivity())
        binding.rcySticker.adapter = stickerbookAdapter
        binding.rcySticker.layoutManager = GridLayoutManager(context, 4)

    }

    override fun onClick(item: View?) {
        sessionManager.playClickSound()
        when (item?.id) {
            R.id.btn_back -> {
                findNavController().navigateUp()
            }
        }
    }

}


