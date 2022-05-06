package com.tasks.viemed.util

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String) {
    view?.let { Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show() }
}

fun Activity.showToast(error: String) {
    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
}