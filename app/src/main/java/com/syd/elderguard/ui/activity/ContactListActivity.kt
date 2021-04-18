package com.syd.elderguard.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.ui.adapter.ContactAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.widget.LetterView
import com.silencedut.taskscheduler.Task
import com.silencedut.taskscheduler.TaskScheduler
import kotlinx.android.synthetic.main.activity_person_list.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class ContactListActivity : BaseActivity(), KoinComponent, LetterView.CharacterClickListener,
 ContactAdapter.OnItemClickListener {

    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: ContactAdapter
    private val accountDao: AccountDao by inject()

    override fun getLayoutId(): Int {
        return R.layout.activity_person_list
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_contact
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        letter_view.setCharacterListener(this)
        loadContactList()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }

    private fun loadContactList() {
        TaskScheduler.execute(object : Task<List<String>>() {

            override fun onSuccess(result: List<String>?) {
                result?.let {
                    manager = LinearLayoutManager(this@ContactListActivity)
                    adapter = ContactAdapter(this@ContactListActivity, result.toTypedArray())
                    adapter.setOnItemClickListener(this@ContactListActivity)
                    contact_list.layoutManager = manager
                    contact_list.addItemDecoration(BottomLine(this@ContactListActivity))
                    contact_list.adapter = adapter
                }
            }

            override fun doInBackground(): List<String> {
                return accountDao.getContactList()
            }

        })
    }

    override fun clickArrow() {
        manager.scrollToPositionWithOffset(0,0)
    }

    override fun clickCharacter(character: String?) {
        manager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0)
    }

    override fun onItemClick(name: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra(Constant.BUNDLE_CONTACT_NAME, name))
        finish()
    }
}