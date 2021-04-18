package com.syd.elderguard.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.R
import com.syd.elderguard.model.RelationshipManager

class RelationshipManagerAdapter : BaseQuickAdapter<RelationshipManager, BaseViewHolder>(R.layout.item_relationship_manager) {

    override fun convert(helper: BaseViewHolder, item: RelationshipManager?) {
        helper.setText(R.id.txvRelationshipName, item?.relationship)
        helper.setText(R.id.txvRelationshipCount, "${item?.peopleCount}人/${item?.relationshipCount}笔")
    }

}