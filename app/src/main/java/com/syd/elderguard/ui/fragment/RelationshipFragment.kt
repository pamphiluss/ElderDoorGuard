package com.syd.elderguard.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.syd.elderguard.R
import com.syd.elderguard.model.RelationshipManager
import com.syd.elderguard.ui.adapter.RelationshipManagerAdapter
import com.syd.elderguard.ui.base.BaseFragment
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.ui.viewmodel.RelationshipViewModel
import kotlinx.android.synthetic.main.fragment_relationship.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.koin.android.viewmodel.ext.android.getViewModel

class RelationshipFragment : BaseFragment() {

    private lateinit var relationshipManagerAdapter: RelationshipManagerAdapter
    private lateinit var relationshipViewModel: RelationshipViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relationship, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRelationshipManager.layoutManager = LinearLayoutManager(activity)
        relationshipManagerAdapter = RelationshipManagerAdapter();
        relationshipManagerAdapter.setOnItemClickListener { _, _, position ->

        }
        activity?.let { BottomLine(it) }?.let { rvRelationshipManager.addItemDecoration(it) }
        rvRelationshipManager.adapter = relationshipManagerAdapter
        relationshipManagerAdapter.setOnItemLongClickListener { adapter, view, position ->
            val relationshipManager = relationshipManagerAdapter.data[position]
            if (relationshipManager.relationshipId <=6) return@setOnItemLongClickListener true
            showDeleteRelationshipDialog(relationshipManager)
            return@setOnItemLongClickListener true
        }
        relationshipViewModel = getViewModel<RelationshipViewModel>().apply{
            fetchDisneyPosterList()
        }

        relationshipViewModel.posterListLiveData.observe(viewLifecycleOwner, Observer {
            //未知移动到最后
            var unknownRelationship: RelationshipManager? = null
            val newRelationshipList = mutableListOf<RelationshipManager>()
            it.forEach { rs ->
               if (rs.relationship == "未知") {
                   unknownRelationship = rs
               } else {
                   newRelationshipList.add(rs)
               }
            }

            newRelationshipList.add(unknownRelationship!!)
            relationshipManagerAdapter.setNewData(newRelationshipList)
        })

        relationshipViewModel.toastLiveData.observe(viewLifecycleOwner, Observer {
            showLongToast(it)
        })

        toolbar.setTitle(R.string.title_relationship_manager)
        toolbar.inflateMenu(R.menu.custom_add_relationship_menu)
        toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.item_add_relationship -> {
                    showAddCustomRelationshipDialog()
                }
            }

            return@setOnMenuItemClickListener true
        }
    }

    override fun onResume() {
        super.onResume()
        relationshipViewModel.fetchDisneyPosterList()
    }

    private fun showDeleteRelationshipDialog(relationshipManager: RelationshipManager) {
        if (relationshipManager.peopleCount >0 || relationshipManager.relationshipCount >0) {
            activity?.let {
                MaterialDialog(it).show {
                    title(R.string.dialog_title_tips)
                    message(R.string.dialog_message_cannot_delete_relationship)
                    positiveButton(R.string.lable_i_know)
            }
            return
        }}
        activity?.let {
            MaterialDialog(it).show {
                title(R.string.dialog_title_delete_relationship)
                message(text = "你确定要删除关系‘${relationshipManager.relationship}’吗？")
                negativeButton(R.string.lable_cancle)
                positiveButton(R.string.lable_ok) {
                    relationshipViewModel.deleteCustomRelationshipName(relationshipManager.relationship)
                }
            }
        }
    }

    private fun showAddCustomRelationshipDialog() {
        activity?.let {
            MaterialDialog(it).show {
                title(R.string.lable_custom_add)
                input(maxLength = 8, hintRes = R.string.hint_relation_name) { dialog, text ->
                    relationshipViewModel.addCustomRelationship(text.toString())
                }
                positiveButton(R.string.lable_ok)
            }
        }

    }

}