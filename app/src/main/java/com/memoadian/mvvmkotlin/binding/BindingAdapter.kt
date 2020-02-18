package com.memoadian.mvvmkotlin.binding

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide (v: View, show: Boolean) {
        //v.visibility = if (show) View.VISIBLE else View.GONE
        v.visibility = if(show) View.VISIBLE else View.GONE
    }
}