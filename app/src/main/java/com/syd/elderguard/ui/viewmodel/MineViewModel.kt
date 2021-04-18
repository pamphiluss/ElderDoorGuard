package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.db.EventDao
import com.syd.elderguard.db.RelationshipDao
import com.silencedut.taskscheduler.TaskScheduler

class MineViewModel constructor(
    private val eventDao: EventDao,
    private val relationshipDao: RelationshipDao,
    private val accountDao: AccountDao
) : LiveCoroutinesViewModel() {

    val counterLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * 待上传数据监测任务：
     */
    fun checkUnUploadCounter() {
        TaskScheduler.execute {
            var total = 0
            //事件
            val eventCount = eventDao.checkUnUploadCount()
            total += eventCount
            //关系
            val relationshipCount = relationshipDao.checkUnUploadCount()
            total += relationshipCount
            //记账
            val accountCount = accountDao.checkUnUploadCount()
            total += accountCount

            counterLiveData.postValue(total)
        }
    }
}