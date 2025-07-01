package com.yesitlabs.jumballapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yesitlabs.jumballapp.databinding.ItemOngroupBinding
import com.yesitlabs.jumballapp.model.teamListModel.TeamListModel

class OnGroupAdapter(private val dataList: ArrayList<TeamListModel>) :
    RecyclerView.Adapter<OnGroupAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOngroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    class VH(private val binding: ItemOngroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: TeamListModel) {
            val names = listOf(
                binding.name1, binding.name2, binding.name3, binding.name4,
                binding.name5, binding.name6, binding.name7, binding.name8
            )

            val pld = listOf(
                binding.pld1, binding.pld2, binding.pld3, binding.pld4,
                binding.pld5, binding.pld6, binding.pld7, binding.pld8
            )

            val w = listOf(
                binding.w1, binding.w2, binding.w3, binding.w4,
                binding.w5, binding.w6, binding.w7, binding.w8
            )

            val d = listOf(
                binding.d1, binding.d2, binding.d3, binding.d4,
                binding.d5, binding.d6, binding.d7, binding.d8
            )

            val l = listOf(
                binding.l1, binding.l2, binding.l3, binding.l4,
                binding.l5, binding.l6, binding.l7, binding.l8
            )

            val f = listOf(
                binding.f1, binding.f2, binding.f3, binding.f4,
                binding.f5, binding.f6, binding.f7, binding.f8
            )

            val a = listOf(
                binding.a1, binding.a2, binding.a3, binding.a4,
                binding.a5, binding.a6, binding.a7, binding.a8
            )

            val gd = listOf(
                binding.gd1, binding.gd2, binding.gd3, binding.gd4,
                binding.gd5, binding.gd6, binding.gd7, binding.gd8
            )

            val pts = listOf(
                binding.pts1, binding.pts2, binding.pts3, binding.pts4,
                binding.pts5, binding.pts6, binding.pts7, binding.pts8
            )

            for (i in 0 until 8) {
                names[i].text = team.captainName[i]
                pld[i].text = team.PLD[i].toString()
                w[i].text = team.W[i].toString()
                d[i].text = team.D[i].toString()
                l[i].text = team.L[i].toString()
                f[i].text = team.F[i].toString()
                a[i].text = team.A[i].toString()

                val goalDiff = team.F[i] - team.A[i]
                gd[i].text = goalDiff.toString()

                val points = team.PLD[i] + team.W[i] + team.D[i] + team.L[i] + team.F[i] + team.A[i]
                pts[i].text = points.toString()
            }
        }
    }
}
