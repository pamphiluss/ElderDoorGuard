package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.syd.elderguard.Constant
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.db.EventDao
import com.syd.elderguard.db.RelationshipDao
import com.syd.elderguard.model.AccountManager
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.Relationship
import com.silencedut.taskscheduler.TaskScheduler
import com.tencent.mmkv.MMKV
import org.json.JSONObject


class HomeViewModel constructor(
    private val accountDao: AccountDao,
    private val relationshipDao: RelationshipDao,
    private val eventDao: EventDao
) : LiveCoroutinesViewModel() {

    var accoutListLiveData: MutableLiveData<List<AccountManager>> = MutableLiveData()
    var soupLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadAccountList(outIntype: Int, sort: Int = 4) {
        TaskScheduler.execute {
            when(sort) {
                1-> {
                    val list = accountDao.getAccountListByOutIntypeAscCount(outIntype)
                    accoutListLiveData.postValue(list)
                }
                2-> {
                    val list = accountDao.getAccountListByOutIntypeDescCount(outIntype)
                    accoutListLiveData.postValue(list)
                }
                3-> {
                    val list = accountDao.getAccountListByOutIntypeAscDate(outIntype)
                    accoutListLiveData.postValue(list)
                }
                4-> {
                    val list = accountDao.getAccountListByOutIntypeDescDate(outIntype)
                    accoutListLiveData.postValue(list)
                }
            }
        }
    }

    fun initBaseData() {
        TaskScheduler.execute {
            //关系表没有数据，初始化默认本地数据
            var posters = relationshipDao.getRelationshipList()

            if (posters.isEmpty()) {
                var list: MutableList<Relationship> = ArrayList()
                list.add(Relationship("亲戚"))
                list.add(Relationship("朋友"))
                list.add(Relationship("同学"))
                list.add(Relationship("同事"))
                list.add(Relationship("邻里"))
                list.add(Relationship("未知"))

                relationshipDao.insertRelationshipList(list)
            }

            var eventList = eventDao.getEventList();
            if (eventList.isEmpty()) {
                //插入本地数据库
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
            }
        }
    }

    fun requestSoupHappay() {
        //https://dujitang.90so.net/docs
        AndroidNetworking.get("https://data.zhai78.com/openOneBad.php")
            .setTag("test")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    val title = response.optString("txt")
                    soupLiveData.postValue(title)
                    MMKV.defaultMMKV().encode(Constant.SP_DAY_SOUP, title)
                }

                override fun onError(anError: ANError) {
                    val title = MMKV.defaultMMKV().decodeString(Constant.SP_DAY_SOUP,
                        "很多时候，乐观的态度和好听的话帮不了你。")
                    soupLiveData.postValue(title)
                }
            })
    }
}