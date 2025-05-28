package com.github.skytoph.simpleweather.core.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.github.skytoph.simpleweather.R

abstract class BaseBindingFragment<B : ViewBinding> : Fragment() {

    private var _binding: B? = null
    protected val binding: B get() = _binding!!

    protected abstract val bindingInflation: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = bindingInflation.invoke(inflater, container, false)
        return binding.root
    }

    protected fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun Toolbar.addBackNavigation(onClick: View.OnClickListener) {
        setNavigationIcon(R.drawable.ic_back)
        setNavigationOnClickListener(onClick)
    }

    protected fun Toolbar.removeNavigationIcon() {
        setNavigationIcon(null)
    }
}

abstract class BaseFragment<M : ViewModel, B : ViewBinding> : BaseBindingFragment<B>() {
    protected abstract val viewModel: M
}