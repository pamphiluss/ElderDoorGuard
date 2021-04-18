package com.syd.elderguard.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.AccountBmob
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 上传记账记录
 */
class UploadAccountWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), KoinComponent {

    override fun doWork(): Result {
        val accountDao by inject<AccountDao>()
        val accountList = accountDao.getAccountUploadList()

        if (!UserManager.isLogin() || accountList.isEmpty()) {
            return Result.success()
        }

        //批量上传列表
        val uploadList: MutableList<AccountBmob> = ArrayList()
        val user = UserManager.getUser()
        accountList.forEach {
            uploadList.add(AccountBmob(it.count, it.outIntype, it.eventId, it.people,
                it.relationshipId, it.date, it.place, it.remark, user.objectId))
        }

        //批量上传
        BmobBatch().insertBatch(uploadList as List<BmobObject>?).doBatch(object : QueryListListener<BatchResult>() {
            override fun done(list: MutableList<BatchResult>?, e: BmobException?) {
                if (e != null) return

                list?.let {
                    for ((index, value) in list.withIndex()) {
                        val error = value.error
                        if(error == null) {
                            //插入成功
                            var account = accountList[index]
                            account.state = 0
                            accountDao.updateAccount(account)
                        }
                    }
                }

            }
        })

        return Result.success()
    }
}