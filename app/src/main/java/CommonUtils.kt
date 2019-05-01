import android.os.Handler
import android.os.HandlerThread

object CommonUtils {
    private val handler: Handler by lazy {
        val handlerThread = HandlerThread("DefaultNormalThread", Thread.NORM_PRIORITY)
        handlerThread.start()
        Handler(handlerThread.looper)
    }

    fun runOnDefaultThread(runnable: () -> Unit, delayMillis: Long = 0) {
        handler.postDelayed(runnable, delayMillis)
    }
}