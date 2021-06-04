package com.example.miketoide.domain.animation

import android.app.Dialog
import android.content.Context
import android.util.Log
import com.example.miketoide.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class AnimationApiDialog @Inject constructor(
    @ActivityContext private val activityContext: Context
) : IAnimationApi
{
    companion object {
        private val TAG = AnimationApiDialog::class.java.simpleName
    }

    private lateinit var dialog : Dialog

    override fun showLoading() {
        Log.d(TAG, "show()")
        dialog = Dialog(activityContext).apply {
            setContentView(R.layout.dialog_loading)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            show()
        }
    }

    override fun hideLoading() {
        Log.d(TAG, "hide()")
        dialog.dismiss()
    }
}