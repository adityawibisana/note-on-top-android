import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.view_main_editable.view.*

object CommonUtils {
    private const val TAG = "CommonUtils"

    private val handler: Handler by lazy {
        val handlerThread = HandlerThread("DefaultNormalThread", Thread.NORM_PRIORITY)
        handlerThread.start()
        Handler(handlerThread.looper)
    }

    private const val WHAT_IS_RUNNING : Int = 1


    private val handlers: ArrayList<Handler> by lazy {
        val handlerThreads = arrayListOf<HandlerThread>()
        val handlers = arrayListOf<Handler>()
        for (i in 0..12) {
            handlerThreads.add(HandlerThread("", Thread.MIN_PRIORITY))
            handlerThreads[i].start()

            handlers.add(Handler(handlerThreads[i].looper))
        }
        handlers
    }

    fun runOnDefaultThread(runnable: () -> Unit, delayMillis: Long = 0) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun runOnUIThread(runnable: () -> Unit, delayMillis: Long = 0) {
        Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis)
    }


    fun forceShowKeyboard(context: Context, view: View) {
        CommonUtils.runOnUIThread({
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0.0f, 0.0f, 0))
            view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0.0f, 0.0f, 0))
        }, 70)
    }

    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun runOnUnusedThread (runnable: () -> Unit) {
        handlers[12].post {
            do {
                val filtered = handlers.indexOfFirst {
                    !it.hasMessages(WHAT_IS_RUNNING) && it != handlers[12]
                }

                if (filtered != -1) {
                    Log.v(TAG, "Using index:$filtered for the thread")
                    handlers[filtered].post {
                        handlers[filtered].sendEmptyMessage(WHAT_IS_RUNNING)
                        runnable()
                        handlers[filtered].removeMessages(WHAT_IS_RUNNING)
                    }
                    break
                }

                Log.v(TAG, "All thread is busy. Retrying...")

            } while (filtered == -1)
        }
    }

}