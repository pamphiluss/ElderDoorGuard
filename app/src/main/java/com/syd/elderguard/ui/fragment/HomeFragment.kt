package com.syd.elderguard.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.jeremyliao.liveeventbus.LiveEventBus
import com.syd.elderguard.BaseApplication
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.extensions.getCurrentDate
import com.syd.elderguard.extensions.getToday_Chinaname
import com.syd.elderguard.extensions.toPx
import com.syd.elderguard.ui.activity.AddAccountActivity
import com.syd.elderguard.ui.activity.SearchActivity
import com.syd.elderguard.ui.adapter.HomeAdapter
import com.syd.elderguard.ui.dialog.ActionSheetDialog
import com.syd.elderguard.ui.dvider.BottomLine
import com.syd.elderguard.ui.viewmodel.HomeViewModel
import com.syd.elderguard.utils.Utils
import com.syd.elderguard.works.SyncDataScheduler
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.getViewModel
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var emptyView: View
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private var outIntype: Int = 1
    private lateinit var txvHomeTotal: TextView
    private lateinit var syncDownloadCloud: LottieAnimationView
    private lateinit var txvHomeMessage: TextView
    private lateinit var imvHomeThemeImage: ImageView
    private var sort: Int = 4
    private var bannerHeight = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHomeList.layoutManager = LinearLayoutManager(activity)
        homeAdapter = HomeAdapter();
        homeAdapter.setOnItemClickListener { _, _, position ->
            val account = homeAdapter.data[position]
            startActivity(Intent(activity, AddAccountActivity::class.java)
                .putExtra(Constant.BUNDLE_ACCOUNT_MANAGER, account))
        }
        activity?.let { BottomLine(it) }?.let { rvHomeList.addItemDecoration(it) }
        rvHomeList.adapter = homeAdapter

        homeViewModel = getViewModel<HomeViewModel>().apply{
            initBaseData()
            loadAccountList(outIntype, sort)
            //requestSoupHappay()
        }

        dataObserver()
        addHeaderView()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.loadAccountList(outIntype, sort)
        updateCover()
    }

    private fun dataObserver() {
        homeViewModel.accoutListLiveData.observe(viewLifecycleOwner, Observer { list ->
            homeAdapter.setNewData(list)
            var total = 0.0
            list.forEach {
                total+=it.count
            }

            txvHomeTotal.text = total.toString()
            if (total == 0.0) {
                addEmptyHeaderView()
            } else {
                if (::emptyView.isInitialized) homeAdapter.removeHeaderView(emptyView)
            }
        })

        txvHomeTitle.setOnClickListener {
            if (outIntype == 1) {
                txvHomeTitle.text = getString(R.string.title_take_back)
                outIntype = 2
            } else {
                txvHomeTitle.text = getString(R.string.title_send_out)
                outIntype = 1
            }

            changeTitleArrow()
            homeViewModel.loadAccountList(outIntype, sort)
        }

        imvHomeSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        imvHomeSort.setOnClickListener {
            showListSortDialog()
        }

        rvHomeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var totalDy = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalDy += dy
                if (totalDy <= bannerHeight) {
                    imvHomeTitleArrow.setColorFilter(Color.WHITE)
                    imvHomeSearch.setColorFilter(Color.WHITE)
                    imvHomeSort.setColorFilter(Color.WHITE)
                    txvHomeTitle.setTextColor(ContextCompat.getColor(BaseApplication.inst, R.color.mywhite))
                    val alpha: Float = totalDy.toFloat() / bannerHeight
                    linTitleBar.setBackgroundColor(
                        ColorUtils.blendARGB(
                            Color.TRANSPARENT
                            , ContextCompat.getColor(BaseApplication.inst, R.color.white), alpha
                        )
                    )
                } else {
                    imvHomeSearch.setColorFilter(Color.BLACK)
                    imvHomeSort.setColorFilter(Color.BLACK)
                    imvHomeTitleArrow.setColorFilter(Color.BLACK)
                    txvHomeTitle.setTextColor(ContextCompat.getColor(BaseApplication.inst, R.color.black))
                    linTitleBar.setBackgroundColor(
                        ColorUtils.blendARGB(
                            Color.TRANSPARENT
                            , ContextCompat.getColor(BaseApplication.inst, R.color.white), 1f
                        )
                    )
                }
            }
        })

        homeViewModel.soupLiveData.observe(viewLifecycleOwner, Observer {
            txvHomeMessage.text = it
        })

        eventBusObserver()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun eventBusObserver() {
        //登录成功，同步云端任务
        LiveEventBus.get(Constant.EVENT_LOGIN_SUCCESS, Boolean::class.java)
            .observe(this, Observer<Boolean> {
                if (it) {
                    syncDownloadCloud.visibility = View.VISIBLE
                    syncDownloadCloud.playAnimation()
                    SyncDataScheduler.refreshDownloadTimeWork(viewLifecycleOwner)
                }
            })

        //同步完成
        LiveEventBus.get(Constant.EVENT_SYNC_DOWNLOAD_LESS_COUNTER, Int::class.java)
            .observe(this, Observer<Int> {
                if (it == 0) {
                    syncDownloadCloud.visibility = View.GONE
                    syncDownloadCloud.pauseAnimation()
                    //重新加载一次数据
                    homeViewModel.loadAccountList(outIntype, sort)
                }
            })
    }

    private fun changeTitleArrow() {
        if (outIntype == 1) {
            imvHomeTitleArrow.setImageResource(R.drawable.ic_arrow_drop_down)
        } else{
            imvHomeTitleArrow.setImageResource(R.drawable.ic_arrow_drop_up)
        }

    }

    private fun showListSortDialog() {
        ActionSheetDialog(activity)
            .builder()
            .setCancelable(true)
            .setCanceledOnTouchOutside(true)
            .addSheetItem(getString(R.string.lable_count_asc),
                if(sort == 1) ActionSheetDialog.SheetItemColor.Red else ActionSheetDialog.SheetItemColor.Gray
            ) {
                 sort = 1
                homeViewModel.loadAccountList(outIntype, sort)
            }.addSheetItem(getString(R.string.lable_count_desc),
                if(sort == 2) ActionSheetDialog.SheetItemColor.Red else ActionSheetDialog.SheetItemColor.Gray
            ) {
                sort = 2
                homeViewModel.loadAccountList(outIntype, sort)
            }.addSheetItem(getString(R.string.lable_time_asc),
                if(sort == 3) ActionSheetDialog.SheetItemColor.Red else ActionSheetDialog.SheetItemColor.Gray
            ) {
                sort = 3
                homeViewModel.loadAccountList(outIntype, sort)
            }.addSheetItem(getString(R.string.lable_time_desc),
                if(sort == 4) ActionSheetDialog.SheetItemColor.Red else ActionSheetDialog.SheetItemColor.Gray
            ) {
                sort = 4
                homeViewModel.loadAccountList(outIntype, sort)
            }.show()
    }

    /**
     * 头部内容
     */
    private fun addHeaderView() {
        val layoutInflater = LayoutInflater.from(activity)
        val headerView = layoutInflater.inflate(R.layout.header_home, rvHomeList.parent as ViewGroup, false)
        txvHomeTotal = headerView.findViewById(R.id.txvHomeTotal)
        txvHomeMessage = headerView.findViewById(R.id.txvHomeMessage)
        imvHomeThemeImage = headerView.findViewById(R.id.imvHomeThemeImage)
        txvHomeMessage.text = Date().getToday_Chinaname()+" "+Date().getCurrentDate()
        syncDownloadCloud = headerView.findViewById(R.id.syncDownloadCloud)
        updateCover()
        homeAdapter.addHeaderView(headerView)
        val bannerParams: ViewGroup.LayoutParams = headerView.layoutParams
        val titleBarParams: ViewGroup.LayoutParams = homeToolbar.layoutParams
        bannerHeight =
            bannerParams.height - titleBarParams.height - ImmersionBar.getStatusBarHeight(this)
    }

    /**
     * 更新封面
     */
    private fun updateCover() {
        if (imvHomeThemeImage == null) return

        //显示预览图
        val coverIndex = MMKV.defaultMMKV().decodeString(Constant.SP_COVER_INDEX, "5")
        if (coverIndex == "-1") {
            val customPath = MMKV.defaultMMKV().decodeString(Constant.SP_COVER_CUSTOM_PATH)
            Glide.with(this).load(customPath).into(imvHomeThemeImage)
            return
        }
        var drawable = Utils.getDrawableResource(activity, "ic_header_theme_${coverIndex}")
        imvHomeThemeImage.setImageResource(drawable)
    }
    /**
     * 空内容
     */
    private fun addEmptyHeaderView() {
        if (::emptyView.isInitialized) {
            homeAdapter.removeHeaderView(emptyView)
        }
        val layoutInflater = LayoutInflater.from(activity)
        emptyView = layoutInflater.inflate(R.layout.empty_view, rvHomeList.parent as ViewGroup, false)
        emptyView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        emptyView.layoutParams.height = resources.displayMetrics.heightPixels - bannerHeight - 50.toPx()
        homeAdapter.addHeaderView(emptyView)
    }

}


