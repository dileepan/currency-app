package com.nagarro.currency.presentation.details

import android.os.Bundle
import android.view.View
import androidx.navigation.NavArgsLazy
import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.databinding.DetailsFragmentBinding
import com.nagarro.currency.presentation.base.BaseFragment

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>(DetailsViewModel::class) {

    private val args by NavArgsLazy(DetailsFragmentArgs::class) {
        arguments ?: throw IllegalStateException("Fragment $this has null arguments")
    }

    override val layoutResId: Int
        get() = R.layout.details_fragment

    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadData(args.fromCurrency, args.toCurrency)
    }
}