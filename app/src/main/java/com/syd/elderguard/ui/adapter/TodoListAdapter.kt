package com.syd.elderguard.ui.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.syd.elderguard.R
import com.syd.elderguard.extensions.timeStamp2Date
import com.syd.elderguard.model.Todo
import java.util.*

class TodoListAdapter : BaseQuickAdapter<Todo, BaseViewHolder>(R.layout.item_todo_list) {

    override fun convert(helper: BaseViewHolder, item: Todo?) {
        helper.setText(R.id.txvTodoTitle, item?.content)
        if (item != null) {
            helper.setText(R.id.txvTodoDateTime, Date().timeStamp2Date(item.dateTime))
            if (item.dateTime<System.currentTimeMillis()) {
                //过期
                helper.setTextColor(R.id.txvTodoTitle, Color.parseColor("#a0a0a0"))
                helper.setVisible(R.id.txvLabelOverdue, true)
            }  else {
                helper.setTextColor(R.id.txvTodoTitle, Color.parseColor("#383838"))
                helper.setVisible(R.id.txvLabelOverdue, false)
            }
        }

    }

}