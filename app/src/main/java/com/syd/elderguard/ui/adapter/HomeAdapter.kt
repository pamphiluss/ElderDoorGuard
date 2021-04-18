package com.syd.elderguard.ui.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.R
import com.syd.elderguard.extensions.transToString
import com.syd.elderguard.model.AccountManager
import java.util.*


class HomeAdapter : BaseQuickAdapter<AccountManager, BaseViewHolder>(R.layout.item_home) {

    override fun convert(helper: BaseViewHolder, item: AccountManager?) {
        helper.setText(R.id.txvAccountName, item?.people+"("+item?.relationshipName+")")
        helper.setText(R.id.txvAccountDate, item?.date?.let { Date().transToString(it) })
        helper.setText(R.id.txvAccountEvent, item?.eventName)
        helper.setText(R.id.txvAccountCount, "${item?.count}")

        if (item?.outIntype == 1) {
            helper.setTextColor(R.id.txvAccountCount, ContextCompat.getColor(mContext, R.color.common_green))
        } else {
            helper.setTextColor(R.id.txvAccountCount, ContextCompat.getColor(mContext, R.color.common_red))
        }

    }

}