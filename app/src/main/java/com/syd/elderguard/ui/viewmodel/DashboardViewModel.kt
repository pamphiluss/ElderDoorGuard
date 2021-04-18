package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.model.CountSum
import com.syd.elderguard.model.EventSum
import com.syd.elderguard.model.RelationshipSum
import com.silencedut.taskscheduler.TaskScheduler

class DashboardViewModel constructor(
    private val accountDao: AccountDao
) : LiveCoroutinesViewModel() {

    val sumLiveData: MutableLiveData<List<CountSum>> = MutableLiveData()
    val intPeopleCountLiveData: MutableLiveData<Int> = MutableLiveData()
    val outPeopleCountLiveData: MutableLiveData<Int> = MutableLiveData()
    val eventSumLiveData: MutableLiveData<List<EventSum>> = MutableLiveData()
    val relationshipSumLiveData: MutableLiveData<List<RelationshipSum>> = MutableLiveData()

    fun getRelationshipChartList() {
        TaskScheduler.execute {
            val list = accountDao.relationshipChartList()
            relationshipSumLiveData.postValue(list)
        }
    }

    fun getEventChartList() {
        TaskScheduler.execute {
            val list = accountDao.eventChartList()
            eventSumLiveData.postValue(list)
        }
    }

    fun intPeopleCount() {
        TaskScheduler.execute {
            val count = accountDao.intPeopleCount()
            intPeopleCountLiveData.postValue(count)
        }
    }

    fun outPeopleCount() {
        TaskScheduler.execute {
            val count = accountDao.outPeopleCount()
            outPeopleCountLiveData.postValue(count)
        }
    }

    fun getSumAccount() {
        TaskScheduler.execute {
            val list = accountDao.accountSumByIntype()
            sumLiveData.postValue(list)
        }
    }
}