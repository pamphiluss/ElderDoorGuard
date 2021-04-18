package com.syd.elderguard.ui.base

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty

open class BaseFragment : Fragment() {

    fun showLongToast(message: String) {
        activity?.let { Toasty.normal(it, message).show() };
    }

    open fun showLongToast(@StringRes resId: Int) {
        activity?.let { Toasty.normal(it, getString(resId)).show() }
    }
}