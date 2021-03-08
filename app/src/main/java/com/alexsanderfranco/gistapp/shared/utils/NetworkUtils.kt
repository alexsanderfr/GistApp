package com.alexsanderfranco.gistapp.shared.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET


class NetworkUtils {
    companion object {
        /** Registers callbacks for network states asynchronously. */
        fun registerNetworkCallback(
            activity: Activity,
            onNetworkAvailable: () -> Unit,
            onNetworkUnavailable: () -> Unit
        ) {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    activity.runOnUiThread {
                        onNetworkAvailable()
                    }
                }

                override fun onLost(network: Network) {
                    activity.runOnUiThread {
                        onNetworkUnavailable()
                    }
                }

                override fun onUnavailable() {
                    activity.runOnUiThread {
                        onNetworkUnavailable()
                    }
                }
            })
            if (!isConnected(activity)) {
                onNetworkUnavailable()
            }
        }

        /** Necessary for synchronously creating initial setup. */
        fun isConnected(activity: Activity): Boolean {
            val connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true
        }
    }
}