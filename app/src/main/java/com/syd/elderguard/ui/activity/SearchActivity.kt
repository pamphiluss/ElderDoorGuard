package com.syd.elderguard.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.syd.elderguard.R
import com.syd.elderguard.ui.adapter.HomeAdapter
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dvider.Y_Divider
import com.syd.elderguard.ui.dvider.Y_DividerBuilder
import com.syd.elderguard.ui.dvider.Y_DividerItemDecoration
import com.syd.elderguard.ui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.getViewModel

class SearchActivity : BaseActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var homeAdapter: HomeAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvSearchList.layoutManager = LinearLayoutManager(this)
        homeAdapter = HomeAdapter();
        homeAdapter.setOnItemClickListener { _, _, position ->

        }
        rvSearchList.addItemDecoration(DividerItemDecoration(this))
        rvSearchList.adapter = homeAdapter
    }

    override fun initData() {
        super.initData()
        searchViewModel = getViewModel<SearchViewModel>()

        edtSearchWord.addTextChangedListener {
            val word = it.toString().trim()
            searchViewModel.loadAccountList(word)
        }

        searchViewModel.accoutListLiveData.observe(this, Observer {
            Log.i("查询", it.size.toString())
            homeAdapter.setNewData(it)
        })
    }

    inner class DividerItemDecoration(context: Context) : Y_DividerItemDecoration(context){

        override fun getDivider(itemPosition: Int): Y_Divider? {
            return Y_DividerBuilder()
                .setBottomSideLine(true, 0xffe0e0e0.toInt(), 1F, 10F, 10F)
                .create()
        }
    }
}