package com.alexsanderfranco.gistapp.shared.utils

import android.view.View
import com.alexsanderfranco.gistapp.R
import com.google.android.material.snackbar.Snackbar


class UiUtils {
    companion object {
        fun getFavoriteImageId(isFavorite: Boolean) = when (isFavorite) {
            true -> R.drawable.ic_favorite
            false -> R.drawable.ic_favorite_border
        }

        fun showSnackBar(view: View, text: String) {
            val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        fun showPermanentSnackBar(
            view: View,
            text: String,
            actionText: CharSequence?,
            action: View.OnClickListener?
        ) {
            val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
            if (actionText != null && action != null) {
                snackBar.setAction(actionText, action)
            }
            snackBar.show()
        }
    }
}