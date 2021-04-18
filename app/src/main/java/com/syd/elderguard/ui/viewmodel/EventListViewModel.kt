package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.db.EventDao
import com.syd.elderguard.manager.UserManager
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.EventBmob
import com.silencedut.taskscheduler.Task
import com.silencedut.taskscheduler.TaskScheduler

class EventListViewModel constructor(
    private val eventDao: EventDao,
    private val accountDao: AccountDao
) : LiveCoroutinesViewModel() {

    var eventListLiveData: MutableLiveData<List<Event>> = MutableLiveData()
    val toastLiveData: MutableLiveData<String> = MutableLiveData()
    val accountHasEventLiveData: MutableLiveData<Int> = MutableLiveData()

    /**
     * 删除自定义事件
     */
    fun deleteCustomEventName(name: String) {
        deleteEventFromCloud(name)
        TaskScheduler.execute {
            eventDao.deleteEventByName(name)
            loadEventList()
        }
    }

    /**
     * 删除云数据
     */
    private fun deleteEventFromCloud(name: String) {
        if (!UserManager.isLogin()) return

        val query = BmobQuery<EventBmob>()
        query.addWhereEqualTo("name", name)
        query.addWhereEqualTo("userId", UserManager.getUser().objectId)
        query.findObjects(object : FindListener<EventBmob>() {
            override fun done(p0: MutableList<EventBmob>?, p1: BmobException?) {
                if (p1 == null&& p0!= null) {
                    p0.forEach {
                        //删除
                        val eventBmob = EventBmob()
                        eventBmob.objectId = it.objectId
                        eventBmob.delete(object : UpdateListener() {
                            override fun done(e: BmobException?) {

                            }
                        })
                    }
                }
            }

        })
    }

    /**
     * 判断是否有相关记账记录
     */
    fun checkAccountHasEvent(eventId: Long) {
        TaskScheduler.execute {
            val count = accountDao.checkCountByEventId(eventId)
            accountHasEventLiveData.postValue(count)
        }
    }
    /**
     * 添加事件列表
     */
    fun addCustomEvent(name: String) {
        val eventList = eventListLiveData.value
        eventList!!.forEach {
            if (name.contentEquals(it.name)) {
                toastLiveData.postValue("已存在$name")
                return
            }
        }

        TaskScheduler.execute {
            val event = Event(name, state = -1)
            eventDao.insertEvent(event)
            loadEventList()
            toastLiveData.postValue("添加成功")
        }
    }

    /**
     * 加载事件列表
     */
    fun loadEventList() {
        TaskScheduler.execute(object: Task<List<Event>>() {
            override fun doInBackground(): List<Event> {
                var eventList = eventDao.getEventList();
                if (eventList.isEmpty()) {
                    //插入本地数据库，默认数据
                    var list: MutableList<Event> = ArrayList()
                    list.add(Event("参加婚礼"))
                    list.add(Event("参加葬礼"))
                    list.add(Event("宝宝出生"))
                    list.add(Event("宝宝满月"))
                    list.add(Event("宝宝周岁"))
                    list.add(Event("老人办寿"))
                    list.add(Event("探望病人"))
                    list.add(Event("金榜题名"))
                    list.add(Event("小学升学"))
                    list.add(Event("压碎红包"))
                    list.add(Event("其他"))
                    eventDao.insertEventList(list)
                    return list
                }
                return eventList
            }

            override fun onSuccess(result: List<Event>?) {
                eventListLiveData.value = result

            }
        })
    }

}