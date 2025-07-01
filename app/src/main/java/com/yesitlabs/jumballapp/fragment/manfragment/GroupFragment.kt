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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.adapter.GroupAdapter
import com.yesitlabs.jumballapp.database.team_dtl.TeamDatabaseHelper
import com.yesitlabs.jumballapp.database.team_dtl.TeamModel
import com.yesitlabs.jumballapp.databinding.FragmentGroupBinding
import com.yesitlabs.jumballapp.errormassage.ErrorMessage
import com.yesitlabs.jumballapp.model.GetGroupDetailResp
import com.yesitlabs.jumballapp.model.teamListModel.TeamListModel
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewModel.gameapi.stickerViewModel.cericatureResp
import com.yesitlabs.jumballapp.viewmodeljumball.CaricatureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GroupFragment : Fragment() {

    private var teamListModel: ArrayList<TeamListModel> = arrayListOf()
    private lateinit var teamDbHelper: TeamDatabaseHelper
    lateinit var sessionManager: SessionManager
    private lateinit var viewModel: CaricatureViewModel
    private lateinit var binding: FragmentGroupBinding
    var is_first = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        sessionManager.changeMusic(1, 1)
        viewModel = ViewModelProvider(requireActivity())[CaricatureViewModel::class.java]
        teamDbHelper = TeamDatabaseHelper(requireContext())
        teamDbHelper.deleteAllTeams()
        Log.d("@Error","number :-  "+sessionManager.getGameNumber())
        val gameCount = (sessionManager.getGameNumber()-1)
        is_first = if (gameCount <= 3 && gameCount != 0){
            "1"
        }else{
            "0"
        }
        if(sessionManager.isNetworkAvailable()){
            getTeamFromDatabase()
        }else{
            Toast.makeText(requireContext(), ErrorMessage.netWorkError, Toast.LENGTH_SHORT).show()
        }

    }
    private fun getTeamFromDatabase() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.getTeam({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, GetGroupDetailResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                     model.data?.let { data->
                                         if (data.size>0){
                                             for (i in 0 until data.size) {
                                                 teamDbHelper.addTeam(TeamModel(i,
                                                     data[i].name, "Demo", 1,
                                                     data[i].totalgame,
                                                     data[i].totalWin,
                                                     data[i].total_defence,
                                                     data[i].totalLoss,
                                                     data[i].total_goal,
                                                     data[i].total_goal_console,
                                                 )
                                                 )
                                             }
                                             setDataFromDataBase()
                                         }
                                     }
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
            }, is_first)
        }
    }

    // This is used for set team list in database
    private fun setDataFromDataBase() {
        val allTeam = teamDbHelper.getAllTeams()
        val teamID: ArrayList<Int> = ArrayList()
        val captainName: ArrayList<String> = ArrayList()
        val country: ArrayList<String> = ArrayList()
        val enable: ArrayList<Int> = ArrayList()
        val pld: ArrayList<Int> = ArrayList()
        val w: ArrayList<Int> = ArrayList()
        val d: ArrayList<Int> = ArrayList()
        val l: ArrayList<Int> = ArrayList()
        val f: ArrayList<Int> = ArrayList()
        val a: ArrayList<Int> = ArrayList()
        val gd: ArrayList<Int> = ArrayList()
        val pts: ArrayList<Int> = ArrayList()
        val teamID1: ArrayList<Int> = ArrayList()
        val captainName1: ArrayList<String> = ArrayList()
        val country1: ArrayList<String> = ArrayList()
        val enable1: ArrayList<Int> = ArrayList()
        val pld1: ArrayList<Int> = ArrayList()
        val w1: ArrayList<Int> = ArrayList()
        val d1: ArrayList<Int> = ArrayList()
        val l1: ArrayList<Int> = ArrayList()
        val f1: ArrayList<Int> = ArrayList()
        val a1: ArrayList<Int> = ArrayList()
        val gd1: ArrayList<Int> = ArrayList()
        val pts1: ArrayList<Int> = ArrayList()
        val teamID2: ArrayList<Int> = ArrayList()
        val captainName2: ArrayList<String> = ArrayList()
        val country2: ArrayList<String> = ArrayList()
        val enable2: ArrayList<Int> = ArrayList()
        val pld2: ArrayList<Int> = ArrayList()
        val w2: ArrayList<Int> = ArrayList()
        val d2: ArrayList<Int> = ArrayList()
        val l2: ArrayList<Int> = ArrayList()
        val f2: ArrayList<Int> = ArrayList()
        val a2: ArrayList<Int> = ArrayList()
        val gd2: ArrayList<Int> = ArrayList()
        val pts2: ArrayList<Int> = ArrayList()
        val teamID3: ArrayList<Int> = ArrayList()
        val captainName3: ArrayList<String> = ArrayList()
        val country3: ArrayList<String> = ArrayList()
        val enable3: ArrayList<Int> = ArrayList()
        val pld3: ArrayList<Int> = ArrayList()
        val w3: ArrayList<Int> = ArrayList()
        val d3: ArrayList<Int> = ArrayList()
        val l3: ArrayList<Int> = ArrayList()
        val f3: ArrayList<Int> = ArrayList()
        val a3: ArrayList<Int> = ArrayList()
        val gd3: ArrayList<Int> = ArrayList()
        val pts3: ArrayList<Int> = ArrayList()
        for (i in 0..3) {
            val data = allTeam[i]
            teamID.add(data.teamID)
            captainName.add(data.captainName)
            country.add(data.country)
            enable.add(data.enable)
            pld.add(data.PLD)
            w.add(data.W)
            d.add(data.D)
            l.add(data.L)
            f.add(data.F)
            a.add(data.A)
            gd.add(data.F - data.A)
            pts.add(data.PLD + data.W + data.D + data.L + data.F + data.A)
        }
        teamListModel.add(TeamListModel("A",teamID, captainName,
                country,
                enable,
                pld,
                w,
                d,
                l,
                f,
                a,
                gd,
                pts
            )
        )
        for (i in 4..7) {
            val data = allTeam[i]
            teamID1.add(data.teamID)
            captainName1.add(data.captainName)
            country1.add(data.country)
            enable1.add(data.enable)
            pld1.add(data.PLD)
            w1.add(data.W)
            d1.add(data.D)
            l1.add(data.L)
            f1.add(data.F)
            a1.add(data.A)
            gd1.add(data.F - data.A)
            pts1.add(data.PLD + data.W + data.D + data.L + data.F + data.A)
        }
        teamListModel.add(
            TeamListModel("B",
                teamID1,
                captainName1,
                country1,
                enable1,
                pld1,
                w1,
                d1,
                l1,
                f1,
                a1,
                gd1,
                pts1
            )
        )
        for (i in 8..11) {
            val data = allTeam[i]
            teamID2.add(data.teamID)
            captainName2.add(data.captainName)
            country2.add(data.country)
            enable2.add(data.enable)
            pld2.add(data.PLD)
            w2.add(data.W)
            d2.add(data.D)
            l2.add(data.L)
            f2.add(data.F)
            a2.add(data.A)
            gd2.add(data.F - data.A)
            pts2.add(data.PLD + data.W + data.D + data.L + data.F + data.A)
        }
        teamListModel.add(
            TeamListModel("C",
                teamID2,
                captainName2,
                country2,
                enable2,
                pld2,
                w2,
                d2,
                l2,
                f2,
                a2,
                gd2,
                pts,
            )
        )
        for (i in 12..15) {
            val data = allTeam[i]
            teamID3.add(data.teamID)
            captainName3.add(data.captainName)
            country3.add(data.country)
            enable3.add(data.enable)
            pld3.add(data.PLD)
            w3.add(data.W)
            d3.add(data.D)
            l3.add(data.L)
            f3.add(data.F)
            a3.add(data.A)
            gd3.add(data.F - data.A)
            pts3.add(data.PLD + data.W + data.D + data.L + data.F + data.A)
        }
        teamListModel.add(
            TeamListModel("D",
                teamID3,
                captainName3,
                country3,
                enable3,
                pld3,
                w3,
                d3,
                l3,
                f3,
                a3,
                gd3,
                pts3
            )
        )
        binding.rcyGroup.adapter = GroupAdapter(teamListModel, requireActivity())
        binding.rcyGroup.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.btnNext.setOnClickListener{
            if (sessionManager.isNetworkAvailable()) {
                selectTeam()
            } else {
               Toast.makeText(requireContext(),ErrorMessage.netWorkError,Toast.LENGTH_SHORT).show()
            }

        }
    }

    // This is used for select cpu team auto
    private fun selectTeam() {
        var selectTeam = 0
        for (i in 1 until teamDbHelper.getAllTeams().size) {
            if (teamDbHelper.getAllTeams()[i].enable != 1) {
                selectTeam = i + 1
                break
            }
        }
        if (selectTeam != 0) {
            sessionManager.saveTeamDetails(selectTeam)
            teamDbHelper.update_team_Enabling(sessionManager.getTeamDetails(), 1)
            sessionManager.resetScore()
            Log.e("Open Match Day Screen", "DONE")
            getCaricature()
        } else {
            for (i in 1 until teamDbHelper.getAllTeams().size) {
                teamDbHelper.update_team_Enabling(i + 1, 0)
            }
            selectTeam()
        }
    }

    // This is used for get caricature image from database
    private fun getCaricature() {
        sessionManager.showMe(requireContext())
        val matchNo = sessionManager.getGameNumber()-1
        lifecycleScope.launch {
            viewModel.getCaricature({
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, cericatureResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    val bundle = Bundle()
                                    bundle.putString("creatureImg", model.data?.get(0)?.image)
                                    findNavController().navigate(R.id.matchdayScreenFragment, bundle)
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
            }, matchNo.toString() )
        }
    }
}