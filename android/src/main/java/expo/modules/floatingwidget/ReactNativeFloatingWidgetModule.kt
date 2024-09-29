package expo.modules.floatingwidget

import android.util.Log
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ReactNativeFloatingWidgetModule : Module() {

  private val TAG = "CUSTOM_LOG"

  override fun definition() = ModuleDefinition {
    Name("ReactNativeFloatingWidget")

    AsyncFunction("checkPermissionAsync"){
      return@AsyncFunction true
    }

    AsyncFunction("requestPermissionAsync"){
      return@AsyncFunction requestPermissionAsync()
    }

    AsyncFunction("start"){
      return@AsyncFunction start()
    }

    AsyncFunction("stop"){
      return@AsyncFunction stop()
    }
  }

  private fun checkPermissionAsync(): Boolean {
    Log.d(TAG, "checkPermissionAsync");
    return true
  }

  private fun requestPermissionAsync(): Boolean {
    Log.d(TAG, "requestPermissionAsync");
    return true
  }

  private fun start(): Boolean {
    Log.d(TAG, "start");
    return true
  }

  private fun stop(): Boolean {
    Log.d(TAG, "stop");
    return true
  }
}