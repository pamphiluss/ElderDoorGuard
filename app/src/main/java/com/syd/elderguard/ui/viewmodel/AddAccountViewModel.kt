package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.*
import com.silencedut.taskscheduler.TaskScheduler

class AddAccountViewModel constructor(
    private val accountDao: AccountDao
) : LiveCoroutinesViewModel() {

    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    val contactShowLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun addOneAccount(id: Long, accountType: Int, accountCount: String, event: Event, accountPeople: String,
                               relationship: Relationship, recordDate: Long, place: String, remark: String) {
        if (accountCount.isEmpty()) {
            toastLiveData.postValue("请输入份子金额")
            return
        }

        if (event.name.isEmpty()) {
            toastLiveData.postValue("请选择事件")
            return
        }

        if (accountPeople.isEmpty()) {
            toastLiveData.postValue("请输入人物")
            return
        }

        if (relationship.name.isEmpty()) {
            toastLiveData.postValue("请输入关系")
            return
        }

        val account = Account(id, accountCount.toFloat(), accountType, event.id,
            accountPeople, relationship.id, recordDate, place, remark)

        TaskScheduler.execute {
            accountDao.insertEvent(account)
            toastLiveData.postValue("添加成功")
        }
    }

    fun deleteOnAccount(accountManager: AccountManager) {
        deleteAccountFromCloud(accountManager)
        TaskScheduler.execute {
            accountDao.deleteAccount(accountManager.id)
            toastLiveData.postValue("删除成功")
        }
    }

    fun loadContactShow() {
        TaskScheduler.execute {
            val list = accountDao.getContactList()
            contactShowLiveData.postValue(list.isNotEmpty())
        }
    }

    /**
     * 删除云数据
     */
    private fun deleteAccountFromCloud(accountManager: AccountManager) {
        if (!UserManager.isLogin()) return

        val query = BmobQuery<AccountBmob>()
        query.addWhereEqualTo("date", accountManager.date)
        query.addWhereEqualTo("userId", UserManager.getUser().objectId)
        query.findObjects(object : FindListener<AccountBmob>() {
            override fun done(p0: MutableList<AccountBmob>?, p1: BmobException?) {
                if (p1 == null&& p0!= null) {
                    p0.forEach {
                        //删除
                        val accountBmob = AccountBmob()
                        accountBmob.objectId = it.objectId
                        accountBmob.delete(object : UpdateListener() {
                            override fun done(e: BmobException?) {

                            }
                        })
                    }
                }
            }

        })
    }


}