import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

object CommonUtils {

    private val handler: Handler by lazy {
        val handlerThread = HandlerThread("DefaultNormalThread", Thread.NORM_PRIORITY)
        handlerThread.start()
        Handler(handlerThread.looper)
    }

    private val job: Job by lazy { Job() }

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

    fun runOnUnusedThread (runnable: () -> Unit) {
        handlers[12].post {
            do {
                val filtered = handlers.indexOfFirst {
                    !it.hasMessages(WHAT_IS_RUNNING) && it != handlers[12]
                }

                if (filtered != -1) {
                    Timber.v("Using index:$filtered for the thread")
                    handlers[filtered].post {
                        handlers[filtered].sendEmptyMessage(WHAT_IS_RUNNING)
                        runnable()
                        handlers[filtered].removeMessages(WHAT_IS_RUNNING)
                    }
                    break
                }

                Timber.v("All thread is busy. Retrying...")

            } while (filtered == -1)
        }
    }

    val defaultScope: CoroutineScope by lazy {
        object: CoroutineScope {
            override val coroutineContext: CoroutineContext
                get() = job + Dispatchers.Main
        }
    }

    fun getDimen(context: Context, resource: Int) : Float {
        return context.resources.getDimension(resource)
    }

}