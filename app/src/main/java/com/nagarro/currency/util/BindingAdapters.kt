package com.nagarro.currency.util

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nagarro.currency.listeners.OnItemSelectListener

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("onSpinnerItemSelectListener")
    fun setOnSpinnerItemSelectListener(view: Spinner, listener: OnItemSelectListener) {
        view.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                view?.let { listener.onItemSelected(view, position) }
            }

            override fun onNothingSelected(adapter: AdapterView<*>?) {

            }
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