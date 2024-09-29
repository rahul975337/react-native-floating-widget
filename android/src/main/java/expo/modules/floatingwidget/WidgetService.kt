package expo.modules.floatingwidget

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.OrientationEventListener
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.util.Calendar

object Constants {
    const val BROADCAST_ACTION = "expo.modules.reactnative.floatingwidget.BROADCAST_ACTION"
    const val MAX_CLICK_DURATION = 200
}

class WidgetService : Service() {

    private var windowManager: WindowManager? = null
    private var widgetView: View? = null
    private var orientationEventListener: OrientationEventListener? = null
    private var startClickTime: Long = 0
    private val TAG = "CUSTOM_LOG"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        registerReceiver()
        initializeWidgetView()
        setupOrientationListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupWidgetView()
        unregisterReceiver()
        disableOrientationListener()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun registerReceiver() {
        WidgetReceiver.registerReceiver(this, IntentFilter(Constants.BROADCAST_ACTION))
    }

    private fun unregisterReceiver() {
        WidgetReceiver.unregisterReceiver(this)
    }

    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeWidgetView() {
        widgetView = LayoutInflater.from(this).inflate(R.layout.layout_widget, null)
        val params = createLayoutParams()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(widgetView, params)
        setupTouchListener(params)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 20
            y = 700
        }
    }

    private fun setupTouchListener(params: WindowManager.LayoutParams) {
        widgetView?.findViewById<View>(R.id.root_container)
            ?.setOnTouchListener(object : View.OnTouchListener {
                private var initialY = 0
                private var initialTouchY = 0f

                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialY = params.y
                            initialTouchY = event.rawY
                            startClickTime = Calendar.getInstance().timeInMillis
                            return false
                        }

                        MotionEvent.ACTION_UP -> {
                            val clickDuration = Calendar.getInstance().timeInMillis - startClickTime
                            if (clickDuration < Constants.MAX_CLICK_DURATION) {
                                sendBroadcast(Intent(Constants.BROADCAST_ACTION))
                                stopSelf()
                            }
                            return false
                        }

                        MotionEvent.ACTION_MOVE -> {
                            params.y = initialY + (event.rawY - initialTouchY).toInt()
                            windowManager?.updateViewLayout(widgetView, params)
                            return false
                        }
                    }
                    return false
                }
            })
    }

    private fun setupOrientationListener() {
        orientationEventListener =
            object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                override fun onOrientationChanged(orientation: Int) {
                    val isLandscape =
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                    widgetView?.visibility = if (isLandscape) View.GONE else View.VISIBLE
                }
            }
        if (orientationEventListener?.canDetectOrientation() == true) {
            orientationEventListener?.enable()
        }
    }

    private fun disableOrientationListener() {
        orientationEventListener?.disable()
    }

    private fun cleanupWidgetView() {
        if (widgetView != null) {
            windowManager?.removeView(widgetView)
            widgetView?.findViewById<View>(R.id.root_container)?.setOnTouchListener(null)
            widgetView = null
        }
    }
}