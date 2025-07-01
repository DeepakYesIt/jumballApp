package com.yesitlabs.jumballapp.fragment.manfragment

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.databinding.FragmentPlayerUserCPUBinding


class PlayerUserCPUFragment : Fragment() {
    private lateinit var binding: FragmentPlayerUserCPUBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPlayerUserCPUBinding.inflate(inflater, container, false)

        val team = listOf(
            Player("Alex Martinez", 1, "Goalkeeper", "Starter"),
            // 3 Defenders
            Player("Ryan Blake", 2, "Defender", "Starter"),
            Player("Jamal Okafor", 3, "Defender", "Starter"),
            Player("Luca Fernandez", 4, "Defender", "Starter"),
            // 2 Midfielders
            Player("Kevin Liu", 6, "Midfielder", "Starter"),
            Player("Daniel Costa", 7, "Midfielder", "Starter"),

            // 5 Attackers (Wingers/Forwards)
            Player("Carlos Mendes", 9, "Attacker", "Starter"),
            Player("Jordan Knox", 10, "Attacker", "Starter"),
            Player("Ahmed Salah", 11, "Attacker", "Starter"),
            Player("Leo Anders", 8, "Attacker", "Starter"),
            Player("Samir Patel", 14, "Attacker", "Starter") // Central striker
        )
        setupFootballFormation("3-2-5-1",team)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupFootballFormation(players1: String, players: List<Player>) {
        binding.formationContainer.removeAllViews()

        // Group players by position

        val groupedPlayers = listOf(
            "Defender" to players.filter { it.position == "Defender" },
            "Midfielder" to players.filter { it.position == "Midfielder" },
            "Attacker" to players.filter { it.position == "Attacker" },
            "Goalkeeper" to players.filter { it.position == "Goalkeeper" },
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
                if (player.showStatus){
                    rpSelect.visibility = View.VISIBLE
                }else{
                    rpSelect.visibility = View.GONE
                    playerView.setOnClickListener {
                        if (player.position.equals("Goalkeeper",true)){
                            players.forEach {
                                it.showStatus=true
                            }
                            // Refresh with filter for that position only
                            setupFootballFormation("", players)
                        }else{
                            val playerIndex = players.indexOfFirst { it.number == player.number  }
                            if (playerIndex != -1) {
                                players[playerIndex].showStatus = true
                                // Refresh with filter for that position only
                                setupFootballFormation("", players)
                            }
                        }
                    }
                }

                rpName.text = player.name
                rpNumber.text = "${player.number}"

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

}