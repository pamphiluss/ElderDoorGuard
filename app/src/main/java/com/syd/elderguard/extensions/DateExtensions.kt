package com.syd.elderguard.extensions

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

//获取date对象
var data: Date = Date()
//获取日历对象
var calendar: Calendar = Calendar.getInstance()

//获取今天是星期几
fun Date.getToday_Englishname(): String {
    var list = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    calendar.time = data
    var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (index < 0) {
        index = 0
    }
    return list[index]
}

//获取今天是星期几
fun Date.getToday_Chinaname(): String {
    var list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    var calendar: Calendar = Calendar.getInstance()
    calendar.time = data
    var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (index < 0) {
        index = 0
    }
    return list[index]
}

//获取当前日期
fun Date.getToday(): String {
    //需要得到的格式
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(data)
}

fun Date.getCurrentDate(): String {
    //需要得到的格式
    var sdf: Format = SimpleDateFormat("MM月dd日")
    return sdf.format(data)
}

fun Date.getCurrentTime(): String {
    //需要得到的格式
    var sdf: Format = SimpleDateFormat("HH:mm")
    return sdf.format(data)
}

fun Date.getCurrentYear(): String {
//需要得到的格式
    var sdf: Format = SimpleDateFormat("yyyy")
    return sdf.format(data)
}

//获取上周的今天的日期
fun Date.getlastweekToday(): String {
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    val t: Long = calendar.getTimeInMillis()
    val l: Long = t - 24 * 3600 * 1000 * 7
    return sdf.format(l)
}

//获取上个月今天的日期
fun Date.getlastmonthToday(): String {
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    calendar.add(Calendar.MONTH, -1)
    return sdf.format(calendar.time)
}

//获取昨天的日期
fun Date.getDateofYesterday(): String? {
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    val t: Long = calendar.getTimeInMillis()
    val l: Long = t - 24 * 3600 * 1000
    var d: Date = Date(l)
    return sdf.format(d)
}

//获取上个月的第一天
fun Date.getFirstDayOfLastMonth(): String {
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    calendar.set(Calendar.DATE, 1)
    calendar.add(Calendar.MONTH, -1)
    return sdf.format(calendar.getTime())
}

//获取上个月的最后一天
fun Date.getLastDayOfLastMonth(): String {
    var sdf: Format = SimpleDateFormat("yyyy-MM-dd")
    calendar.set(Calendar.DATE, 1)
    calendar.add(Calendar.MONTH, -1)
    calendar.roll(Calendar.DATE, -1)
    return sdf.format(calendar.getTime());
}

//判断是否是闰年
fun Date.isLeapYear(year: Int): Boolean {
    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
        return true
    }
    return false
}


// 字符串日期 获取想要格式的日期格式，栗子："2017—10-10 10:10:10"
fun Date.getTime4String(time: String): String {
    //代转日期的字符串格式(输入的字符串格式)
    var inputsdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    //获取想要的日期格式(输出的日期格式)
    var outputsdf: SimpleDateFormat = SimpleDateFormat("HH:mm")
    var date: Date = inputsdf.parse(time)
    return outputsdf.format(date)
}


// 判断两个日期大小  如，第一个日期大于第二个日期，返回true  反之false
fun Date.isDateOneBigger(str1: String, str2: String): Boolean {
    var isBigger: Boolean = false
    //输入的格式，选择性更改
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    isBigger = sdf.parse(str1).time >= sdf.parse(str2).time
    return isBigger
}

//获取某个日期的前一天
fun Date.getSpecifiedDayBefore(specifiedDay: String): String {
    //输出的日期格式
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    //自定义过来的String格式的日期
    var date: Date = SimpleDateFormat("yyyy/MM/dd").parse(specifiedDay)
    calendar.time = date
    var day = calendar.get(Calendar.DATE)
    calendar.set(Calendar.DATE, day - 1)
    return sdf.format(calendar.time)
}


//获取某个日期的后一天
fun Date.getSpecifiedDayAfter(specifiedDay: String): String {
    //输出的日期格式
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    //自定义过来的String格式的日期
    var date: Date = SimpleDateFormat("yyyy/MM/dd").parse(specifiedDay)
    calendar.time = date
    var day = calendar.get(Calendar.DATE)
    calendar.set(Calendar.DATE, day + 1)
    return sdf.format(calendar.time)
}

fun Date.transToString(time:Long):String {
    return SimpleDateFormat("yyyy-MM-dd").format(time)
}

fun Date.transToTimeStamp(date:String): Long {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).time
}

/**
 * 按指定格式转换时间戳
 * @param time 时间戳
 * @param format 格式，如yyyy-MM-dd HH:mm
 */
fun Date.timeStamp2Date(time: Long, format: String): String {
    var sdf: SimpleDateFormat = SimpleDateFormat(format)
    return sdf.format(Date(time))
}

fun Date.timeStamp2Date(time: Long): String {
    return timeStamp2Date(time, "yyyy-MM-dd HH:mm")
}