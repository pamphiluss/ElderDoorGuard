package com.syd.elderguard.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.timePicker
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.extensions.*
import com.syd.elderguard.model.Todo
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.viewmodel.TodoAddViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_todo_add.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.util.*


class TodoAddActivity : BaseActivity(), View.OnClickListener {

    private lateinit var todoAddViewModel: TodoAddViewModel
    private lateinit var oldTodo: Todo

    override fun getLayoutId(): Int {
        return R.layout.activity_todo_add
    }

    override fun getToolbarTitleId(): Int {
        if(::oldTodo.isInitialized) {
            return R.string.title_todo_edit
        }
        return R.string.title_todo_add
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val tmpTodo = intent.getParcelableExtra<Todo>(Constant.BUNDLE_TODO)
        if (tmpTodo != null) {
            oldTodo = tmpTodo
        }

        super.onCreate(savedInstanceState)
        todoAddViewModel = getViewModel<TodoAddViewModel>()
        todoAddViewModel.toastLiveData.observe(this, androidx.lifecycle.Observer {
            if (it == "保存成功") {
                showLongToast(it)
                finish()
            }
        })

        if (::oldTodo.isInitialized) {
            //编辑
            txvTodoAddDate.text = Date().timeStamp2Date(oldTodo.dateTime, "yyyy-MM-dd")
            txvTodoAddTime.text = Date().timeStamp2Date(oldTodo.dateTime, "HH:mm")
            edtTodoAddContent.text = oldTodo.content.toEditable()
        } else {
            txvTodoAddDate.text = Date().getToday()
            txvTodoAddTime.text = Date().getCurrentTime()
        }


        txvTodoAddDate.setOnClickListener(this)
        txvTodoAddTime.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.txvTodoAddDate-> {
                onChoiceDate()
            }
            R.id.txvTodoAddTime-> {
                onChoiceTime()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_save_account-> {
                AndPermission.with(this)
                    .runtime()
                    .permission(Permission.Group.CALENDAR)
                    .onGranted {
                        addOneTodo()
                    }
                    .onDenied { }
                    .start()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addOneTodo() {
        val content = edtTodoAddContent.text.toString()
        if (content.isEmpty()) {
            showLongToast("请输入提醒内容")
            return
        }

        val dateTime = Date().transToTimeStamp("${txvTodoAddDate.text} ${txvTodoAddTime.text}:00")
        if (::oldTodo.isInitialized) {
            val oldContent = oldTodo.content
            oldTodo.content = content
            oldTodo.dateTime = dateTime
            todoAddViewModel.addOneTodo(oldTodo, oldContent)
        } else {
            val todo = Todo(content, dateTime)
            todoAddViewModel.addOneTodo(todo)
        }

    }

    private fun onChoiceDate() {
        MaterialDialog(this).show {
            datePicker { _, date ->
                this@TodoAddActivity.txvTodoAddDate.text = "${date.year}-${date.month+1}-${date.dayOfMonth}"
            }
        }
    }

    private fun onChoiceTime() {
        MaterialDialog(this).show {
            timePicker { _, datetime ->
                this@TodoAddActivity.txvTodoAddTime.text = "${datetime.get(Calendar.HOUR_OF_DAY)}:${datetime.get(Calendar.MINUTE)}"
            }
        }
    }
}