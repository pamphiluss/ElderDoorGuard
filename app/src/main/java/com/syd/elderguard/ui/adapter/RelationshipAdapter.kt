package com.syd.elderguard.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.R
import com.syd.elderguard.model.Relationship

class RelationshipAdapter : BaseQuickAdapter<Relationship, BaseViewHolder>(R.layout.item_event) {

    override fun convert(helper: BaseViewHolder, item: Relationship?) {
        helper.setText(R.id.txvEventName, item?.name)
    }

}