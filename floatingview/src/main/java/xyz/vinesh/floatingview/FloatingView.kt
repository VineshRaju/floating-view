package xyz.vinesh.floatingview

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

class FloatingView constructor(private val resIdOfView: Int,
                               private val width: Int,
                               private val height: Int) : LifecycleObserver {
    private lateinit var windowManager: WindowManager
    private lateinit var activity: AppCompatActivity
    private lateinit var view: View
    private var gravity: Int = Gravity.NO_GRAVITY
    private var xPos = 0
    private var yPos = 0

    var onClick: (() -> Unit)? = null

    fun attachOn(activity: AppCompatActivity, xPos: Int = 0, yPos: Int = 0, gravity: Int = Gravity.NO_GRAVITY): FloatingView {
        activity.lifecycle.addObserver(this)
        windowManager = activity.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        this.activity = activity
        this.xPos = xPos
        this.yPos = yPos
        this.gravity = gravity
        view = LayoutInflater.from(activity).inflate(resIdOfView, null, false)
        view.setOnClickListener {
            onClick?.invoke()
        }

        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun peek(): FloatingView {
        var shouldShow = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShow = Settings.canDrawOverlays(activity)
        }
        if (shouldShow) {
            val layoutParams = WindowManager.LayoutParams(
                    this@FloatingView.width,
                    this@FloatingView.height,
                    xPos,
                    yPos,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    else
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    PixelFormat.TRANSLUCENT
            )

            layoutParams.gravity = gravity
            windowManager.addView(view, layoutParams)
        }
        return this
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun hide() {
        if (view.isShown) {
            windowManager.removeViewImmediate(view)
        }
    }

}