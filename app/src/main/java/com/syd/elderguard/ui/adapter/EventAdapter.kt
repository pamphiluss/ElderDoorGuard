package com.syd.elderguard.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.R
import com.syd.elderguard.model.Event

class EventAdapter : BaseQuickAdapter<Event, BaseViewHolder>(R.layout.item_event) {

    override fun convert(helper: BaseViewHolder, item: Event?) {
        helper.setText(R.id.txvEventName, item?.name)
    }

}