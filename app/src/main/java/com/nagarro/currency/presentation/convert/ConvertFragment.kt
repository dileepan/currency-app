package com.nagarro.currency.presentation.convert

import android.os.Bundle
import android.view.View
import com.nagarro.currency.BR
import com.nagarro.currency.R
import com.nagarro.currency.databinding.ConvertFragmentBinding
import com.nagarro.currency.presentation.base.BaseFragment

class ConvertFragment : BaseFragment<ConvertViewModel, ConvertFragmentBinding>(ConvertViewModel::class) {

    override val layoutResId: Int
        get() = R.layout.convert_fragment

    override val bindingVariable: Int
        get() = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.items.isEmpty()) viewModel.init()

        viewModel.swapEvent.observe(viewLifecycleOwner) {
            val fromListener = binding.fromCurrency.onItemSelectedListener
            val toListener = binding.toCurrency.onItemSelectedListener

            binding.fromCurrency.onItemSelectedListener = null
            binding.toCurrency.onItemSelectedListener = null

            binding.fromCurrency.setSelection(it.first)
            binding.toCurrency.setSelection(it.second)

            binding.fromCurrency.onItemSelectedListener = fromListener
            binding.toCurrency.onItemSelectedListener = toListener
        }
    }
}