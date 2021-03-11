package com.aw.commons

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

object ScopeUtils {
    val ui = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + Dispatchers.Main
    }

    val disk = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + Dispatchers.IO
    }

    val db = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + Dispatchers.IO
    }
    val network = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + Dispatchers.IO
    }

    val cpu = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + Dispatchers.Default
    }
}