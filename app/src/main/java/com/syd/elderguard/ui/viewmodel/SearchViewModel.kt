package com.syd.elderguard.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.db.AccountDao
import com.syd.elderguard.model.AccountManager
import com.silencedut.taskscheduler.TaskScheduler

class SearchViewModel constructor(
    private val accountDao: AccountDao
) : LiveCoroutinesViewModel() {

    var accoutListLiveData: MutableLiveData<List<AccountManager>> = MutableLiveData()

    fun loadAccountList(word: String) {
        Log.i("查询", "'%$word%'")
        TaskScheduler.execute {
            val list = accountDao.getAccountListByKeyword(word)
            accoutListLiveData.postValue(list)
        }
    }

}