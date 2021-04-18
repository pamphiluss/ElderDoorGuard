package com.syd.elderguard.extensions


import android.content.Context
import android.view.View
import androidx.annotation.DimenRes

/**
 * Created by zyyoona7 on 2017/8/24.
 *
 * 尺寸、尺寸转换 扩展函数
 * dimension extensions
 * https://github.com/zyyoona7/KExtensions
 */
/*
  ---------- Context ----------
 */

/**
 * screen width in pixels
 */
val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * screen height in pixels
 */
val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * returns dip(dp) dimension value in pixels
 * @param value dp
 */
fun Context.dip2px(value: Int): Int = (value * resources.displayMetrics.density).toInt()

fun Context.dip2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()

/**
 * return sp dimension value in pixels
 * @param value sp
 */
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/**
 * converts [px] value into dip or sp
 * @param px
 */
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density

fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

/**
 * return dimen resource value in pixels
 * @param resource dimen resource
 */
fun Context.dimen2px(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)


/*
  ---------- View ----------
 */
val View.screenWidth
    get() = context.screenWidth

val View.screenHeight
    get() = context.screenHeight

fun View.dip2px(value: Int):Int = context.dip2px(value)
fun View.dip2px(value: Float):Int = context.dip2px(value)

fun View.sp2px(value: Int):Int = context.sp2px(value)
fun View.sp2px(value: Float):Int = context.sp2px(value)

fun View.px2dip(px: Int):Float = context.px2dip(px)
fun View.px2sp(px: Int):Float = context.px2sp(px)

fun View.dimen2px(@DimenRes resource: Int):Int = context.dimen2px(resource)