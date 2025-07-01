package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.databinding.FragmentSuddenDeathBinding
import com.yesitlabs.jumballapp.databinding.FragmentTermsAndConditionBinding
import com.yesitlabs.jumballapp.network.viewModel.PenaltyScreenViewModel
import com.yesitlabs.jumballapp.network.viewModel.WorldCupWonViewModel


class SuddenDeathFragment : Fragment() {


    private var userType = "USER"
    private var user = ""
    private var cpu = ""
    lateinit var sessionManager : SessionManager
    private lateinit var viewModel : PenaltyScreenViewModel
    private lateinit var worldCupWonViewmodel: WorldCupWonViewModel

    private lateinit var binding: FragmentSuddenDeathBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSuddenDeathBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(1, 1)
        viewModel = ViewModelProvider(requireActivity())[PenaltyScreenViewModel::class.java]



        userType=arguments?.getString("userType","USER").toString()
        user=arguments?.getString("player1","").toString()
        cpu=arguments?.getString("player2","").toString()


        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("****","Back Lock")
                }
            })

    }

    fun setBackgroundGreen(img:ImageView){
        img.setBackgroundResource(R.drawable.ball_2)
    }
    fun setBackgroundRed(img: ImageView){
        img.setBackgroundResource(R.drawable.ball_1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        worldCupWonViewmodel = ViewModelProvider(this)[WorldCupWonViewModel::class.java]

        binding.playerName1.text = user
        binding.playerName2.text = cpu

        binding.tvMynumber.text=""+viewModel.userPoint

        binding.tvCpunumber.text=""+viewModel.cpuPoint
        Log.d("Error ","cpu count "+viewModel.userPoint)
        Log.d("Error ","user count "+viewModel.cpuPoint)

        settingUiData()




        Handler().postDelayed({
            if(viewModel.cpuPoint != viewModel.userPoint){
                if (sessionManager.isNetworkAvailable()) {
                    worldCupWon()
                } else {
                    sessionManager.alertError(getString(R.string.no_internet))
                }
            }else{
                Handler(Looper.myLooper()!!).postDelayed({
                    viewModel.loopCount =0
                    viewModel.count =0
                    viewModel.userCount =0
                    viewModel.cpuCount =0
                    val bundle=Bundle()
                    bundle.putString("userType",userType)
                    findNavController().navigate(R.id.penaltiesPlayUserFragment,bundle)

                }, 3000)
//                alertBoxAccordingToResponse(R.drawable.extra_time_ht_img,"extra")
            }
        },2000)

    }

    // This function is used for store the data world cup is won or loss
    private fun worldCupWon() {



        worldCupWonViewmodel.worldCupWonResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val wonResp = response.body()
                Log.d("@@@Api Response ","******"+wonResp?.message)
                if (wonResp != null) {
                    if(viewModel.cpuPoint > viewModel.userPoint){
                        alertBoxAccordingToResponse(R.drawable.eliminated_img,"loss")
                    }else{
                        alertBoxAccordingToResponse(R.drawable.winner_img,"Win")
                    }
                } else {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Record Not Register !", Toast.LENGTH_SHORT).show()
            }
        }


        if(viewModel.cpuPoint > viewModel.userPoint){
            worldCupWonViewmodel.worldCupWon("Bearer " +sessionManager.fetchAuthToken()!!,"0")
        }else{
            worldCupWonViewmodel.worldCupWon("Bearer " +sessionManager.fetchAuthToken()!!,"1")
        }



    }


    private fun settingUiData(){
        for (i in  0..viewModel.cpuList.size-1){
            if(i ==0){
                if(viewModel.cpuList[0]){
                    binding.cpuscr11.setBackgroundResource(R.drawable.ball_2)
                }else{
                    binding.cpuscr11.setBackgroundResource(R.drawable.ball_1)
                }

            }
            else if( i==1){
                if(viewModel.cpuList[1]){
                    binding.cpuscr2.setBackgroundResource(R.drawable.ball_2)
                }else{
                    binding.cpuscr2.setBackgroundResource(R.drawable.ball_1)
                }
            }
            else if(i ==2){
                if(viewModel.cpuList[2]){
                    binding.cpuscr3.setBackgroundResource(R.drawable.ball_2)
                }else{
                    binding.cpuscr3.setBackgroundResource(R.drawable.ball_1)
                }
            }
            else if(i ==3){
                if(viewModel.cpuList[3]){
                    binding.cpuscr4.setBackgroundResource(R.drawable.ball_2)
                }else{
                    binding.cpuscr4.setBackgroundResource(R.drawable.ball_1)
                }
            }
            else if(i ==4){
                if(viewModel.cpuList[4]){
                    binding.cpuscr5.setBackgroundResource(R.drawable.ball_2)
                }else{
                    binding.cpuscr5.setBackgroundResource(R.drawable.ball_1)
                }
            }
        }

        for(i in 0 ..viewModel.userlist.size-1){
            if(i ==0){
                if(viewModel.userlist.get(0)){
                    setBackgroundGreen(binding.scr1)
                }else{
                    setBackgroundRed(binding.scr1)
                }
            }
            else if( i==1){
                if(viewModel.userlist.get(1)){
                    setBackgroundGreen(binding.scr2)
                }else{
                    setBackgroundRed(binding.scr2)
                }
            }
            else if(i ==2){
                if(viewModel.userlist.get(2)){
                    setBackgroundGreen(binding.scr3)
                }else{
                    setBackgroundRed(binding.scr3)
                }
            }
            else if(i ==3){
                if(viewModel.userlist.get(3)){
                    setBackgroundGreen(binding.scr4)
                }else{
                    setBackgroundRed(binding.scr4)
                }
            }
            else if(i ==4){
                if(viewModel.userlist.get(4)){
                    setBackgroundGreen(binding.scr5)
                }else{
                    setBackgroundRed(binding.scr5)
                }
            }
        }
        viewModel.cpuList.clear()
        viewModel.userlist.clear()
    }

    private fun alertBoxAccordingToResponse(savedImg: Int, type: String){
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)
        sessionManager.changeMusic(21, 0)

        imgChange.setImageResource(savedImg)




        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
            /*if (type.equals("loss",true)){
                sessionManager.resetScore()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity()
            }
            if (type.equals("win",true)){

            }*/
            if (type.equals("extra",true)){
                viewModel.loopCount =0
                viewModel.count =0
                viewModel.userCount =0
                viewModel.cpuCount =0
                val bundle=Bundle()
                bundle.putString("userType",userType)
                findNavController().navigate(R.id.penaltiesPlayUserFragment,bundle)
            }else{
                sessionManager.putCpuScore(0)
                sessionManager.putMyScore(0)
                sessionManager.putCpuScore(viewModel.cpuPoint)
                sessionManager.putMyScore(viewModel.userPoint)
                val bundle = Bundle()
                bundle.putString("opposeTeamName", binding.playerName2.text.toString())
                bundle.putString("myTeamName", binding.playerName1.text.toString())
                findNavController().navigate(R.id.score_fragment, bundle)
            }

        }, 3000)

        dialog.show()
    }

}