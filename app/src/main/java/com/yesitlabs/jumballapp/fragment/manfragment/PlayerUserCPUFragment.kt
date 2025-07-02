package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.ValueStore
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.databinding.FragmentPlayerUserCPUBinding
import com.yesitlabs.jumballapp.gameRule.SetGames
import java.util.Locale


class PlayerUserCPUFragment : Fragment() , View.OnClickListener{
    private lateinit var binding: FragmentPlayerUserCPUBinding
    lateinit var sessionManager: SessionManager
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    var totalTime = 5400000L
    var startTime = 0
    private var myPass = 0
    private var cpuPass = 0
    private var userType: String? = null
    private var playerIdUser = 0
    //Shrawan
    private var  isGoalClick  = false
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    private var setGames: SetGames = SetGames()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerUserCPUBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())


        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
            binding.layProgess.max = 900000
            totalTime=900000
            startTime=0
            if (sessionManager.getTimer() != 0) {
                startTime = sessionManager.getTimer()
                if (startTime>=900000){
                    totalTime=900000
                }else{
                    totalTime -= sessionManager.getTimer().toLong()
                }
            }
        }else{
            binding.layProgess.max = 2700000
            if (sessionManager.getTimer() != 0) {
                startTime = sessionManager.getTimer()
                if (startTime>=5400000){
                    totalTime=5400000
                }else{
                    totalTime -= sessionManager.getTimer().toLong()
                }
            }
        }



        myPass = sessionManager.getMyPass()
        cpuPass = sessionManager.getCpuPass()

        if (arguments != null) {
            userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
            playerIdUser = requireArguments().getInt("id")
            isGoalClick = requireArguments().getBoolean("isGoalClick",false)//Shrawan
        } else {
            Log.e("Argument", "No")
        }

        Log.d("@@@Error ", "getExtraTime"+sessionManager.getExtraTime())
        Log.d("@@@Error ", "sessionManager.getTimer"+sessionManager.getTimer())
        Log.d("@@@Error ","match count"+sessionManager.getGameNumber())
        Log.d("@@@Error ","timer count"+sessionManager.getTimer())
        Log.d("@@@Error ", "startTime$startTime")
        Log.d("@@@Error ", "totalTime$totalTime")
        Log.d("@@@Error ", "myPass$myPass")
        Log.d("@@@Error ", "cpuPass$cpuPass")
        Log.d("@@@Error ", "userType$userType")

        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())

