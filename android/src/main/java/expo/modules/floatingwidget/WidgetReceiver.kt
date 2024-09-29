package expo.modules.floatingwidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.RequiresApi

class WidgetReceiver private constructor() : BroadcastReceiver() {

    companion object {
        private var instance: WidgetReceiver? = null
        private var isRegistered = false

        private fun getInstance(): WidgetReceiver {
            if (instance == null) {
                instance = WidgetReceiver()
                Log.d(Constants.TAG, "GetWidgetReceiver instance created")
            }
            return instance!!
        }

        @RequiresApi(33)
        @Synchronized
        fun registerReceiver(context: Context, intentFilter: IntentFilter) {
            try {
                if (!isRegistered) {
                    isRegistered = true
                    context.registerReceiver(
                        getInstance(), intentFilter, Context.RECEIVER_EXPORTED
                    )
                    Log.d(Constants.TAG, "Receiver registered")
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Error registering receiver: ${e.message}")
            }
        }

        @Synchronized
        fun unregisterReceiver(context: Context) {
            try {
                if (isRegistered) {
                    context.unregisterReceiver(getInstance())
                    isRegistered = false
                    Log.d(Constants.TAG, "Receiver removed")
                }
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Error unregistering receiver: ${e.message}")
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Constants.TAG, "onReceive called")
        val packageName = context.packageName
        val packageManager = context.packageManager
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    }
}