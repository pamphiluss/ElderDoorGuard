package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.db.TodoDao
import com.syd.elderguard.model.Todo
import com.syd.elderguard.utils.CalendarReminderUtils
import com.silencedut.taskscheduler.TaskScheduler

class TodoAddViewModel constructor(
    private val todoDao: TodoDao
) : LiveCoroutinesViewModel() {

    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    fun addOneTodo(todo: Todo, oldContent: String="") {
       TaskScheduler.execute {
           if (oldContent != "") {
               //编辑，先删除就
               CalendarReminderUtils.deleteCalendarEvent(BaseApplication.inst, oldContent)
           }
           todoDao.insertTodo(todo)
           CalendarReminderUtils.addCalendarEvent(BaseApplication.inst, todo.content, "", todo.dateTime, 60)
           toastLiveData.postValue("保存成功")
       }
    }
}