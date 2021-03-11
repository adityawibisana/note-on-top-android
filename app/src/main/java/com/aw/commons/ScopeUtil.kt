package com.aw.commons

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object ScopeUtil {
    val ui = Job() + Dispatchers.Main
    val disk = Job() + Dispatchers.IO
    val network = Job() + Dispatchers.IO
    val cpu = Job() + Dispatchers.Default

}