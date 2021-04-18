package com.syd.elderguard.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.model.Todo
import com.syd.elderguard.ui.adapter.TodoListAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.ui.viewmodel.TodoListViewModel
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_todo_list.*
import org.koin.android.viewmodel.ext.android.getViewModel

class TodoListActivity : BaseActivity() {

    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var todoListAdapter: TodoListAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_todo_list
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_todo_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoListViewModel = getViewModel<TodoListViewModel>()
        dataObserver()

        rvTodoList.layoutManager = LinearLayoutManager(this)
        todoListAdapter = TodoListAdapter()
        todoListAdapter.setOnItemClickListener { _, _, position ->
            val todo = todoListAdapter.data[position]
            if (todo.dateTime<System.currentTimeMillis()) {
                showLongToast("过期事项，请删除")
                return@setOnItemClickListener
            }

            //跳转到编辑
            startActivity(Intent(this@TodoListActivity, TodoAddActivity::class.java)
                .putExtra(Constant.BUNDLE_TODO, todo))

        }

        rvTodoList.addItemDecoration(BottomLine(this))
        rvTodoList.adapter = todoListAdapter
        todoListAdapter.setOnItemLongClickListener { adapter, view, position ->
            val todo = todoListAdapter.data[position]
            AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.CALENDAR)
                .onGranted {
                    showDeleteTodoDialog(todo)
                }
                .onDenied { }
                .start()
            return@setOnItemLongClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        todoListViewModel.loadTodoList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_add_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add_event -> {
                startActivity(Intent(this@TodoListActivity, TodoAddActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun dataObserver() {
        todoListViewModel.todoListLiveData.observe(this, Observer {
            if (it.isEmpty()) {
                todoListAdapter.setNewData(it)
                todoListAdapter.emptyView = LayoutInflater.from(this@TodoListActivity)
                    .inflate(R.layout.empty_todo_list, rvTodoList as ViewGroup, false)
            } else {
                todoListAdapter.setNewData(it)
            }
        })

        todoListViewModel.toastLiveData.observe(this, Observer {
            showLongToast(it)
        })
    }

    private fun showDeleteTodoDialog(todo: Todo) {
        MaterialDialog(this).show {
            title(R.string.dialog_title_delete_todo)
            message(text = "你确定要删除该事项吗？")
            negativeButton(R.string.lable_cancle)
            positiveButton(R.string.lable_ok) {
                todoListViewModel.deleteOneTodo(todo)
            }
        }
    }
}