package me.sujanpoudel.mvidemo.helpers

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


fun Disposable.into(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var TextView.txt
    get() = this.text.toString()
    set(value) {
        if (this.text.toString() != value)
            this.text = value
    }


fun Fragment.hideKeyBoard() {
    val imm: InputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    val view = activity!!.currentFocus ?: View(activity)
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

val View.layoutInflater: LayoutInflater get() = LayoutInflater.from(this.context)


