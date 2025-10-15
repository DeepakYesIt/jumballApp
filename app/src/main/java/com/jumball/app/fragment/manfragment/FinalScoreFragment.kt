package com.jumball.app.fragment.manfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.jumball.app.SessionManager
import com.jumball.app.activity.GroupDetailsActivity
import com.jumball.app.activity.MainActivity
import com.jumball.app.database.player_dtl.CPUPlayerDatabaseHelper
import com.jumball.app.database.team_dtl.TeamDatabaseHelper
import com.jumball.app.databinding.FragmentFinalScoreBinding
import com.jumball.app.errormassage.ErrorMessage
import com.jumball.app.model.SaveScoreResp
import com.jumball.app.network.NetworkResult
import com.jumball.app.viewmodeljumball.SaveScoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinalScoreFragment : Fragment(){

    lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentFinalScoreBinding
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var viewmodel: SaveScoreViewModel
    private var captainId=""
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinalScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(0, 1)
        teamDbHelper = TeamDatabaseHelper(requireContext())
        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        viewmodel = ViewModelProvider(this)[SaveScoreViewModel::class.java]
        Log.d("@Error","number :-  "+sessionManager.getGameNumber())
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Log.d("*******","back Stop")
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        Log.d("@Error ","before worldcup")
        if (sessionManager.getMatchType().equals("worldcup",true)) {
            Log.d("@Error ","before TeamDatabaseHelper")
            try {
                cpuDbHelper.getAllPlayers().forEach {
                    if (it.is_captain.equals("1",true)){
                        captainId= it.id
                    }
                }
            }catch (e:Exception){
                captainId=""
            }
            teamDbHelper = TeamDatabaseHelper(requireContext())
            val allTeam = teamDbHelper.getAllTeams()
            for (data in allTeam) {
                if (data.teamID == 1) {
                    binding.userName.text = sessionManager.getLastName(data.captainName?.uppercase()?:"")
                }
                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                    val cpuName = cpuDbHelper.getAllPlayers().find { it.is_captain.equals("1",true) }
                    binding.opposeTeamPlayerName.text = cpuName?.name?:""
                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                    binding.opposeTeamPlayerName.text = "Manager"
                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                    binding.opposeTeamPlayerName.text = "Lizard mascot"
                }
            }
            Log.d("@Error ","after TeamDatabaseHelper")
        } else {
            binding.userName.text=sessionManager.getLastName(sessionManager.getName()?.uppercase()?:"")
            binding.opposeTeamPlayerName.text = "CPU"
        }
        Log.d("@Error ","after worldcup")
        if (sessionManager.getCpuScore() <= 9) {
            binding.cpuScoreTv.text = /*"0" +*/ sessionManager.getCpuScore().toString()
        } else {
            binding.cpuScoreTv.text = sessionManager.getCpuScore().toString()
        }
        if (sessionManager.getMyScore() <= 9) {
            binding.MyTeamScore.text = /*"0" +*/ sessionManager.getMyScore().toString()
        } else {
            binding.MyTeamScore.text = sessionManager.getMyScore().toString()
        }
        Log.e("@Error ", sessionManager.getMyScore().toString())
        Log.e("@Error ", sessionManager.getCpuScore().toString())
        Log.e("@Error captianId", captainId)
        Log.e("@Error captianId", sessionManager.getTotalDefence().toString())
        Log.e("@Error captianId", sessionManager.getMyNameSuggessionPass().toString())
        Log.e("@Error captianId", sessionManager.getcpuNameSuggessionPass().toString())
        val myTeamScore = binding.MyTeamScore.text.toString().toInt()
        val cpuScore = binding.cpuScoreTv.text.toString().toInt()
        val result = if (cpuScore >= myTeamScore) "0" else "1"
        if (sessionManager.isNetworkAvailable()) {
            storeScore(myTeamScore.toString(), cpuScore.toString(), result)
        } else {
            sessionManager.alertError(ErrorMessage.netWorkError)
        }
    }
    // This is used for store user score of every match in database
    private fun storeScore(totalGoal: String, totalGoalConsole: String, matchStatus: String) {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewmodel.saveScoreList({
                sessionManager.dismissMe()
                sessionManager.saveMyNameSuggessionPass(0)
                sessionManager.savecpuNameSuggessionPass(0)
                sessionManager.saveTotalDefence(0)
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, SaveScoreResp::class.java)
                            if (model.code == 200 && model.success) {
                                checkScore()
                            } else {
                                Toast.makeText(requireContext(),model.message,Toast.LENGTH_SHORT).show()
                                moveToNextScreen()
                            }
                        }catch (e:Exception){
                            Log.d("signup","message:---"+e.message)
                            moveToNextScreen()
                        }
                    }
                    is NetworkResult.Error -> {
                        Log.d("signup","message:---"+it.message)
                        moveToNextScreen()
                    }
                    else -> {
                        sessionManager.alertError(it.message.toString())
                    }
                }
            }, totalGoal,
                totalGoalConsole,
                matchStatus,
                captainId,
                sessionManager.getTotalDefence().toString(),
                sessionManager.getcpuNameSuggessionPass().toString(),
                sessionManager.getMyNameSuggessionPass().toString(),
                sessionManager.getGameNumber().toString())
        }
    }
    private fun moveToNextScreen(){
        val intent=Intent(requireContext(),MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    // This is used for check user score of every match
    private fun checkScore() {
            if (sessionManager.getMyScore() > sessionManager.getCpuScore()) {
                sessionManager.changeMusic(13, 0)
                teamDbHelper.updateW(1, 3)
            }
            if (sessionManager.getMyScore() == sessionManager.getCpuScore()) {
                sessionManager.changeMusic(7, 0)
                teamDbHelper.updateD(1, 1)
            }
            if (sessionManager.getMyScore() < sessionManager.getCpuScore()) {
                sessionManager.changeMusic(11, 0)
                teamDbHelper.updateL(1, 3)
            }
            teamDbHelper.updatePLD(1, 1)
            Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireContext(), GroupDetailsActivity::class.java)
                if (binding.cpuScoreTv.text.toString().toInt() == binding.MyTeamScore.text.toString().toInt()) {
                    sessionManager.setGameNumber(sessionManager.getGameNumber()+1)
                    intent.putExtra("win", 2)
                    intent.putExtra("winType", "Drow")
                } else {
                    intent.putExtra("win", 1)
                    if (binding.cpuScoreTv.text.toString().toInt() < binding.MyTeamScore.text.toString().toInt()) {
                        sessionManager.setGameNumber(sessionManager.getGameNumber()+1)
                        intent.putExtra("winType", "win")
                    }
                    if (binding.cpuScoreTv.text.toString().toInt() > binding.MyTeamScore.text.toString().toInt()) {
                        intent.putExtra("winType", "Loss")
                    }
                }
            startActivity(intent)
            requireActivity().finish()
        }, 2000)
    }
}

