package me.sujanpoudel.mvidemo.base

import android.app.Dialog
import android.app.ProgressDialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.sujanpoudel.mvidemo.helpers.hideKeyBoard

open class BaseFragment : Fragment() {
    private var loadingProgressBar: Dialog? = null

    protected fun showLoadingDialog(title: String = "Loading...") {
        hideKeyBoard()
        loadingProgressBar = loadingProgressBar ?: ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setTitle(title)
        }
        loadingProgressBar?.show()
    }

    protected fun hideLoadingDialog() {
        loadingProgressBar?.dismiss()
        loadingProgressBar = null
    }

    protected fun toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}