package com.syd.elderguard.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.db.TodoDao
import com.syd.elderguard.model.Todo
import com.syd.elderguard.utils.CalendarReminderUtils
import com.silencedut.taskscheduler.Task
import com.silencedut.taskscheduler.TaskScheduler

class TodoListViewModel constructor(
    private val todoDao: TodoDao
) : LiveCoroutinesViewModel() {

    var todoListLiveData: MutableLiveData<List<Todo>> = MutableLiveData()
    val toastLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadTodoList() {
        TaskScheduler.execute(object: Task<List<Todo>>() {

            override fun onSuccess(result: List<Todo>) {
                todoListLiveData.value = result
            }

            override fun doInBackground(): List<Todo> {
                return todoDao.getTodoList()
            }

        })
    }

    fun deleteOneTodo(todo: Todo) {
        TaskScheduler.execute {
            todoDao.deleteTodoById(todo.id)
            CalendarReminderUtils.deleteCalendarEvent(BaseApplication.inst, todo.content)
            toastLiveData.postValue("删除成功")
            loadTodoList()
        }
    }
}