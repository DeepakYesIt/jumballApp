package com.yesitlabs.jumballapp.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CenterItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val totalSpace = parent.width - (parent.paddingLeft + parent.paddingRight)
        val spanCount = 3 // Number of columns
        val itemWidth = totalSpace / spanCount

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        // Calculate padding for each item
        val leftPadding = column * itemWidth / spanCount
        val rightPadding = itemWidth - leftPadding

        outRect.left = leftPadding
        outRect.right = rightPadding
    }
}