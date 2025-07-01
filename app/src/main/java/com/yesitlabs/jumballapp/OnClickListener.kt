package com.yesitlabs.jumballapp

import com.yesitlabs.jumballapp.model.Player

interface OnClickListener {

    fun onClick(position: Int, model: Player)
}