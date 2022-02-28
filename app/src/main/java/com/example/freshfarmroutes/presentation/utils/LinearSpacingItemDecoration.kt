package com.example.freshfarmroutes.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingItemDecoration(): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition=parent.getChildLayoutPosition(view)
        if(itemPosition==0){
            outRect.top=36
        }
        outRect.bottom=24
        outRect.left=24
        outRect.right=24
    }
}