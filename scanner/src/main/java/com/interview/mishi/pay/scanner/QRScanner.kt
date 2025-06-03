package com.interview.mishi.pay.scanner

import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView


object QRScanner {

    private lateinit var currentActivity: ComponentActivity

    fun init(activity: ComponentActivity) {
        currentActivity = activity
    }

    fun scanProduct(result: (ScanResult) -> Unit) {
        val view = ComposeView(currentActivity)
        view.setContent {
            CameraUtils.OpenCamera {
                result(it)
                removeView(view)
            }
        }
        addView(view)
    }

    private fun addView(view: View)
    {
        (currentActivity.findViewById<View>(android.R.id.content)?.rootView as ViewGroup).addView(view)
    }

    private fun removeView(view: View)
    {
        val parent = view.parent as ViewGroup
        parent.removeView(view)
    }
}