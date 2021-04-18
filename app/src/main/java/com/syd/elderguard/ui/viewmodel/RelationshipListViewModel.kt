package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.db.RelationshipDao
import com.syd.elderguard.model.Relationship
import com.silencedut.taskscheduler.Task
import com.silencedut.taskscheduler.TaskScheduler

class RelationshipListViewModel constructor(
    private val relationshipDao: RelationshipDao
) : LiveCoroutinesViewModel() {

    var eventListLiveData: MutableLiveData<List<Relationship>> = MutableLiveData()
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * 添加事件列表
     */
    fun addCustomRelationship(name: String) {
        val eventList = eventListLiveData.value
        eventList!!.forEach {
            if (name.contentEquals(it.name)) {
                toastLiveData.postValue("已存在$name")
                return
            }
        }

        TaskScheduler.execute {
            val relationship = Relationship(name)
            relationshipDao.insertRelationship(relationship)
            loadRelationshipList()
            toastLiveData.postValue("添加成功")
        }
    }

    /**
     * 加载事件列表
     */
    fun loadRelationshipList() {
        TaskScheduler.execute(object: Task<List<Relationship>>() {
            override fun doInBackground(): List<Relationship> {
                var eventList = relationshipDao.getRelationshipList();
                if (eventList.isEmpty()) {
                    //插入本地数据库
                    var list: MutableList<Relationship> = ArrayList()
                    list.add(Relationship("亲戚"))
                    list.add(Relationship("朋友"))
                    list.add(Relationship("同学"))
                    list.add(Relationship("同事"))
                    list.add(Relationship("邻里"))
                    list.add(Relationship("未知"))

                    relationshipDao.insertRelationshipList(list)
                    return list
                }
                return eventList
            }

            override fun onSuccess(result: List<Relationship>?) {
                eventListLiveData.value = result

            }
        })
    }

}