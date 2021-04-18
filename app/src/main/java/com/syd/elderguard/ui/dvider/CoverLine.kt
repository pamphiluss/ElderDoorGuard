package com.syd.elderguard.ui.dvider

import android.content.Context

class CoverLine(context: Context, lineColor: Int = 0x00000000) : Y_DividerItemDecoration(context){

    //30%透明
    private var lineColor: Int = 0x00000000

    init {
        this.lineColor = lineColor
    }

    override fun getDivider(itemPosition: Int): Y_Divider? {
        return Y_DividerBuilder()
            .setLeftSideLine(true, lineColor.toInt(), 5F, 0F, 10F)
            .setRightSideLine(true, lineColor.toInt(), 5F, 0F, 10F)
            .setBottomSideLine(true, lineColor.toInt(), 5F, 0F, 10F)
            .setTopSideLine(true, lineColor.toInt(), 5F, 0F, 10F)
            .create()
    }
}