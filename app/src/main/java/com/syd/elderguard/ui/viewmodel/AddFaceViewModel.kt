package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.silencedut.taskscheduler.TaskScheduler
import com.syd.elderguard.db.FaceDao
import com.syd.elderguard.model.Account
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.Relationship

class AddFaceViewModel constructor(
    private val faceDao: FaceDao
) : LiveCoroutinesViewModel(){
    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    val contactShowLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun addOneFace(id: Long, accountType: Int, accountCount: String, event: Event, accountPeople: String,
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
            faceDao.insertEvent(account)
            toastLiveData.postValue("添加成功")
        }
    }
}