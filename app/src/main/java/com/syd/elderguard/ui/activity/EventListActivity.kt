package com.syd.elderguard.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.model.Event
import com.syd.elderguard.ui.adapter.EventAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.ui.viewmodel.EventListViewModel
import kotlinx.android.synthetic.main.activity_event_list.*
import org.koin.android.viewmodel.ext.android.getViewModel

class EventListActivity : BaseActivity() {

    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventListViewModel: EventListViewModel
    private lateinit var deleteEventName: String

    override fun getLayoutId(): Int {
        return R.layout.activity_event_list
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_event
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvEventList.layoutManager = LinearLayoutManager(this@EventListActivity)
        eventAdapter = EventAdapter();
        eventAdapter.setOnItemClickListener { _, _, position ->
            setResult(Activity.RESULT_OK,
                Intent().putExtra(Constant.BUNDLE_EVENT, eventAdapter.getItem(position)))
            finish()
        }
        eventAdapter.setOnItemLongClickListener{ madapter, view, position ->
            val event = eventAdapter.getItem(position)
            event?.let {
                if (event.id<12) return@let true

                deleteEventName = event.name
                eventListViewModel.checkAccountHasEvent(event.id)
            }

            true
        }
        rvEventList.addItemDecoration(BottomLine(this@EventListActivity))
        rvEventList.adapter = eventAdapter
        eventListViewModel = getViewModel<EventListViewModel>().apply{
            loadEventList()
        }

        eventListViewModel.accountHasEventLiveData.observe(this, Observer {
            if (it > 0) {
                //不能直接删除
                showCannotDeleteEventDialog()
            } else {
                showDeleteEventDialog()
            }
        })

        eventListViewModel.eventListLiveData.observe(this, Observer {
            //其他移动到最后
            var unknownEvent: Event? = null
            val newEventList = mutableListOf<Event>()
            it.forEach { rs ->
                if (rs.name == "其他") {
                    unknownEvent = rs
                } else {
                    newEventList.add(rs)
                }
            }

            newEventList.add(unknownEvent!!)
            eventAdapter.setNewData(newEventList)
        })

        eventListViewModel.toastLiveData.observe(this, Observer {
            showLongToast(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_add_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add_event -> {
                showAddEventDialog()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * 不能直接删除
     */
    private fun showCannotDeleteEventDialog() {
        MaterialDialog(this).show {
            title(R.string.dialog_title_tips)
            message(R.string.dialog_message_cannot_delete_event)
            positiveButton(R.string.lable_i_know)
        }
    }

    /**
     * 删除事件
     */
    private fun showDeleteEventDialog() {
        MaterialDialog(this).show {
            title(R.string.dialog_title_delete_relationship)
            message(text = "你确定要删除事件‘${deleteEventName}’吗？")
            negativeButton(R.string.lable_cancle)
            positiveButton(R.string.lable_ok) {
                eventListViewModel.deleteCustomEventName(deleteEventName)
            }
        }
    }

    /**
     * 添加自定义事件
     */
    private fun showAddEventDialog() {
        MaterialDialog(this).show {
            title(R.string.lable_custom_add)
            input(maxLength = 8, hintRes = R.string.hint_event_name) { _, text ->
                eventListViewModel.addCustomEvent(text.toString())
            }
            positiveButton(R.string.lable_ok)
        }

    }

}

