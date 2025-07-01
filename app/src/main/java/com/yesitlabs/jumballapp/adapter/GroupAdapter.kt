package com.yesitlabs.jumballapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yesitlabs.jumballapp.databinding.ItemGroupBinding
import com.yesitlabs.jumballapp.model.teamListModel.TeamListModel

class GroupAdapter(
    private var dataList: ArrayList<TeamListModel>,
    var requireActivity: FragmentActivity
) : RecyclerView.Adapter<GroupAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    class Holder(private val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: TeamListModel) {
            binding.tvSn.text = "GROUP ${team.teamsn}"

            binding.name1.text = team.captainName[0]
            binding.name2.text = team.captainName[1]
            binding.name3.text = team.captainName[2]
            binding.name4.text = team.captainName[3]

            binding.pldTxt1.text = team.PLD[0].toString()
            binding.pldTxt2.text = team.PLD[1].toString()
            binding.pldTxt3.text = team.PLD[2].toString()
            binding.pldTxt4.text = team.PLD[3].toString()

            binding.W1.text = team.W[0].toString()
            binding.W2.text = team.W[1].toString()
            binding.W3.text = team.W[2].toString()
            binding.W4.text = team.W[3].toString()

            binding.D1.text = team.D[0].toString()
            binding.D2.text = team.D[1].toString()
            binding.D3.text = team.D[2].toString()
            binding.D4.text = team.D[3].toString()

            binding.L1.text = team.L[0].toString()
            binding.L2.text = team.L[1].toString()
            binding.L3.text = team.L[2].toString()
            binding.L4.text = team.L[3].toString()

            binding.F1.text = team.F[0].toString()
            binding.F2.text = team.F[1].toString()
            binding.F3.text = team.F[2].toString()
            binding.F4.text = team.F[3].toString()

            binding.A1.text = team.A[0].toString()
            binding.A2.text = team.A[1].toString()
            binding.A3.text = team.A[2].toString()
            binding.A4.text = team.A[3].toString()

            binding.GD1.text = team.GD[0].toString()
            binding.GD2.text = team.GD[1].toString()
            binding.GD3.text = team.GD[2].toString()
            binding.GD4.text = team.GD[3].toString()

            binding.PTS1.text = team.PTS[0].toString()
            binding.PTS2.text = team.PTS[1].toString()
            binding.PTS3.text = team.PTS[2].toString()
            binding.PTS4.text = team.PTS[3].toString()
        }
    }
}
