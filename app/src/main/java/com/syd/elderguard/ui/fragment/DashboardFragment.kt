package com.syd.elderguard.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.syd.elderguard.R
import com.syd.elderguard.model.*
import com.syd.elderguard.ui.viewmodel.DashboardViewModel
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.koin.android.viewmodel.ext.android.getViewModel


class DashboardFragment : Fragment() {

    private lateinit var balloon: Balloon
    private lateinit var dashboardViewModel: DashboardViewModel
    //0=事件 1=关系
    private var chartType = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = getViewModel<DashboardViewModel>()

        toolbar.setTitle(R.string.title_dashboard)
        dataObserver()
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.getSumAccount()
        dashboardViewModel.intPeopleCount()
        dashboardViewModel.outPeopleCount()
        if (chartType == 0) {
            dashboardViewModel.getEventChartList()
        } else {
            dashboardViewModel.getRelationshipChartList()
        }

    }

    private fun dataObserver() {
        linQueryType.setOnClickListener {
            showBalloonMenu(it)
        }
        dashboardViewModel.outPeopleCountLiveData.observe(viewLifecycleOwner, Observer {
            txvOutPeopleCount.text = "${it}人"
        })
        dashboardViewModel.intPeopleCountLiveData.observe(viewLifecycleOwner, Observer {
            txvInPeopleCount.text = "${it}人"
        })
        dashboardViewModel.sumLiveData.observe(viewLifecycleOwner, Observer {
            loadHeaderData(it)
        })

        dashboardViewModel.eventSumLiveData.observe(viewLifecycleOwner, Observer {
            loadEventChart(it)
        })

        dashboardViewModel.relationshipSumLiveData.observe(viewLifecycleOwner, Observer {
            loadRelationshipChart(it)
        })
    }

    /**
     * 头部数据
     */
    private fun loadHeaderData(list: List<CountSum>) {
        var inSum: CountSum? = null
        var outSum: CountSum? = null
        var profit: Float = 0F
        var total: Float = 0.0F
        list.forEach {
            total +=it.sum
            if (it.outIntype == 1) {
                outSum =it
            } else if(it.outIntype == 2) {
                inSum = it
            }
        }

        //送出
        if (outSum !=null) {
            val outProgress = outSum!!.sum / total
            pgbProgressOut.progress = (outProgress*100).toInt()
            txvOutText.text = "-${outSum!!.sum}"
            profit -= outSum!!.sum
        }
        //收回
        if (inSum !=null) {
            val inProgress = inSum!!.sum / total
            pgbProgressIn.progress = (inProgress*100).toInt()
            txvInText.text = inSum!!.sum.toString()
            profit += inSum!!.sum
        }

        if (profit>=0) {
            txvProfitTitle.text = "盈"
            txvProfitValue.text = profit.toString()
        } else {
            txvProfitTitle.text = "亏"
            txvProfitValue.text = profit.toString()
        }

    }

    private fun loadRelationshipChart(relationshipSumList: List<RelationshipSum>) {
        if (relationshipSumList.isEmpty()) {
            barChart.setNoDataText("暂无数据"); // this is the top line
            barChart.invalidate();
            return
        }
        val yvalue: ArrayList<BarEntry> = ArrayList()
        val xvalue: ArrayList<String> = ArrayList()
        var i =0F
        val hashMap: LinkedHashMap<String, RelationshipChartItem> = LinkedHashMap<String,RelationshipChartItem>()
        relationshipSumList.forEach { eventSum ->
            var tmp = hashMap[eventSum.relationshipName]
            //不存在，先增加
            if (tmp == null)  {
                xvalue.add(eventSum.relationshipName)
                if(eventSum.outIntype == 1) {
                    var eventChartItem = RelationshipChartItem(0F, eventSum.count, eventSum.relationshipName)
                    hashMap[eventSum.relationshipName] = eventChartItem
                } else {
                    var eventChartItem = RelationshipChartItem(eventSum.count, 0F, eventSum.relationshipName)
                    hashMap[eventSum.relationshipName] = eventChartItem
                }
            } else {
                if(eventSum.outIntype == 1) {
                    tmp.outCount = eventSum.count
                    hashMap[eventSum.relationshipName] = tmp
                } else {
                    tmp.inCount = eventSum.count
                    hashMap[eventSum.relationshipName] = tmp
                }

            }
        }

        hashMap.forEach {
            yvalue.add(BarEntry(i, floatArrayOf(it.value.inCount, it.value.outCount),
                null))
            i++
        }

        //X轴坐标
        val xAxis = barChart.xAxis
        xAxis.granularity = 1F
        xAxis.valueFormatter = EventValueFormatter(xvalue)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        if (xvalue.size > 6) {
            xAxis.labelRotationAngle = 300F
        } else {
            xAxis.labelRotationAngle = 0F
        }
        //去掉纵向网格线和顶部边线：
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        //去掉左右边线
        barChart.axisLeft.setDrawAxisLine(false);
        barChart.axisRight.setDrawAxisLine(false);

        val set = BarDataSet(yvalue, "")
        set.stackLabels = arrayOf("收回的", "送出的")
        //红 绿
        set.setColors(*intArrayOf(Color.parseColor("#d7463e"), Color.parseColor("#00b805")))
        val barData = BarData(set)
        //柱状宽带
        barData.barWidth = 0.2F
        barChart.data = barData
        barChart.description = Description().apply {
            text = ""
        }
        barChart.setTouchEnabled(false)
        barChart.setNoDataText("暂无数据");
        barChart.invalidate()
    }

    /**
     * 加载事件图表数据
     */
    private fun loadEventChart(eventSumList: List<EventSum>) {
        if (eventSumList.isEmpty()) {
            barChart.setNoDataText("暂无数据"); // this is the top line
            barChart.invalidate();
            return
        }
        val yvalue: ArrayList<BarEntry> = ArrayList()
        val xvalue: ArrayList<String> = ArrayList()
        var i =0F
        val hashMap: LinkedHashMap<String, EventChartItem> = LinkedHashMap<String,EventChartItem>()
        eventSumList.forEach { eventSum ->
            var tmp = hashMap[eventSum.eventName]
            //不存在，先增加
            if (tmp == null)  {
                xvalue.add(eventSum.eventName)
                if(eventSum.outIntype == 1) {
                    var eventChartItem = EventChartItem(0F, eventSum.count, eventSum.eventName)
                    hashMap[eventSum.eventName] = eventChartItem
                } else {
                    var eventChartItem = EventChartItem(eventSum.count, 0F, eventSum.eventName)
                    hashMap[eventSum.eventName] = eventChartItem
                }
            } else {
                if(eventSum.outIntype == 1) {
                    tmp.outCount = eventSum.count
                    hashMap[eventSum.eventName] = tmp
                } else {
                    tmp.inCount = eventSum.count
                    hashMap[eventSum.eventName] = tmp
                }

            }
        }

        hashMap.forEach {
            yvalue.add(BarEntry(i, floatArrayOf(it.value.inCount, it.value.outCount),
                null))
            i++
        }

        //X轴坐标
        val xAxis = barChart.xAxis
        xAxis.granularity = 1F
        xAxis.valueFormatter = EventValueFormatter(xvalue)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        if (xvalue.size > 6) {
            xAxis.labelRotationAngle = 300F
        } else {
            xAxis.labelRotationAngle = 0F
        }
        //去掉纵向网格线和顶部边线：
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        //去掉左右边线
        barChart.axisLeft.setDrawAxisLine(false);
        barChart.axisRight.setDrawAxisLine(false);

        val set = BarDataSet(yvalue, "")
        set.stackLabels = arrayOf("收回的", "送出的")
        //红 绿
        set.setColors(*intArrayOf(Color.parseColor("#d7463e"), Color.parseColor("#00b805")))
        val barData = BarData(set)
        //柱状宽带
        barData.barWidth = 0.2F
        barChart.data = barData
        barChart.description = Description().apply {
            text = ""
        }
        //barChart.setTouchEnabled(false)
        barChart.isDragEnabled = true;// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//
        barChart.setNoDataText("暂无数据");
        barChart.invalidate()
    }

    private fun showBalloonMenu(v: View) {
        if (::balloon.isInitialized&& balloon.isShowing) {
            balloon.dismiss()
            return
        }

        activity?.let { it ->
            balloon = Balloon.Builder(it)
                .setDismissWhenTouchOutside(true)
                .setLayout(R.layout.pop_query_type)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.5f)
                .setWidth(80)
                .setElevation(0.5f)
                .setCornerRadius(4f)
                .setBackgroundColor(ContextCompat.getColor(activity!!, R.color.black))
                .setBalloonAnimation(BalloonAnimation.CIRCULAR)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()
            val txvQueryByEvent = balloon.getContentView().findViewById<TextView>(R.id.txvQueryByEvent)
            val txvQueryByRelationship = balloon.getContentView().findViewById<TextView>(R.id.txvQueryByRelationship)
            txvQueryByEvent.setOnClickListener {
                chartType = 0
                dashboardViewModel.getEventChartList()
                balloon.dismiss()
            }
            txvQueryByRelationship.setOnClickListener {
                chartType = 1
                dashboardViewModel.getRelationshipChartList()
                balloon.dismiss()
            }

            balloon.showAlignBottom(v)
        }
    }

    /**
     * 事件X坐标轴
     */
    inner class EventValueFormatter(listLables: ArrayList<String>): ValueFormatter() {

        private var listLables: ArrayList<String> = arrayListOf()

        init {
            this.listLables = listLables
        }

        override fun getFormattedValue(value: Float): String {
            if (value.toInt()>=listLables.size) {
                return ""
            }
            return listLables[value.toInt()]
        }
    }
}
