package com.frstudio.bilibilivideomanagerpro.compent

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private val job = Job()
private val scope = CoroutineScope(job)
fun IO(block: suspend CoroutineScope.() -> Unit): Job = scope.launch(Dispatchers.IO, block = block)
fun Main(block: suspend CoroutineScope.() -> Unit): Job = scope.launch(Dispatchers.Main, block = block)