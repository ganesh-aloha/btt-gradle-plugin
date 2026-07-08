package com.bluetriangle.bttplugin

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class Logger {
    companion object {
        fun log(message: String) {
            println("BttPlugin: $message")
        }
    }
}

val Long.toFormattedTime: String
    get() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        return formatter.format(this)
    }

object InstrumentationSummary {
    private val totalTimeNs = AtomicLong(0)
    private val classCount = AtomicInteger(0)

    fun record(durationNs: Long) {
        totalTimeNs.addAndGet(durationNs)
        classCount.incrementAndGet()
    }

    fun logSummary() {
        if (classCount.get() <= 1)
            Logger.log("Instrumented ${classCount.get()} class in ${totalTimeNs.get() / 1_000_000} ms")
        else
            Logger.log("Instrumented ${classCount.get()} classes in ${totalTimeNs.get() / 1_000_000} ms")
    }
}