//        val team = listOf(
//            Player("Alex Martinez", 1, "Goalkeeper", "Starter"),
//            // 3 Defenders
//            Player("Ryan Blake", 2, "Defender", "Starter"),
//            Player("Jamal Okafor", 3, "Defender", "Starter"),
//            Player("Luca Fernandez", 4, "Defender", "Starter"),
//            // 2 Midfielders
//            Player("Kevin Liu", 6, "Midfielder", "Starter"),
//            Player("Daniel Costa", 7, "Midfielder", "Starter"),
//
//            // 5 Attackers (Wingers/Forwards)
//            Player("Carlos Mendes", 9, "Attacker", "Starter"),
//            Player("Jordan Knox", 10, "Attacker", "Starter"),
//            Player("Ahmed Salah", 11, "Attacker", "Starter"),
//            Player("Leo Anders", 8, "Attacker", "Starter"),
//            Player("Samir Patel", 14, "Attacker", "Starter") // Central striker
//        )
//        setupFootballFormation("3-2-5-1",team)

        timerLogic()

        setPlayerScreens()

        backButton()

        binding.btShoot.setOnClickListener(this)
        binding.btPass.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun setPlayerScreens(){
        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()
        Log.e("@@@Error ", "All Cpu Player $allCpuPlayer")
        Log.e("@@@Error ", "All User Player $allUserPlayer")
        Log.e("@@@Error ","All name "+sessionManager.getName().toString())
        if (sessionManager.getMatchType().equals("worldcup",true)) {
            teamDbHelper = TeamDatabaseHelper(requireContext())
            val allTeam = teamDbHelper.getAllTeams()
            for (data in allTeam) {
                if (data.teamID == 1) {
                    binding.userName.text = data.captainName
                }
                if(sessionManager.getGameNumber() <= 3)  { //for first 3 match
                    binding.opposeTeamPlayerName.text = data.captainName
                }else if(sessionManager.getGameNumber() == 4){ // for 4th match
                    binding.opposeTeamPlayerName.text = "Asst. Manager"
                }else if(sessionManager.getGameNumber() == 5){ // for 5th match
                    binding.opposeTeamPlayerName.text = "Lizard mascot"
                }
            }

        } else {
            binding.userName.text = sessionManager.getName()?.split(" ")?.drop(1)?.joinToString(" ")
            binding.opposeTeamPlayerName.text = "CPU"
        }

        binding.cpuScoreTv.text = sessionManager.getCpuScore().toString()
        binding.userGoalTv.text = sessionManager.getMyScore().toString()


        if (userType.equals("USER",true)){
            binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            setupFootballFormation("3-2-5-1",allCpuPlayer)
        }else{
            binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            setupFootballFormation("3-2-5-1",allUserPlayer)
        }




    }

    private fun timerLogic(){

        val min = startTime / 60000
        binding.tvCount.text = "‘$min"
        val timeForNoise = startTime / 60000
        Log.d("@@@Error", "***** $timeForNoise")
        Log.d("@@@Error", "***** show time tv_count  $min")
        if (timeForNoise >= 3 && timeForNoise % 3 == 0) {
            sessionManager.changeMusic(14, 0)
        }
        if (timeForNoise >= 5 && timeForNoise % 5 == 0) {
            sessionManager.changeMusic(1, 1)
        }
        if (timeForNoise >= 9 && timeForNoise % 9 == 0) {
            sessionManager.changeMusic(1, 1)
        }
        if (startTime / 60000 == 0 && startTime > 60000) {
            Log.e("Time ", min.toString())
        }
        Log.d("@@@Error ","******** "+sessionManager.getExtraTime())
        if (sessionManager.getExtraTime().equals("ExtraTime",true) || sessionManager.getExtraTime().equals("TimeHalf",true)){
            sessionManager.changeMusic(6, 0)
            if (sessionManager.getExtraTime().equals("ExtraTime",true)){
                Log.d("@@@Error","when TimeHalf time set")
                if (startTime >= 900000){
                    totalTime = 900000L
                    startTime = 0
//                    status=false
//                    setCondition("ExtraTime")
                }
            }

            if (sessionManager.getExtraTime().equals("TimeHalf",true)){
                if (startTime >= 900000){
                    Log.d("@@@Error","when FinalTime time set")
                    totalTime = 900000L
                    startTime = 0
                    sessionManager.setExtraTimeUser("TimeHalfEnd")
//                    status=false
//                    setCondition("TimeHalfEnd")
                }
            }

            if (startTime <= 900000) {
                binding.layProgess.progress = startTime
            } else {
                binding.layProgess.progress = startTime - 900000
            }

        }else{
            Log.e("@@@Error", "Full getValue "+ ValueStore.getValue1())
            if (startTime >= 5400000 && ValueStore.getValue1() ==0){
                ValueStore.setValue1(1)
                Log.d("@@@Error","when FullTime time set")
                Log.e("@@@Error", "Full time")
                sessionManager.changeMusic(6, 0)
//                isTimerFinish = true
                if (sessionManager.getGameNumber()==3){
                    totalTime = 900000L
                    startTime = 0
                    sessionManager.setExtraTimeUser("FullTime")
                    Log.e("@@@Error", "second")
//                    playAlertBox(R.drawable.full_time_img, "FullTime")
                }else{
                    sessionManager.setExtraTimeUser("timeOver")
                    Log.e("@@@Error", "timeOver")
//                    playAlertBox(R.drawable.full_time_img, "timeOver")
                }
            }else{
                if (startTime >= 2700000 && ValueStore.getValue() == 0) {
                    ValueStore.setValue(1)
                    Log.d("@@@Error","when half time start")
                    Log.e("Half Time", "Yaa hui")
                    sessionManager.changeMusic(6, 0)
                    sessionManager.setExtraTimeUser("halftime")
//                    playAlertBox(R.drawable.half_time_img, "halftime")
                }
            }

            if(startTime <= 2700000) {
                binding.layProgess.progress = startTime
            }else {
                binding.layProgess.progress = startTime - 2700000
            }
        }



    }

    private fun backButton(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @SuppressLint("SetTextI18n")
    private fun setupFootballFormation(players1: String, players: List<PlayerModel>) {
        binding.formationContainer.removeAllViews()

        // Group players by position

        val groupedPlayers = listOf(
            "FW" to players.filter { it.designation == "FW" },
            "MF" to players.filter { it.designation == "MF" },
            "DF" to players.filter { it.designation == "DF" },
            "GK" to players.filter { it.designation == "GK" },
        )

        for ((position, playersInPosition) in groupedPlayers) {
            // Create a row for the position
            val row = LinearLayout(requireContext()).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_HORIZONTAL
            }

            // Add space before players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))

            for ((index, player) in playersInPosition.withIndex()) {
                val playerView = LayoutInflater.from(requireContext()).inflate(R.layout.playerview, row, false)
                val rpSelect = playerView.findViewById<ImageView>(R.id.rp_select)
                val playerImage = playerView.findViewById<ImageView>(R.id.rp_img)
                val rpName = playerView.findViewById<TextView>(R.id.rp_name)
                val rpNumber = playerView.findViewById<TextView>(R.id.rp_number)

                // Set margin on playerView
                val layoutParams = LayoutParams(
                    playerView.layoutParams.width,
                    playerView.layoutParams.height
                ).apply {
                    topMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._5sdp) // or use a fixed value e.g., 16
                }

                playerView.layoutParams = layoutParams
                if (player.answer.equals("true",true)){
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.VISIBLE
                    rpNumber.visibility = View.VISIBLE
                    rpName.text = player.name
                    val countryID = player.country_id
                    val textColor = ContextCompat.getColor(requireContext(), setGames.getTShirtTextColor(countryID))
                    playerImage.setImageResource(setGames.getTShirtImage(countryID))
                    rpNumber.setTextColor(textColor)
                    val countAnswer = players.count { it.answer.equals("true",true) }?:0
                    if (countAnswer!=0){
                        binding.rootButton.visibility=View.VISIBLE
                        if (countAnswer>1){
                            binding.btShoot.visibility=View.VISIBLE
                            binding.btPass.visibility=View.VISIBLE
                        }else{
                            binding.btShoot.visibility=View.GONE
                            binding.btPass.visibility=View.VISIBLE
                        }
                    }else{
                        binding.rootButton.visibility=View.GONE
                    }

                }else{
                    playerImage.setImageResource(R.drawable.user_guess_white)
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.GONE
                    rpNumber.visibility = View.GONE
                    if (player.type.equals("yes",true)){
                        rpSelect.visibility = View.VISIBLE
                    }else{
                        rpSelect.visibility = View.GONE
                    }
                    playerView.setOnClickListener {
                        val countAnswer = players.count { it.answer.equals("true",true) }?:0
                        if (countAnswer==0){
                            if (player.designation.equals("MF",true)){
                                val playerIndex = players.indexOfFirst { it.jersey_number == player.jersey_number  }
                                if (playerIndex != -1) {
                                    players[playerIndex].answer = "true"
                                }
                            }
                        }else{
                            if (player.designation.equals("GK",true)){
                                players.forEach {
                                    it.answer="true"
                                }
                            }else{
                                val playerIndex = players.indexOfFirst { it.jersey_number == player.jersey_number  }
                                if (playerIndex != -1) {
                                    players[playerIndex].answer = "true"
                                }
                            }

                        }
                        Toast.makeText(requireContext(),"player Name -"+player.name,Toast.LENGTH_SHORT).show()
                        // Refresh with filter for that position only
                        setupFootballFormation("", players)
                    }
                }

                row.addView(playerView)

                // Add space between players
                if (index < playersInPosition.size - 1) {
                    row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))
                }
            }

            // Add space after players to center them
            row.addView(Space(requireContext()), LayoutParams(0, LayoutParams.MATCH_PARENT, 1f))

            // Add the row to the container
            binding.formationContainer.addView(row)
        }
    }

    data class Player(
        val name: String,
        val number: Int,
        val position: String,
        val status: String,
        var showStatus: Boolean = false
    )

    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.bt_pass -> {
                    userPassBall()
            }
        }
    }

    private fun userPassBall() {
            isGoalClick = false
            if (userType.equals("USER",true)) {
                val targetIds = setOf(1, 2, 3, 6, 7, 8, 9)
                allCpuPlayer = allCpuPlayer
                    .map { player ->
                        if (player.id.toInt()  in targetIds) {
                            player.copy(type = "yes")
                        } else {
                            player
                        }
                    }
                    .toCollection(ArrayList())  // ensures you get an ArrayList<PlayerModel>
                setupFootballFormation("3-2-5-1",allCpuPlayer)
                Toast.makeText(requireContext(),"pass user ",Toast.LENGTH_SHORT).show()
            } /*else {
                autoButtonClick()
            }*/
    }

}