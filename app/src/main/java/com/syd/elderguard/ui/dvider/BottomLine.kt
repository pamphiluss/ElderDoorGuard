package com.syd.elderguard.ui.dvider

import android.content.Context

class BottomLine(context: Context, lineColor: Int = 0x4de0e0e0) : Y_DividerItemDecoration(context){

    //30%透明
    private var lineColor: Int = 0x4de0e0e0

    init {
        this.lineColor = lineColor
    }

    override fun getDivider(itemPosition: Int): Y_Divider? {
        return Y_DividerBuilder()
            .setBottomSideLine(true, lineColor.toInt(), 1F, 10F, 10F)
            .create()
    }
}