package com.nagarro.currency.presentation.details

import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.databinding.DetailsFragmentBinding
import com.nagarro.currency.presentation.base.BaseFragment

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>(DetailsViewModel::class) {

    override val layoutResId: Int
        get() = R.layout.details_fragment

    override val bindingVariable: Int
        get() = BR.viewModel
}