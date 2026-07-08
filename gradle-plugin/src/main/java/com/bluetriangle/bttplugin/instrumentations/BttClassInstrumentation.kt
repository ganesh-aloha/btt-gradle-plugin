package com.bluetriangle.bttplugin.instrumentations

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.ClassVisitor

abstract class BttClassInstrumentation {

    abstract fun isEnabled(parameters: ByteCodeManipulationParameters): Boolean

    abstract fun getVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor,
        debugLog: Boolean = false
    ): ClassVisitor

    abstract val className: String

}