package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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


class PlayerUserCPUFragment :Fragment() , View.OnClickListener{

    private lateinit var binding: FragmentPlayerUserCPUBinding
    lateinit var sessionManager: SessionManager
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    var totalTime = 5400000L
    var startTime = 0
    private var myPass = 0
    private var cpuPass = 0
    private var userType: String? = null
    private var playerIdUser = 0
    private var  isGoalClick  = false
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper
    private lateinit var teamDbHelper: TeamDatabaseHelper
    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()
    private var setGames: SetGames = SetGames()
    private var isCpuActive = false
    // List to store clickable players and their views
    val clickablePlayers = mutableListOf<Pair<View, PlayerModel>>()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (isCpuActive) {
                selectCpuButton()
                handler.postDelayed(this, 3000) // Repeat every 3 seconds
            }
        }
    }

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
            stopCpuProcess()
            setupFootballFormation(sessionManager.getUserScreenType(),allCpuPlayer)
        }else{
            binding.opposeTeamPlayerName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.userName.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            setupFootballFormationCpu(sessionManager.getUserScreenType(),allUserPlayer)
        }
    }

    private fun selectCpuButton(){
        if (clickablePlayers.isNotEmpty()) {
            val countAnswer = allUserPlayer.count { it.answer.equals("true",true) }?:0
            if (countAnswer==0){
                val randomTarget =  (6..7).random().toString()
                val targetPlayer = clickablePlayers.firstOrNull { (_, player) ->
                    player.id == randomTarget
                }
                if (targetPlayer != null) {
                    val (viewToClick, player) = targetPlayer
                    viewToClick.performClick()
                    viewToClick.setOnClickListener {
                        stopCpuProcess()
                        val bundle = Bundle()
                        bundle.putString("Name", player.name)
                        bundle.putString("userType", userType)
                        bundle.putString("Num", player.jersey_number)
                        bundle.putString("id", player.id)
                        Log.e("Send Detail of Quiz", userType + " " + player.name + " " + player.jersey_number)
                        Log.e("Send Detail of bundel"," ***** $bundle")
                        findNavController().navigate(R.id.player_name_guess, bundle)
                    }
                }
            }else{
                val countSelect = allUserPlayer.count { it.use.equals("true",true) }?:0
                if (countSelect==0){
                    stopCpuProcess()
                    cpuPassBall()
                }else{
                    val result = if (countAnswer % 2 == 0) {
                        "even"
                    } else {
                        "odd"
                    }
                    if (result.equals("even",true)){
                        val randomTarget =  (1..11).random().toString()
                        Log.d("Random number", "*****$randomTarget")
                        val targetPlayer = allUserPlayer.find {
                            it.id==randomTarget
                        }
                        if (targetPlayer != null) {
                            Log.d("@@@Error", "******$targetPlayer")
                            if (targetPlayer.use.equals("true",true)){
                                if (targetPlayer.answer.equals("false",true)){
                                    val bundle = Bundle()
                                    bundle.putString("Name", targetPlayer.name)
                                    bundle.putString("userType", userType)
                                    bundle.putString("Num", targetPlayer.jersey_number)
                                    bundle.putString("id", targetPlayer.id)
                                    Log.e("Send Detail of Quiz", userType + " " + targetPlayer.name + " " + targetPlayer.jersey_number)
                                    findNavController().navigate(R.id.player_name_guess, bundle)
                                    Toast.makeText(requireContext(), "Auto-selected MF #${targetPlayer.id}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        stopCpuProcess()
                        Toast.makeText(requireContext(),"Soot",Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
            object : OnBackPressedCallback(true  /*enabled by default*/ ) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    @SuppressLint("SetTextI18n")
    private fun setupFootballFormation(formationType: String, players: List<PlayerModel>) {
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
                    if (player.use.equals("true",true)){
                        rpSelect.visibility = View.VISIBLE
                    }else{
                        rpSelect.visibility = View.GONE
                    }
                    playerView.setOnClickListener {
                        val countAnswer = players.count { it.answer.equals("true",true) }?:0
                        if (countAnswer==0){
                            if (player.designation.equals("MF",true)){
                                val bundle = Bundle()
                                bundle.putString("Name", player.name)
                                bundle.putString("userType", userType)
                                bundle.putString("Num", player.jersey_number)
                                bundle.putString("id", player.id)
                                Log.e("Send Detail of Quiz", userType + " " + player.name + " " + player.jersey_number)
                                findNavController().navigate(R.id.player_name_guess, bundle)
                            }
                        }else{
                            if (player.use.equals("true",true)){
                                val bundle = Bundle()
                                bundle.putString("Name", player.name)
                                bundle.putString("userType", userType)
                                bundle.putString("Num", player.jersey_number)
                                bundle.putString("id", player.id)
                                Log.e("Send Detail of Quiz", userType + " " + player.name + " " + player.jersey_number)
                                findNavController().navigate(R.id.player_name_guess, bundle)
                            }
                        }
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

    @SuppressLint("SetTextI18n")
    private fun setupFootballFormationCpu(players1: String, players: List<PlayerModel>) {
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
                    binding.rootButton.visibility=View.GONE
                    val countAnswer = players.count { it.answer.equals("true",true) }?:0
                    if (countAnswer!=0){

                    }
                }else{
                    playerImage.setImageResource(R.drawable.user_guess_white)
                    rpSelect.visibility = View.GONE
                    rpName.visibility = View.GONE
                    rpNumber.visibility = View.GONE

                    if (player.use.equals("true",true)){
                        rpSelect.visibility = View.VISIBLE
                    }else{
                        rpSelect.visibility = View.GONE
                    }
                    clickablePlayers.add(Pair(playerView, player))
                    autoButtonClick()

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

    override fun onClick(item: View?) {
        when (item!!.id) {
            R.id.bt_pass -> {
                    userPassBall()
            }
            R.id.bt_shoot -> {
                    userShoot()
            }
        }
    }

    private fun userShoot(){
        val plarerId=sessionManager.getMySelectedTeamPlayerNum()
        val player = allCpuPlayer.find { it.id.toInt() == plarerId }
        if (!player?.designation.equals("GK",true)){
            val bundle = Bundle()
            bundle.putString("userType", "USER")
            bundle.putInt("selected_player_num", plarerId)
            val size = 0
            for (data in allCpuPlayer) {
                if (data.id.toInt() == plarerId) {
                    if (data.designation.uppercase() == "DF") {
                        bundle.putInt("size", size + 2 + myPass)
                    } else {
                        if (data.designation.uppercase() == "MF") {
                            bundle.putInt("size", size + 3 + myPass)
                        } else {
                            bundle.putInt("size", size + 4 + myPass)
                        }
                    }
                }
            }
            findNavController().navigate(R.id.shoot_Screen, bundle)
        }
    }

    private fun userPassBall() {
            if (userType.equals("USER",true)) {
                val plarerId=sessionManager.getMySelectedTeamPlayerNum()
                val player = allCpuPlayer.find { it.id.toInt() == plarerId }
                if (player != null) {
                    if (player.is_captain.equals("0", ignoreCase = true)) {
                        val targetMap = getTargetMap()
                        val targetIds = targetMap[plarerId] ?: emptySet()
                        allCpuPlayer = allCpuPlayer
                            .map { player ->
                                if (player.id.toInt()  in targetIds) {
                                    player.copy(use = "true")
                                } else {
                                    player.copy(use = "false")
                                }
                            }
                            .toCollection(ArrayList())  // ensures you get an ArrayList<PlayerModel>
                    } else {
                        allCpuPlayer = allCpuPlayer
                            .map { player ->
                                player.copy(use = "true")
                            }
                            .toCollection(ArrayList())
                    }
                    setupFootballFormation("3-2-5-1",allCpuPlayer)
                }
            }
    }

    private fun cpuPassBall() {
        val plarerId=sessionManager.getSelectedTeamPlayerNum()
        Log.d("@@@@Error", "****** plarerId$plarerId")
        val player = allUserPlayer.find { it.id.toInt() == plarerId }
        if (player != null) {
            if (player.is_captain.equals("0", ignoreCase = true)) {
                val targetMap = getTargetMap()
                val targetIds = targetMap[plarerId] ?: emptySet()
                allUserPlayer = allUserPlayer
                    .map { player ->
                        if (player.id.toInt()  in targetIds) {
                            player.copy(use = "true")
                        } else {
                            player.copy(use = "false")
                        }
                    }
                    .toCollection(ArrayList())  // ensures you get an ArrayList<PlayerModel>
            } else {
                allUserPlayer = allUserPlayer
                    .map { player ->
                        player.copy(use = "true")
                    }
                    .toCollection(ArrayList())
            }
            setupFootballFormationCpu("3-2-5-1",allUserPlayer)
        }
    }


    private fun getTargetMap(): Map<Int, Set<Int>> {
        val data = sessionManager.getUserScreenType()?.lowercase()

        val map523 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(1, 6, 3, 11),
            3 to setOf(2, 6, 7, 4, 11),
            4 to setOf(3, 5, 7, 11),
            5 to setOf(4, 7),
            6 to setOf(1, 2, 3, 7, 8, 9),
            7 to setOf(6, 3, 4, 5, 9, 10),
            8 to setOf(6, 9),
            9 to setOf(6, 7, 8, 10),
            10 to setOf(9, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map541 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(1, 3, 6, 11),
            3 to setOf(2, 4, 7, 8, 11),
            4 to setOf(3, 5, 8, 9, 11),
            5 to setOf(4, 9),
            6 to setOf(1, 2, 4),
            7 to setOf(10, 2, 3, 6, 8),
            8 to setOf(3, 4, 7, 9),
            9 to setOf(8, 5),
            10 to setOf(7, 8),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map532 = mapOf(
            1 to setOf(2, 6),
            2 to setOf(6, 3, 11),
            3 to setOf(2, 4, 6, 7, 8, 11),
            4 to setOf(3, 5, 7, 8, 11),
            5 to setOf(4, 8),
            6 to setOf(2, 3, 7, 9),
            7 to setOf(6, 8, 3, 2, 4),
            8 to setOf(7, 4, 5),
            9 to setOf(6, 7, 10),
            10 to setOf(9, 8, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map352 = mapOf(
            1 to setOf(2, 11, 4, 5, 6),
            2 to setOf(1, 3, 11, 5, 6, 7),
            3 to setOf(2, 11, 6, 7, 8),
            4 to setOf(1, 5),
            5 to setOf(1, 2, 4, 6, 9),
            6 to setOf(1, 2, 3, 5, 7, 9, 10),
            7 to setOf(6, 8, 2, 3, 10),
            8 to setOf(7, 3),
            9 to setOf(10, 5, 6),
            10 to setOf(9, 6, 7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map451 = mapOf(
            1 to setOf(2, 5, 6),
            2 to setOf(1, 11, 3, 5, 6),
            3 to setOf(2, 4, 11, 7, 8),
            4 to setOf(3, 8, 9),
            5 to setOf(1, 6),
            6 to setOf(1, 2, 5, 7),
            7 to setOf(6, 8, 2, 3, 10),
            8 to setOf(3, 4, 7, 9, 10),
            9 to setOf(8, 4),
            10 to setOf(6, 7, 8),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map442 = mapOf(
            1 to setOf(2, 5),
            2 to setOf(1, 3, 6, 11),
            3 to setOf(2, 4, 11, 7),
            4 to setOf(3, 7, 8),
            5 to setOf(1, 6),
            6 to setOf(5, 2, 7, 9),
            7 to setOf(6, 8, 3, 10),
            8 to setOf(4, 7),
            9 to setOf(7, 6, 10),
            10 to setOf(7, 8, 9),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map433 = mapOf(
            1 to setOf(2,3,11,5),
            2 to setOf(1, 3,11,5,6),
            3 to setOf(2,11,4,6,7),
            4 to setOf(3,7),
            5 to setOf(1,2,8),
            6 to setOf(5,7,2,3,9),
            7 to setOf(6,10,3,4),
            8 to setOf(5,9,6),
            9 to setOf(8,10,6),
            10 to setOf(9,7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        val map343 = mapOf(
            1 to setOf(2,5,11),
            2 to setOf(1, 3,11,5,6),
            3 to setOf(2,11,6,7),
            4 to setOf(1,5,8),
            5 to setOf(4,6,1,2),
            6 to setOf(5,7,2,3,9,10),
            7 to setOf(3,6,10),
            8 to setOf(4,5,9),
            9 to setOf(8,10,5,6),
            10 to setOf(9,6,7),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )
        val map424 = mapOf(
            1 to setOf(2,11),
            2 to setOf(1, 3,11,5),
            3 to setOf(2,11,6,4),
            4 to setOf(3,11),
            5 to setOf(2,6,8),
            6 to setOf(5,3,9),
            7 to setOf(8,5),
            8 to setOf(7,9,5),
            9 to setOf(8,10,6),
            10 to setOf(9,6),
            11 to setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        )

        return when (data) {
            "5-2-3" -> map523
            "5-4-1" -> map541
            "5-3-2" -> map532
            "3-5-2" -> map352
            "4-5-1" -> map451
            "4-4-2" -> map442
            "4-3-3" -> map433
            "3-4-3" -> map343
            "4-2-4" -> map424
            else -> emptyMap()  // return empty map if no match
        }
    }

    //This function is used for cpu button auto click (AI module)
    private fun autoButtonClick() {
        Log.e("CPU Button", "Auto Click")
        try {
            startCpuProcess()
        } catch (e: Exception) {
            Log.d("***** CPU Button", "Auto Click" + e.message)
        }
    }

    fun startCpuProcess() {
        isCpuActive = true
        handler.postDelayed(runnable, 3000) // Start delayed execution
    }

    fun stopCpuProcess() {
        isCpuActive = false
        handler.removeCallbacks(runnable) // Stop execution
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyAndOnStop()
        Log.e("@@@Error","Distro Timer Hold")
    }
    override fun onStop() {
        onDestroyAndOnStop()
        Log.e("@@@Error","Stop TimerHold")
        super.onStop()
    }

    private  fun onDestroyAndOnStop(){
        sessionManager.saveTimer(startTime)
        stopCpuProcess()
    }
}