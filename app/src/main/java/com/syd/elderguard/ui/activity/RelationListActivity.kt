package com.syd.elderguard.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.ui.adapter.RelationshipAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.ui.viewmodel.RelationshipListViewModel
import kotlinx.android.synthetic.main.activity_event_list.*
import org.koin.android.viewmodel.ext.android.getViewModel

class RelationListActivity : BaseActivity() {

    private lateinit var relationshipAdapter: RelationshipAdapter
    private lateinit var relationshipListViewModel: RelationshipListViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_event_list
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_relationship
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvEventList.layoutManager = LinearLayoutManager(this@RelationListActivity)
        relationshipAdapter = RelationshipAdapter();
        relationshipAdapter.setOnItemClickListener { _, _, position ->
            setResult(Activity.RESULT_OK,
                Intent().putExtra(Constant.BUNDLE_RELATIONSHIP, relationshipAdapter.getItem(position)))
            finish()
        }
        rvEventList.addItemDecoration(BottomLine(this@RelationListActivity))
        rvEventList.adapter = relationshipAdapter
        relationshipListViewModel = getViewModel<RelationshipListViewModel>().apply{
            loadRelationshipList()
        }

        relationshipListViewModel.eventListLiveData.observe(this, Observer {
            relationshipAdapter?.setNewData(it)
        })

        relationshipListViewModel.toastLiveData.observe(this, Observer {
            showLongToast(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_add_event_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add_event -> {
                showAddRelationshipDialog()
            }
            else-> {}

        }

        return super.onOptionsItemSelected(item)
    }

    private fun showAddRelationshipDialog() {
        MaterialDialog(this).show {
            title(R.string.lable_custom_add)
            input(maxLength = 8, hintRes = R.string.hint_relation_name) { dialog, text ->
                relationshipListViewModel.addCustomRelationship(text.toString())
            }
            positiveButton(R.string.lable_ok)
        }

    }

}

