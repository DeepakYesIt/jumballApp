package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.activity.GroupDetailsActivity
import com.yesitlabs.jumballapp.activity.MainActivity
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentFinalScoreBinding
import com.yesitlabs.jumballapp.network.viewModel.SaveScoreViewModel


class FinalScoreFragment : Fragment(){

    lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentFinalScoreBinding
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var saveScoreViewmodel: SaveScoreViewModel
    var captianId=""
    var my_guesses=""
    var opponent_guessed=""
    var total_defence=""
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFinalScoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(0, 1)
        teamDbHelper = TeamDatabaseHelper(requireContext())
        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        saveScoreViewmodel = ViewModelProvider(this)[SaveScoreViewModel::class.java]


        Log.d("@Error","number :-  "+sessionManager.getGameNumber())

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Log.d("*******","back Stop")
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        try {
            cpuDbHelper.getAllPlayers().forEach {
                if (it.is_captain.equals("1",true)){
                    captianId= it.id
                }
            }
        }catch (e:Exception){
            captianId=""
        }

        Log.d("@Error ","before worldcup")

        if (sessionManager.getMatchType().equals("worldcup",true)) {

            Log.d("@Error ","before TeamDatabaseHelper")

            teamDbHelper = TeamDatabaseHelper(requireContext())
            val allTeam = teamDbHelper.getAllTeams()
            for (data in allTeam) {
                if (data.teamID == 1) {
                    binding.userName.text = data.captainName
                }
               /* if (data.teamID == sessionManager.getTeamDetails()) {
                    binding.opposeTeamPlayerName.text = data.captainName
                }*/

                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                    if (data.teamID == sessionManager.getTeamDetails()) {
                        binding.opposeTeamPlayerName.text = data.captainName
                    }
                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                    binding.opposeTeamPlayerName.text = "Asst. Manager"
                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                    binding.opposeTeamPlayerName.text = "Lizard mascot"
                }

            }
            Log.d("@Error ","after TeamDatabaseHelper")
        } else {
            binding.userName.text = sessionManager.getName()?.split(" ")?.drop(1)?.joinToString(" ")
            binding.opposeTeamPlayerName.text = "CPU"
        }

        Log.d("@Error ","after worldcup")

        if (sessionManager.getCpuScore() <= 9) {
            binding.cpuScoreTv.text = "0" + sessionManager.getCpuScore().toString()
        } else {
            binding.cpuScoreTv.text = sessionManager.getCpuScore().toString()
        }

        if (sessionManager.getMyScore() <= 9) {
            binding.MyTeamScore.text = "0" + sessionManager.getMyScore().toString()
        } else {
            binding.MyTeamScore.text = sessionManager.getMyScore().toString()
        }

        Log.e("@Error ", sessionManager.getMyScore().toString())
        Log.e("@Error ", sessionManager.getCpuScore().toString())

        Log.e("@Error captianId", captianId)
        Log.e("@Error captianId", sessionManager.getTotalDefence().toString())
        Log.e("@Error captianId", sessionManager.getMyNameSuggessionPass().toString())
        Log.e("@Error captianId", sessionManager.getcpuNameSuggessionPass().toString())


        if (binding.cpuScoreTv.text.toString().toInt() >= binding.MyTeamScore.text.toString().toInt()) {
            storeScore(binding.MyTeamScore.text.toString(), binding.cpuScoreTv.text.toString(), "0")
        } else {
            storeScore(binding.MyTeamScore.text.toString(), binding.cpuScoreTv.text.toString(), "1")
        }


    }


    // This is used for store user score of every match in database
    private fun storeScore(totalGoal: String, totalGoalConsole: String, matchStatus: String) {
        saveScoreViewmodel.saveScoreList("Bearer " + sessionManager.fetchAuthToken()!!, totalGoal, totalGoalConsole, matchStatus,captianId,sessionManager.getTotalDefence().toString(),sessionManager.getcpuNameSuggessionPass().toString(),sessionManager.getMyNameSuggessionPass().toString())
        saveScoreViewmodel.saveScoreListResponse.observe(requireActivity()) { response ->
            binding.tvProgressBar.visibility = View.VISIBLE
            sessionManager.saveMyNameSuggessionPass(0)
            sessionManager.savecpuNameSuggessionPass(0)
            sessionManager.saveTotalDefence(0)

            if (response != null) {
                val scoreResp = response.body()
                if (scoreResp != null) {
                    if (scoreResp.success && scoreResp.code == 200L) {
                        binding.tvProgressBar.visibility = View.GONE
                        checkScore()
                    } else {
                        Toast.makeText(requireContext(), scoreResp.message, Toast.LENGTH_SHORT).show()
//                        findNavController().navigate(R.id.action_scoreBoardFragment_to_dashBoradFragment)
//                        findNavController().navigate(R.id.dashBoardFragment)
                        binding.tvProgressBar.visibility = View.GONE
                        val intent=Intent(requireContext(),MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            } else {
                binding.tvProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Server Issue!", Toast.LENGTH_SHORT).show()
                val intent=Intent(requireContext(),MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }


    }

    // This is used for check user score of every match
    private fun checkScore() {

//        if (sessionManager.getMatchType() == "worldcup") {
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
//        }

        Handler(Looper.getMainLooper()).postDelayed({
//            if (sessionManager.getMatchType().equals("worldcup",true)) {
                if (binding.cpuScoreTv.text.toString().toInt() == binding.MyTeamScore.text.toString().toInt()) {

                    sessionManager.setGameNumber(sessionManager.getGameNumber()+1)
                    // Match Draw
                    //Match Loss
                    val intent = Intent(requireContext(), GroupDetailsActivity::class.java)
                    intent.putExtra("win", 2)
                    intent.putExtra("winType", "Drow")
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    if (binding.cpuScoreTv.text.toString().toInt() < binding.MyTeamScore.text.toString().toInt()) {
                        sessionManager.setGameNumber(sessionManager.getGameNumber()+1)
                        val intent = Intent(requireContext(), GroupDetailsActivity::class.java)
                        // Match Win
                        intent.putExtra("win", 1)
                        intent.putExtra("winType", "win")
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    if (binding.cpuScoreTv.text.toString().toInt() > binding.MyTeamScore.text.toString().toInt()) {
                        //Match Loss
//                        sessionManager.resetScore()
//                        sessionManager.resetGameNumberScore()
////                        findNavController().navigate(R.id.dashBoardFragment)
//                        val intent=Intent(requireContext(),MainActivity::class.java)
//                        startActivity(intent)
//                        requireActivity().finish()
                        val intent = Intent(requireContext(), GroupDetailsActivity::class.java)
                        // Match Win
                        intent.putExtra("win", 1)
                        intent.putExtra("winType", "Loss")
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
//            } else {
//                sessionManager.resetScore()
//                sessionManager.resetGameNumberScore()
////                findNavController().navigate(R.id.dashBoardFragment)
//                val intent=Intent(requireContext(),MainActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
        }, 2000)

    }


    // This function is used for display the alert box of world cup is won or loss
    @SuppressLint("SuspiciousIndentation")
    private fun winAlertBox(int: Int) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView = dialog.findViewById(R.id.img_change)

        sessionManager.changeMusic(21, 0)


        if (int==0){
            imgChange.setImageResource(R.drawable.eliminated_img)
        }else{
            imgChange.setImageResource(R.drawable.winner_img)
        }

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 2000)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
            if (int==0){
                sessionManager.resetScore()
                sessionManager.resetGameNumberScore()
                findNavController().navigate(R.id.action_scoreBoardFragment_to_dashBoradFragment)
            }else{
                sessionManager.setGameNumber(sessionManager.getGameNumber() + 1)
                val intent = Intent(requireContext(), GroupDetailsActivity::class.java)

                 /* if (binding.cpuScoreTv.text.toString().toInt() == binding.MyTeamScore.text.toString().toInt()) {
                      // Match Draw
                      intent.putExtra("win", 0)
                  }*/

                if (binding.cpuScoreTv.text.toString().toInt() < binding.MyTeamScore.text.toString().toInt()) {
                    // Match Win
                    intent.putExtra("win", 1)
                }

                 if (binding.cpuScoreTv.text.toString().toInt() > binding.MyTeamScore.text.toString().toInt()) {
                     //Match Loss
                     intent.putExtra("win", 2)
                 }
                startActivity(intent)
                requireActivity().finish()
            }
        }, 1000)

        dialog.show()
    }

}

