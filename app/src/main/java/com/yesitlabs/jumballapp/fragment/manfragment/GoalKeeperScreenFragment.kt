package com.yesitlabs.jumballapp.fragment.manfragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.ExtraPlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerDatabaseHelper
import com.yesitlabs.jumballapp.database.player_dtl.PlayerModel
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
 
import com.yesitlabs.jumballapp.databinding.FragmentGoalkeaperBinding
import com.yesitlabs.jumballapp.gameRule.SetGames
import com.yesitlabs.jumballapp.network.viewModel.GetGuessPlayerListViewModel

import java.util.Locale
import kotlin.random.Random


class GoalKeeperScreenFragment : Fragment(), View.OnClickListener {

    var size: Int = 2
    private var userType: String = "USER"

    private var selectionPower = false

    private var selectBox = 0
    private var selectedPlayerNum = 0

    lateinit var sessionManager: SessionManager
    private lateinit var getGuessPlayerListViewmodel: GetGuessPlayerListViewModel
    var token: String? = null
    private var setGames: SetGames = SetGames()

    private lateinit var teamDbHelper: TeamDatabaseHelper
    private lateinit var extraPLayerDbHelper: ExtraPlayerDatabaseHelper
    private lateinit var cpuDbHelper: CPUPlayerDatabaseHelper
    private lateinit var myPlayerDbHelper: PlayerDatabaseHelper

    private var allCpuPlayer = ArrayList<PlayerModel>()
    private var allUserPlayer = ArrayList<PlayerModel>()


    private lateinit var binding: FragmentGoalkeaperBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGoalkeaperBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sessionManager = SessionManager(requireContext())
        getGuessPlayerListViewmodel = ViewModelProvider(this)[GetGuessPlayerListViewModel::class.java]


        teamDbHelper = TeamDatabaseHelper(requireContext())
        cpuDbHelper = CPUPlayerDatabaseHelper(requireContext())
        myPlayerDbHelper = PlayerDatabaseHelper(requireContext())
        extraPLayerDbHelper = ExtraPlayerDatabaseHelper(requireContext())

        allCpuPlayer = cpuDbHelper.getAllPlayers()
        allUserPlayer = myPlayerDbHelper.getAllPlayers()

        token = "Bearer " + sessionManager.fetchAuthToken()


        Log.d("******","selectBox not assigne value :="+selectBox)


        size = requireArguments().getInt("size")
        selectBox = requireArguments().getInt("select_box")


        Log.d("******","size :- ="+size)
        Log.d("******","selectBox assigne value :="+selectBox)





        userType = requireArguments().getString("userType").toString().uppercase(Locale.ROOT)
        selectedPlayerNum = requireArguments().getInt("selected_player_num")

        Log.e("Goalkeeper argument", requireArguments().toString())
        sessionManager.saveMyPass(0)
        sessionManager.saveCpuPass(0)

        setStructure(size)

