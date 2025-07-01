package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.FragmentStatisticsBinding
import com.yesitlabs.jumballapp.model.ScoreBoardResp
import com.yesitlabs.jumballapp.network.NetworkResult
import com.yesitlabs.jumballapp.viewmodeljumball.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : Fragment() {


    private lateinit var viewModel: StatisticsViewModel
    lateinit var sessionManager: SessionManager
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(requireActivity())[StatisticsViewModel::class.java]
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        if (sessionManager.getCheckLogin().equals("yes",true)) {
            if (sessionManager.isNetworkAvailable()) {
                getScoreBoard()
            } else {
                sessionManager.alertError( getString(R.string.no_internet))
            }
        }
    }


    // This function is used for get and set the scoreboard the screen from server
    @SuppressLint("SetTextI18n")
    private fun getScoreBoard() {
        sessionManager.showMe(requireContext())
        lifecycleScope.launch {
            viewModel.statisticsRequest {
                sessionManager.dismissMe()
                when (it) {
                    is NetworkResult.Success -> {
                        try {
                            val gson = Gson()
                            val model = gson.fromJson(it.data, ScoreBoardResp::class.java)
                            if (model.code == 200 && model.success) {
                                try {
                                    if (model.data.getTotalMatch != null) {
                                        binding.tvTotalPlayGame.text = model.data.getTotalMatch.toString()
                                    }
                                    if (model.data.totalGoal.totalGoalSum != null) {
                                        binding.tvGoalScored.text = model.data.totalGoal.totalGoalSum.toString()
                                    }
                                    if (model.data.total_goal_console?.total_goal_console != null) {
                                        binding.tvGoalConceded.text = model.data.total_goal_console?.total_goal_console.toString()
                                    }
                                    if (model.data.winPercent != null) {
                                        binding.tvWinPersantage.text = model.data.winPercent.toString()
                                    }
                                    if (model.data.totalPoint != null) {
                                        binding.tvTotalPoint.text = model.data.totalPoint.toString()
                                    }
                                    if (model.data.getUserWonWorldCup?.total_won != null) {
                                        binding.tvWorldCupWon.text = model.data.getUserWonWorldCup?.total_won.toString()
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
    }


}