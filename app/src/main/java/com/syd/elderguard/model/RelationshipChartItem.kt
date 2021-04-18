package com.syd.elderguard.model

data class RelationshipChartItem(
    var inCount: Float,//记账金额
    var outCount: Float,
    val relationshipName: String//事件
)