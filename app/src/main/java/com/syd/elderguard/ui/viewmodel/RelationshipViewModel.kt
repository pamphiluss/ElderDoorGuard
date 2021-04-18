package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.syd.elderguard.db.RelationshipDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.Relationship
import com.syd.elderguard.model.RelationshipBmob
import com.syd.elderguard.model.RelationshipManager
import com.syd.elderguard.repository.RelationshipRepository
import com.silencedut.taskscheduler.TaskScheduler

class RelationshipViewModel constructor(
    private val relationshipDao: RelationshipDao,
    private val relationshipRepository: RelationshipRepository
) : LiveCoroutinesViewModel() {

    private var posterFetchingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val posterListLiveData: LiveData<List<RelationshipManager>>
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        this.posterListLiveData = this.posterFetchingLiveData.switchMap {
            launchOnViewModelScope {
                this.relationshipRepository.loadRelationshipList { this.toastLiveData.postValue(it) }
            }
        }
    }

    fun fetchDisneyPosterList() = this.posterFetchingLiveData.postValue(true)

    /**
     * 添加事件列表
     */
    fun addCustomRelationship(name: String) {
        val eventList = posterListLiveData.value
        eventList!!.forEach {
            if (name.contentEquals(it.relationship)) {
                toastLiveData.postValue("已存在$name")
                return
            }
        }

        TaskScheduler.execute {
            val relationship = Relationship(name, -1)
            relationshipDao.insertRelationship(relationship)
            fetchDisneyPosterList()
            toastLiveData.postValue("添加成功")
        }
    }

    /**
     * 删除关系
     */
    fun deleteCustomRelationshipName(name: String) {
        deleteRelationshipFromCloud(name)
        TaskScheduler.execute {
            relationshipDao.deleteRelationship(name)
            fetchDisneyPosterList()
            toastLiveData.postValue("删除成功")
        }
    }
    /**
     * 删除云数据
     */
    private fun deleteRelationshipFromCloud(name: String) {
        if (!UserManager.isLogin()) return

        val query = BmobQuery<RelationshipBmob>()
        query.addWhereEqualTo("name", name)
        query.addWhereEqualTo("userId", UserManager.getUser().objectId)
        query.findObjects(object : FindListener<RelationshipBmob>() {
            override fun done(p0: MutableList<RelationshipBmob>?, p1: BmobException?) {
                if (p1 == null&& p0!= null) {
                    p0.forEach {
                        //删除
                        val relationshipBmob = RelationshipBmob()
                        relationshipBmob.objectId = it.objectId
                        relationshipBmob.delete(object : UpdateListener() {
                            override fun done(e: BmobException?) {

                            }
                        })
                    }
                }
            }

        })
    }

}