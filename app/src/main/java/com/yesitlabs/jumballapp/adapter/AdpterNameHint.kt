package com.yesitlabs.jumballapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.SessionManager
import com.yesitlabs.jumballapp.databinding.GuessHintItemBinding
import com.yesitlabs.jumballapp.model.guessName.GuessName

class AdpterNameHint(
    private var quiz: ArrayList<GuessName>,
    private var answer: ArrayList<GuessName>,
    var requireActivity: FragmentActivity,
    private var usertype: String
) : RecyclerView.Adapter<AdpterNameHint.Holder>() {

    private var sessionManager: SessionManager = SessionManager(requireActivity)
    private var listener: OnItemClickListener? = null
    private var count = 0

    interface OnItemClickListener {
        fun onItemClick(item: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = GuessHintItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(quiz[position], requireActivity)

        if (usertype == "USER") {
            holder.itemView.setOnClickListener {
                sessionManager.playClickSound()
                onHintClick(position)
            }
        }
    }

    private fun onHintClick(position: Int) {
        Log.e("Position", position.toString())

        if (!quiz[position].used) {
            val isCorrect = quiz[position] == answer.getOrNull(count)

            quiz[position].backgoung_color = if (isCorrect) R.color.hint_color_right else R.color.hint_color_worng
            quiz[position].text_color = R.color.white
            quiz[position].used = true
            notifyItemChanged(position)

            listener?.onItemClick(quiz[position].hint)
            count += 1
        }
    }

    override fun getItemCount(): Int = quiz.size

    class Holder(private val binding: GuessHintItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GuessName, requireActivity: FragmentActivity) {
            binding.hintBox.setCardBackgroundColor(
                ContextCompat.getColor(requireActivity, item.backgoung_color)
            )
            binding.hintChar.text = item.hint
            binding.hintChar.setTextColor(
                ContextCompat.getColor(requireActivity, item.text_color)
            )
        }
    }
}
