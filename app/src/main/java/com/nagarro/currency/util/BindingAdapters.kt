package com.nagarro.currency.util

import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("onSpinnerItemSelectListener")
    fun setOnSpinnerItemSelectListener(view: Spinner, onClickItem: (pos: Int) -> Unit) {
        view.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            onClickItem(i)
        }
    }

    @JvmStatic
    @BindingAdapter("rateText")
    fun setRateText(view: TextView, rate: Double) {
        view.text = String.format("%.6f", rate)
    }

    @JvmStatic
    @BindingAdapter("invRateText")
    fun setInverseRateText(view: TextView, rate: Double) {
        view.text = String.format("%.6f", 1.00 / rate)
    }
}