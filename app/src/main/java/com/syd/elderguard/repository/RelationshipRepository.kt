package com.syd.elderguard.repository

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.db.RelationshipDao
import com.syd.elderguard.model.RelationshipManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RelationshipRepository constructor(
    private val relationshipDao: RelationshipDao
) : Repository {

    override var isLoading: Boolean = false

    suspend fun loadRelationshipList() = withContext(Dispatchers.IO) {
        val liveData = MutableLiveData<List<RelationshipManager>>()
        val relationshipManagerList = relationshipDao.getRelationshipManagerList()
        //所有的管理列表
        val allManagerList = ArrayList<RelationshipManager>()
        //获取到所有的关系列表
        val relationshipList = relationshipDao.getRelationshipList();
        relationshipList.forEach { relationship->
            var hasRecord = false
            relationshipManagerList.forEach {
                if (it.relationshipId == relationship.id) {
                    hasRecord = true
                    allManagerList.add(it)
                }
            }

            if (!hasRecord) {
                val tmpRelationshipManager = RelationshipManager(relationship.id, relationship.name)
                allManagerList.add(tmpRelationshipManager)
            }
        }

        liveData.apply { postValue(allManagerList) }
    }
}