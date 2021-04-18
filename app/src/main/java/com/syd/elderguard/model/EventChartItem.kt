package com.syd.elderguard.model

data class EventChartItem(
    var inCount: Float,//记账金额
    var outCount: Float,
    val eventName: String//事件
)