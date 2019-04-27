import android.os.Handler
import android.os.HandlerThread

object CommonUtils {
    lateinit var handler: Handler

    fun runOnDefaultThread(runnable: () -> Unit) {
        if (!::handler.isInitialized) {
            val handlerThread = HandlerThread("DefaultNormalThread", Thread.NORM_PRIORITY)
            handlerThread.start()

            handler = Handler(handlerThread.looper)
        }

        handler.post(runnable)
    }
}