package com.syd.elderguard.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import cn.bmob.v3.BmobQuery
import com.syd.elderguard.db.EventDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.EventBmob
import org.koin.core.KoinComponent
import org.koin.core.inject

class DownloadEventWorker (context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), KoinComponent {

    // 每页的数据是10条
    private val limit: Int = 500
    // 当前页的编号，从0开始
    private var curPage = 0
    private val eventDao: EventDao by inject()

    override fun doWork(): Result {
        if (!UserManager.isLogin()) {
            return Result.success()
        }

        var hasCloudData: Boolean
        do {
            hasCloudData = syncCloudData()
        } while (hasCloudData)

        return Result.success()
    }

    /**
     * 分页同步数据
     * @return 是否有下一页数据
     */
    private fun syncCloudData(): Boolean {
        val query = BmobQuery<EventBmob>()
        // 跳过之前页数并去掉重复数据
        if (curPage == 0) {
            query.setSkip(0)
        } else {
            query.setSkip(curPage * limit + 1);
        }

        query.setLimit(limit)

        val list = query.findObjectsSync(EventBmob::class.java)

        if (list.isNotEmpty()) {
            list.forEach {
                val event = it.name?.let { it1 -> Event(it1, 0) }
                if (event != null) {
                    this@DownloadEventWorker.eventDao.insertEvent(event)
                }
            }
        }

        if (list.isEmpty() || list.size<limit) {
            return false
        }

        return true
    }
}