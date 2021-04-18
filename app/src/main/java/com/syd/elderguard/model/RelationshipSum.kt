package com.syd.elderguard.model

data class RelationshipSum(
    val count: Float,//记账金额
    val outIntype: Int,//支出/收入 1：支出  2：收入
    val relationshipName: String//事件
)