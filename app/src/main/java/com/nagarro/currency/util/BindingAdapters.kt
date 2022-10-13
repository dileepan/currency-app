package com.nagarro.currency.util

import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("onItemSelectListener")
    fun setOnItemSelectListener(view: Spinner, onClickItem: (Int) -> Unit) {
        view.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            onClickItem(i)
        }
    }
}