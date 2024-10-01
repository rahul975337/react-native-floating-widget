package expo.modules.floatingwidget

import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

object Constants {
    const val BROADCAST_ACTION = "expo.modules.reactnative.floatingwidget.BROADCAST_ACTION"
    const val MAX_CLICK_DURATION = 200
    const val TAG = "CUSTOM_LOG"
}

class ReactNativeFloatingWidgetModule : Module() {


    private val reactContext get() = requireNotNull(appContext.reactContext)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun definition() = ModuleDefinition {
        Name("ReactNativeFloatingWidget")

        AsyncFunction("checkPermissionAsync") {
            return@AsyncFunction checkPermissionAsync()
        }

        AsyncFunction("requestPermissionAsync") {
            return@AsyncFunction requestPermissionAsync()
        }

        AsyncFunction("start") {
            return@AsyncFunction start()
        }

        AsyncFunction("stop") {
            return@AsyncFunction stop()
        }
    }

    private fun checkPermissionAsync(): Boolean {
        return (Settings.canDrawOverlays(reactContext))
    }

    private fun requestPermissionAsync(): Boolean {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + reactContext.packageName)
        ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        reactContext.startActivity(intent)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun start(): Boolean {
        if (!checkPermissionAsync()) return false
        WidgetReceiver.registerReceiver(reactContext, IntentFilter(Constants.BROADCAST_ACTION))
        try {
            Log.d(Constants.TAG, "Starting the service")
            val startIntent = Intent(reactContext, WidgetService::class.java)
            reactContext.startService(startIntent)
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error starting service: ${e.message}")
            return false
        }
        return true
    }

    private fun stop(): Boolean {
        if (!checkPermissionAsync()) return false
        try {
            Log.d(Constants.TAG, "Stopping the service")
            val stopIntent = Intent(reactContext, WidgetService::class.java)
            reactContext.stopService(stopIntent)
        } catch (e: Exception) {
            Log.e(Constants.TAG, "Error starting service: ${e.message}")
            return false
        }

        WidgetReceiver.unregisterReceiver(reactContext)
        return true
    }
}