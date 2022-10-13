package com.nagarro.currency.presentation.convert

import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.databinding.ConvertFragmentBinding
import com.nagarro.currency.presentation.base.BaseFragment

class ConvertFragment : BaseFragment<ConvertViewModel, ConvertFragmentBinding>(ConvertViewModel::class) {

    override val layoutResId: Int
        get() = R.layout.convert_fragment

    override val bindingVariable: Int
        get() = BR.viewModel
}