package com.alexsanderfranco.gistapp.shared.test

import androidx.annotation.Nullable
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import java.util.concurrent.atomic.AtomicBoolean

/** Useful to allow testing to occur only when content is not loading (idle state) */
class SimpleIdlingResource : IdlingResource {
    @Nullable
    @Volatile
    private var mCallback: ResourceCallback? = null

    private val mIsIdleNow: AtomicBoolean = AtomicBoolean(true)

    override fun getName(): String = this.javaClass.name

    override fun isIdleNow(): Boolean = mIsIdleNow.get()

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        mCallback = callback
    }

    fun setIdleState(isIdleNow: Boolean) {
        mIsIdleNow.set(isIdleNow)
        if (isIdleNow && mCallback != null) {
            mCallback?.onTransitionToIdle()
        }
    }
}