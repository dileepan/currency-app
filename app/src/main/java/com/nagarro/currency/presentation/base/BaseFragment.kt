package com.nagarro.currency.presentation.base

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nagarro.currency.R
import com.nagarro.currency.architecture.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel, V : ViewDataBinding>(clazz: KClass<VM>) : Fragment() {
    lateinit var binding: V
    lateinit var progressSpinner: Dialog
    abstract val layoutResId: Int
    abstract val bindingVariable: Int

    open val viewModel by viewModelForClass<VM>(clazz = clazz)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreateView()
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    open fun onCreateView() {
        // override to implement anything that requires to be implemented inside onCreateView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(bindingVariable, viewModel)
        binding.executePendingBindings()
        progressSpinner = getProgress()
        hideKeyboard()
        initViewSubscriptions()
        configSystemUi(screenType())
    }

    private fun initViewSubscriptions() {
        viewModel.progressObserver.observe(viewLifecycleOwner) { show ->
            activity?.runOnUiThread {
                when (show) {
                    true -> progressSpinner.show()
                    false -> progressSpinner.dismiss()
                }
            }
        }

        viewModel.navigateEvent.observe(viewLifecycleOwner) { nc ->
            when (nc) {
                is NavigationCommand.To -> findNavController().navigate(nc.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    nc.destinationId,
                    nc.inclusive
                )
            }
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Error Code: ${it.code}")
                .setMessage(it.message)
                .create()
                .show()
        }
    }

    fun configSystemUi(screenType: ScreenType) {
        activity?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, screenType() == ScreenType.NORMAL)
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            insetsController.let {
                when (screenType) {
                    ScreenType.IMMERSIVE -> {
                        it.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                        it.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
                    }
                    else -> it.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
                }
                it.isAppearanceLightStatusBars = isLightStatusBar()
                it.isAppearanceLightNavigationBars = isLightNavBar()
                window.statusBarColor = systemUiColor()
                window.navigationBarColor = systemUiColor()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    window.navigationBarDividerColor =
                        ContextCompat.getColor(requireContext(), R.color.light_grey)
                }
            }
        }
    }

    fun showKeyboard(view: EditText) {
        view.requestFocus()
        requireActivity().getSystemService<InputMethodManager>()?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        requireActivity().getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun systemUiColor() = ContextCompat.getColor(
        requireContext(), when (isLightStatusBar()) {
            true -> R.color.white
            false -> R.color.black
        }
    )

    private fun getProgress() = Dialog(requireContext()).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(ProgressBar(context))
    }

    open fun screenType() = ScreenType.NORMAL

    open fun isLightStatusBar() = true
    open fun isLightNavBar() = isLightStatusBar()

    open fun onBackPressed(): Boolean = false

    enum class ScreenType {
        FULLSCREEN, IMMERSIVE, NORMAL
    }
}