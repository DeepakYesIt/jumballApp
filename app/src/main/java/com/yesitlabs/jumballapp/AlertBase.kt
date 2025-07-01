package com.yesitlabs.jumballapp

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

object AlertBase {


    private var dialog: Dialog? = null

    fun showMe(context: Context?) {
        dialog?.dismiss()
        dialog = Dialog(context!!)
        dialog?.setContentView(R.layout.my_progess)
        dialog?.setCancelable(false)
        dialog?.window?.setDimAmount(0f)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.show()
    }

    fun dismissMe() {
        if (dialog != null) {
            dialog?.dismiss()
        }
    }

}