        if (userType == "USER") {
            binding.r1g1.setOnClickListener(this)
            binding.r1g2.setOnClickListener(this)
            binding.r1g3.setOnClickListener(this)
            binding.r1g4.setOnClickListener(this)
            binding.r21g1.setOnClickListener(this)
            binding.r22g1.setOnClickListener(this)
            binding.r22g2.setOnClickListener(this)
            binding.r23g1.setOnClickListener(this)
            binding.r23g2.setOnClickListener(this)
            binding.r23g3.setOnClickListener(this)
            binding.r24g1.setOnClickListener(this)
            binding.r24g2.setOnClickListener(this)
            binding.r24g3.setOnClickListener(this)
            binding.r24g4.setOnClickListener(this)
        }

    }


    // This is used for set goal screen structure
    private fun setStructure(size: Int) {

        when (size) {
            2 -> {
                binding.r.weightSum = 1f
                binding.r2.visibility = View.GONE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img_right)
                binding.r1g3.visibility = View.GONE
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 2f
            }

            3 -> {
                binding.r2.visibility = View.GONE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g4.visibility = View.GONE
                binding.r1.weightSum = 3f
                binding.r.weightSum = 1f
            }

            4 -> {
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r2.visibility = View.GONE
                binding.r1.weightSum = 4f
                binding.r.weightSum = 1f
            }

            5 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.VISIBLE
                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r21g11.setImageResource(R.drawable.shoot_img)

                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f
            }

            6 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.VISIBLE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f

                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding. r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r22g11.setImageResource(R.drawable.shoot_img)
                binding.r22g22.setImageResource(R.drawable.shoot_img_right)


            }

            7 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.VISIBLE
                binding.r24.visibility = View.GONE
                binding.r.weightSum = 2f


                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r23g11.setImageResource(R.drawable.shoot_img)
                binding.r23g22.setImageResource(R.drawable.shoot_img)
                binding.r23g33.setImageResource(R.drawable.shoot_img_right)

            }

            8 -> {
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
                binding.r.weightSum = 2f


                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r24g11.setImageResource(R.drawable.shoot_img)
                binding.r24g22.setImageResource(R.drawable.shoot_img)
                binding.r24g33.setImageResource(R.drawable.shoot_img_right)
                binding.r24g44.setImageResource(R.drawable.shoot_img_right)


            }

            else -> {
               /* r1.weightSum = 4f
                r2.visibility = View.GONE
                r1g3.visibility = View.GONE
                r1g4.visibility = View.GONE
                r1.weightSum = 2f
                r.weightSum = 1f*/
                binding.r1.weightSum = 4f
                binding.r2.visibility = View.VISIBLE
                binding.r21.visibility = View.GONE
                binding.r22.visibility = View.GONE
                binding.r23.visibility = View.GONE
                binding.r24.visibility = View.VISIBLE
                binding. r.weightSum = 2f


                binding.r1g11.setImageResource(R.drawable.shoot_img)
                binding.r1g22.setImageResource(R.drawable.shoot_img)
                binding.r1g33.setImageResource(R.drawable.shoot_img_right)
                binding.r1g44.setImageResource(R.drawable.shoot_img_right)
                binding.r24g11.setImageResource(R.drawable.shoot_img)
                binding.r24g22.setImageResource(R.drawable.shoot_img)
                binding.r24g33.setImageResource(R.drawable.shoot_img_right)
                binding.r24g44.setImageResource(R.drawable.shoot_img_right)
            }

        }

        setSelectColor()

    }

    // This is used for reset  box color
    private fun setSelectColor() {

        binding.r1g1Select.visibility = View.GONE
        binding.r1g2Select.visibility = View.GONE
        binding.r1g3Select.visibility = View.GONE
        binding.r1g4Select.visibility = View.GONE

        binding.r21g1Select.visibility = View.GONE

        binding.r22g1Select.visibility = View.GONE
        binding.r22g2Select.visibility = View.GONE

        binding.r23g1Select.visibility = View.GONE
        binding.r23g2Select.visibility = View.GONE
        binding.r23g3Select.visibility = View.GONE

        binding.r24g1Select.visibility = View.GONE
        binding.r24g2Select.visibility = View.GONE
        binding.r24g3Select.visibility = View.GONE
        binding.r24g4Select.visibility = View.GONE

    }

    override fun onClick(view: View?) {

      sessionManager.playClickSound()

        if (view != null) {
            when (view.id) {

                R.id.r1g1 -> {
                    selectKickGoalBox(1, binding.r1g1Select)
                }

                R.id.r1g2 -> {
                    selectKickGoalBox(2, binding.r1g2Select)
                }

                R.id.r1g3 -> {
                    selectKickGoalBox(3, binding.r1g3Select)
                }

                R.id.r1g4 -> {
                    selectKickGoalBox(4, binding.r1g4Select)
                }

                R.id.r21g1 -> {
                    selectKickGoalBox(5, binding.r21g1Select)
                }

                R.id.r22g1 -> {
                    selectKickGoalBox(5, binding.r22g1Select)
                }

                R.id.r22g2 -> {
                    selectKickGoalBox(6, binding.r22g2Select)
                }

                R.id.r23g1 -> {
                    selectKickGoalBox(5, binding.r23g1Select)
                }

                R.id.r23g2 -> {
                    selectKickGoalBox(6, binding.r23g2Select)
                }

                R.id.r23g3 -> {
                    selectKickGoalBox(7, binding.r23g3Select)
                }

                R.id.r24g1 -> {
                    selectKickGoalBox(5, binding.r24g1Select)
                }

                R.id.r24g2 -> {
                    selectKickGoalBox(6, binding.r24g2Select)
                }

                R.id.r24g3 -> {
                    selectKickGoalBox(7, binding.r24g3Select)
                }

                R.id.r24g4 -> {
                    selectKickGoalBox(8, binding.r24g4Select)
                }


            }
        }
    }

    // This is used for cpu select box working [Auto]
    private fun selectKickGoalBox(selectBoxValue: Int, r1g1Select: View?) {
        if (!selectionPower) {
            r1g1Select?.visibility = View.VISIBLE
            selectionPower = true

            Log.d("******", "select user :- $selectBoxValue")
            Handler(Looper.myLooper()!!).postDelayed({
                /*if (selectBoxValue == selectBox) {
                    goalFailed()
                } else {
                    goalSuccessfully()
                }*/

                if (selectBoxValue == selectBox) {
                    goalSuccessfully()
                } else {
                    goalFailed()
                }


            }, 3000)
        }
    }

    // This is used for goal failed
    private fun goalFailed() {



        if (userType == "USER") {
            sessionManager.setFirstGamgeStartUser(true)
            sessionManager.setFirstGamgeStartCPU(false)
        } else {
            sessionManager.setFirstGamgeStartCPU(true)
            sessionManager.setFirstGamgeStartUser(false)
        }

        val screen = setGames.setScreen(sessionManager.getUserScreenType())

        val cpuScreen = setGames.setScreen(
            sessionManager.getCpuScreenType()
        )

        if(sessionManager.isNetworkAvailable()){
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()

            getGuessTeamList(
                false,
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )
        }else{
            alertError( getString(R.string.no_internet))
        }
    }

    // This is used for goal successful
    private fun goalSuccessfully() {

        sessionManager.saveTotalDefence(sessionManager.getTotalDefence()+1)

        if (userType == "USER") {
            sessionManager.setFirstGamgeStartUser(true)
            sessionManager.setFirstGamgeStartCPU(false)
        } else {
            sessionManager.setFirstGamgeStartCPU(true)
            sessionManager.setFirstGamgeStartUser(false)
        }


        sessionManager.saveSelectedTeamPlayerNum(
            selectedPlayerNum
        )

        if (sessionManager.getMatchType() == "worldcup"){
            teamDbHelper.updateA(1,1)
        }

        sessionManager.putCpuScore(1)


        val screen = setGames.setScreen(sessionManager.getUserScreenType())

        val cpuScreen = setGames.setScreen(sessionManager.getCpuScreenType())

        if(sessionManager.isNetworkAvailable()){
            cpuDbHelper.deleteAllPlayers()
            myPlayerDbHelper.deleteAllPlayers()
            extraPLayerDbHelper.deleteAllPlayers()

            // every condition change all the player play
            /*getGuessTeamList(
                true,
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )*/


            getGuessTeamList(
                true,
                screen.r1.toString(),
                screen.r2.toString(),
                screen.r3.toString(),
                cpuScreen.r1.toString(),
                cpuScreen.r2.toString(),
                cpuScreen.r3.toString()
            )
        }else{
            alertError(getString(R.string.no_internet))
        }

    }

    // This is used for show alert box of goal is success or fail
    @SuppressLint("SuspiciousIndentation")
    private fun playAlertBox(drawableImg : Int, player : String) {
        val dialog = Dialog(requireContext(), R.style.myFullscreenAlertDialogStyle)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.show_image_box)
        val imgChange: ImageView =dialog.findViewById(R.id.img_change)
        imgChange.setImageResource(drawableImg)

        Handler(Looper.myLooper()!!).postDelayed({
            sessionManager.increaseTimer(120000)
            val bundle = Bundle()
//            if (userType == "USER") {
//                bundle.putString("userType", "CPU")
//            } else {
                bundle.putString("userType", userType)
//            }
            sessionManager.changeMusic(1,1)
//            findNavController().navigate(R.id.action_goalKeaperFragment_to_playScreenFragment, bundle)
            findNavController().navigate(R.id.playScreenFragment, bundle)
        },2000)

        Handler(Looper.myLooper()!!).postDelayed({
            dialog.dismiss()
        }, 3500)

        dialog.show()
    }

    // This function is used for get guess player list from database api
    private fun getGuessTeamList(
        win : Boolean,
        defender: String,
        midfielder: String,
        attacker: String,
        cpuDefender: String,
        cpuMidFielder: String,
        cpuAttacker: String
    ) {

        getGuessPlayerListViewmodel.getGuessPlayerListResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {


                if (response.isSuccessful ) {
                    val teamResponse = response.body()

                    if (teamResponse != null)
                    {
                        if (teamResponse.data != null){

                            if (teamResponse.data.myplayer != null) {
                                var df = defender.toInt()
                                var mf = midfielder.toInt()
                                var fw = attacker.toInt()
                                Log.e("Player", myPlayerDbHelper.getAllPlayers().size.toString())
                                try {
                                    // Defender
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "DF") {
                                            if (df > 0) {
                                                df -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",

                                                )
                                            }
                                        }
                                    }
                                    // MidFielder
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "MF") {
                                            if (mf > 0) {
                                                mf -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    //Striker
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setMyPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "FW") {
                                            if (fw > 0) {
                                                fw -= 1
                                                myPlayerDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }

                                    // Goalkeeper
                                    //Shrawan
                                    for (data in teamResponse.data.myplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }

                                        if (data.designation == "GK") {
                                            myPlayerDbHelper.addPlayer(
                                                surnames,
                                                data.is_captain.toString(),
                                                data.country_id.toString(),
                                                data.type.toString(),
                                                data.designation.toString(),
                                                data.jersey_number.toString(),
                                                "false",
                                                "false"
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }

                            if (teamResponse.data.cpuplayer != null) {
                                var df = cpuDefender.toInt()
                                var mf = cpuMidFielder.toInt()
                                var fw = cpuAttacker.toInt()
                                try {
                                    // Defender
                                    for (data in teamResponse.data.cpuplayer) {

                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "DF") {
                                            if (df > 0) {
                                                df -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }
                                    // MidFielder
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "MF") {
                                            if (mf > 0) {
                                                mf -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }

                                    //Striker
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1)
                                        {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        if (data.designation == "FW") {
                                            if (fw > 0) {
                                                fw -= 1
                                                cpuDbHelper.addPlayer(
                                                    surnames,
                                                    data.is_captain.toString(),
                                                    data.country_id.toString(),
                                                    data.type.toString(),
                                                    data.designation.toString(),
                                                    data.jersey_number.toString(),
                                                    "false",
                                                    "false",
                                                )
                                            }
                                        }
                                    }

                                    // Goalkeeper
                                    //Shrawan
                                    for (data in teamResponse.data.cpuplayer) {
                                        if (data.is_captain == 1) {
                                            sessionManager.setCpuPlayerId(data.id)
                                        }
                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }

                                        if (data.designation == "GK") {
                                            cpuDbHelper.addPlayer(
                                                surnames,
                                                data.is_captain.toString(),
                                                data.country_id.toString(),
                                                data.type.toString(),
                                                data.designation.toString(),
                                                data.jersey_number.toString(),
                                                "false",
                                                "false"
                                            )
                                        }
                                    }

                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }

                            if (teamResponse.data.SubtitutePlyer != null) {
                                try {
                                    // Extra Player
                                    for (data in teamResponse.data.SubtitutePlyer) {

                                        val surnames = try {
                                            data.name!!.split(" ").last()
                                        } catch (e: Exception) {
                                            "SYSTEM"
                                        }
                                        extraPLayerDbHelper.addPlayer(
                                            surnames,
                                            data.is_captain.toString(),
                                            data.country_id.toString(),
                                            data.type.toString(),
                                            data.designation.toString(),
                                            data.jersey_number.toString(),
                                            "false",
                                            "USER"
                                        )
                                    }
                                } catch (e: Exception) {
                                    Log.e("My Player Database Error", e.toString())
                                }
                            }
                            checkAllPlayer(win)
                        }else{
                            sessionManager.alertError( "Player list not found !")
                        }
                    }else{
                        sessionManager.alertError( response.message().toString())
                    }
                } else {
                    sessionManager.alertError( response.errorBody().toString())
                }
            } else {
                sessionManager.alertError( "Check Your Internet Connection")
            }
        }
        val match_no = (sessionManager.getGameNumber()-1)
        getGuessPlayerListViewmodel.getGuessPlayerList("$token", defender, midfielder,
            attacker, "", "",match_no.toString())

    }

    // This function is used for check cpu and user team player list and verify
    private fun checkAllPlayer(win :Boolean) {

//        Toast.makeText(requireContext(), "win status :-$win",Toast.LENGTH_SHORT).show()

        if (myPlayerDbHelper.getAllPlayers().size < 10) {

            val remain = 10 - myPlayerDbHelper.getAllPlayers().size

            if (remain > 1) {
                for (i in 0 until remain) {
                    myPlayerDbHelper.addPlayer("SYSTEM", "0", "ENG", "no", "MF", "10", "false", "false")
                }
            } else {
                myPlayerDbHelper.addPlayer("SYSTEM", "0", "ENG", "no", "MF", "10", "false", "false")
            }

        }

        if (cpuDbHelper.getAllPlayers().size < 10) {

            val remain = 10 - cpuDbHelper.getAllPlayers().size

            if (remain > 1) {
                for (i in 0 until remain) {
                    cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
                }
            } else {
                cpuDbHelper.addPlayer("ANDROID", "0", "ENG", "no", "MF", "10", "false", "false")
            }

        }

        Log.e("My Team :", myPlayerDbHelper.getAllPlayers().toString())
        Log.e("My Team Size :", myPlayerDbHelper.getAllPlayers().size.toString())
        Log.e("CPU Team :", cpuDbHelper.getAllPlayers().toString())
        Log.e("CPU Team Size :", cpuDbHelper.getAllPlayers().size.toString())
        Log.e("Extra Team :", extraPLayerDbHelper.getAllPlayers().toString())
        Log.e("Extra Team Size :", extraPLayerDbHelper.getAllPlayers().size.toString())


        if (win){
            playAlertBox( R.drawable.goal_alert,"CPU")
        }else{
            when (Random.nextInt(1, /*10*/6)) {
                1 -> {
                    playAlertBox(R.drawable.saved_img, "CPU")
                }

                2 -> {
                    playAlertBox(R.drawable.wide, "CPU")
                }

                3 -> {
                    sessionManager.changeMusic(22, 0)
                    playAlertBox(R.drawable.clearance_img, "CPU")
                }

                4 -> {
                    playAlertBox(R.drawable.crossbar_img, "CPU")
                }

                5 -> {
                    sessionManager.changeMusic(21, 0)
                    playAlertBox(R.drawable.post, "CPU")
                }
                6 -> {
                    sessionManager.changeMusic(21, 0)
                    playAlertBox(R.drawable.over_img, "CPU")
                }

                /* else -> {
                     sessionManager.changeMusic(22, 0)
                     playAlertBox(R.drawable.over_img, "CPU")
                 }*/

            }

//            playAlertBox( R.drawable.saved_img,"USER")
        }



    }


    @SuppressLint("SetTextI18n")
    fun  alertError(msg:String){
        val dialog= Dialog(requireContext(), R.style.BottomSheetDialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alertbox_error)



        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
        val tvTitle: TextView =dialog.findViewById(R.id.tv_title)
        val btnOk: LinearLayout =dialog.findViewById(R.id.btn_ok)
        val btText: TextView =dialog.findViewById(R.id.btText)
        btText.text = "Retry"
        tvTitle.text=msg
        btnOk.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
            goalFailed()
        })
        dialog.show()
    }

}