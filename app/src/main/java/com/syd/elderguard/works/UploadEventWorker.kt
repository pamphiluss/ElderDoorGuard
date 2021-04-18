package com.syd.elderguard.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import com.syd.elderguard.db.EventDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.EventBmob
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * 上传事件
 */
class UploadEventWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), KoinComponent {

    override fun doWork(): Result {
        val eventDao: EventDao by inject()
        val eventList = eventDao.getEventUploadList()

        if (!UserManager.isLogin() || eventList.isEmpty()) {
            return Result.success()
        }

        //批量上传列表
        val uploadList: MutableList<EventBmob> = ArrayList()
        val user = UserManager.getUser()
        eventList.forEach {
            uploadList.add(EventBmob(it.name, user.objectId))
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
                            var event = eventList[index]
                            event.state = 0
                            eventDao.updateEvent(event)
                        }
                    }
                }

            }
        })

        return Result.success()
    }
}