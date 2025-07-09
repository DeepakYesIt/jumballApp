package com.jumball.app

import com.jumball.app.model.Player

interface OnClickListener {

    fun onClick(position: Int, model: Player)
}