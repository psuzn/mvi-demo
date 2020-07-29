package me.sujanpoudel.mvidemo.base

import android.app.Dialog
import android.app.ProgressDialog
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.sujanpoudel.mvidemo.helpers.hideKeyBoard

open class BaseBottomSheet : BottomSheetDialogFragment() {
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