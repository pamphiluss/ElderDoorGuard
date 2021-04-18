package com.syd.elderguard.ui.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.inlineactivityresult.startActivityForResult
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.extensions.getToday
import com.syd.elderguard.extensions.toEditable
import com.syd.elderguard.extensions.transToString
import com.syd.elderguard.model.AccountManager
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.Relationship
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.viewmodel.AddAccountViewModel
import kotlinx.android.synthetic.main.activity_add_account.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.util.*


/**
 * 添加记账
 */
class AddAccountActivity : BaseActivity(), View.OnClickListener {

    //添加类型 1：支出  2：收入
    private var accountType = 1
    private lateinit var event: Event
    private lateinit var relationship: Relationship
    private var recordDate: Long = System.currentTimeMillis()

    private lateinit var accountViewModel: AddAccountViewModel
    private lateinit var accountManager: AccountManager
    private var isEdit = false

    override fun getLayoutId(): Int {
        return R.layout.activity_add_account
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        obtainParams()
        super.onCreate(savedInstanceState)

        txvEventName.setOnClickListener(this)
        txvRelationshipName.setOnClickListener(this)
        txvPartInDate.setOnClickListener(this)
        txvContactChoice.setOnClickListener(this)

        accountViewModel = getViewModel<AddAccountViewModel>().apply {
            loadContactShow()
        }
        accountViewModel.toastLiveData.observe(this, androidx.lifecycle.Observer {
            showLongToast(it)
            if("添加成功" == it || "删除成功" == it) {
                finish()
            }
        })
        accountViewModel.contactShowLiveData.observe(this, androidx.lifecycle.Observer {
            txvContactChoice.visibility = if(it) View.VISIBLE else View.GONE
        })

        initEditData()
    }

    override fun getToolbarTitleId(): Int {
        if (accountType == 2) return R.string.title_add_account_in

        return R.string.title_add_account_out
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val itemSave = menu!!.findItem(R.id.item_save_account)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemSave.title = Html.fromHtml("<font color='#00b805'>保存</font>", Html.FROM_HTML_MODE_LEGACY)
        } else {
            itemSave.title = Html.fromHtml("<font color='#00b805'>保存</font>")
        }

        if (isEdit) {
            val itemDelete = menu!!.findItem(R.id.item_delete_account)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemDelete.title = Html.fromHtml("<font color='#606060'>删除</font>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                itemDelete.title = Html.fromHtml("<font color='#606060'>删除</font>")
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.account_edit_menu, menu)
        } else {
            menuInflater.inflate(R.menu.account_add_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_save_account -> {
                addOneAccount()
            }
            R.id.item_delete_account -> {
                deleteOnAccount()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.txvEventName -> {
                onChoiceEventName()
            }
            R.id.txvRelationshipName -> {
                onChoiceRelationName()
            }
            R.id.txvPartInDate -> {
                onChoiceDate()
            }
            R.id.txvContactChoice-> {
                onChoiceContact()
            }
        }
    }

    private fun obtainParams() {
        accountType = intent.getIntExtra(Constant.BUNDLE_ACCOUNT_TYPE, 0)
        if (accountType == 0) {
            accountManager = intent.getParcelableExtra<AccountManager>(Constant.BUNDLE_ACCOUNT_MANAGER)
            isEdit = true
        }
    }

    private fun initEditData() {
        invalidateOptionsMenu();
        if (!isEdit) {
            //当前日期
            txvPartInDate.text = Date().getToday()
            return
        }

        //初始化数据
        accountType = accountManager.outIntype
        event = Event(accountManager.eventId, accountManager.eventName)
        relationship = Relationship(accountManager.relationshipId, accountManager.relationshipName)
        recordDate = accountManager.date

        edtAccountCount.text = accountManager.count.toString().toEditable()
        txvEventName.text = accountManager.eventName
        edtAccountPeople.text = accountManager.people.toEditable()
        txvRelationshipName.text= accountManager.relationshipName
        txvPartInDate.text = Date().transToString(accountManager.date)
        edtAccountPlace.text = accountManager.place?.toEditable()
        edtAccountRemark.text = accountManager.remark?.toEditable()
    }

    private fun onChoiceContact() {
        startActivityForResult<ContactListActivity> { success, data ->
            if (success) {
                val name = data.getStringExtra(Constant.BUNDLE_CONTACT_NAME)
                edtAccountPeople.text = name.toEditable()
            }
        }
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)
    }

    private fun onChoiceDate() {
        MaterialDialog(this).show {
            datePicker { _, date ->
                this@AddAccountActivity.txvPartInDate.text = "${date.year}-${date.month+1}-${date.dayOfMonth}"
                recordDate = date.timeInMillis
            }
        }
    }

    private fun onChoiceRelationName() {
        startActivityForResult<RelationListActivity> { success, data ->
            if (success) {
                relationship = data.getParcelableExtra<Relationship>(Constant.BUNDLE_RELATIONSHIP)
                txvRelationshipName.text = relationship.name
            }
        }
    }

    /**
     * 选择事件
     */
    private fun onChoiceEventName() {
        startActivityForResult<EventListActivity> { success, data ->
            if (success) {
                event = data.getParcelableExtra<Event>(Constant.BUNDLE_EVENT)
                txvEventName.text = event.name
            }
        }
    }

    private fun deleteOnAccount() {
        accountViewModel.deleteOnAccount(accountManager)
    }

    private fun addOneAccount() {
        if (!::event.isInitialized) {
            showLongToast("请选择事件")
            return
        }

        if (!::relationship.isInitialized) {
            showLongToast("请选择关系")
            return
        }

        val accountCount = edtAccountCount.text.toString().trim()
        val accountPeople = edtAccountPeople.text.toString().trim()
        val place = edtAccountPlace.text.toString().trim()
        val remark = edtAccountRemark.text.toString().trim()
        var id = if (isEdit) accountManager.id else 0

        accountViewModel.addOneAccount(id, accountType, accountCount, event, accountPeople,
            relationship, recordDate, place, remark)
    }
}