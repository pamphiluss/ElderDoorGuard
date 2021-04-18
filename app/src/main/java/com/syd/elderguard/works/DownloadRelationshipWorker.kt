package com.syd.elderguard.works

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import cn.bmob.v3.BmobQuery
import com.syd.elderguard.db.RelationshipDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.Relationship
import com.syd.elderguard.model.RelationshipBmob
import org.koin.core.KoinComponent
import org.koin.core.inject

class DownloadRelationshipWorker (context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), KoinComponent {

    // 每页的数据是10条
    private val limit: Int = 500
    // 当前页的编号，从0开始
    private var curPage = 0
    private val relationshipDao: RelationshipDao by inject()

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
        val query = BmobQuery<RelationshipBmob>()
        // 跳过之前页数并去掉重复数据
        if (curPage == 0) {
            query.setSkip(0)
        } else {
            query.setSkip(curPage * limit + 1);
        }

        query.setLimit(limit)

        val list = query.findObjectsSync(RelationshipBmob::class.java)

        if (list.isNotEmpty()) {
            list.forEach {
                val relationship = it.name?.let { it1 -> Relationship(it1, 0) }
                if (relationship != null) {
                    this@DownloadRelationshipWorker.relationshipDao.insertRelationship(relationship)
                }
            }
        }

        if (list.isEmpty() || list.size<limit) {
            return false
        }

        return true
    }
}