package com.bluetriangle.bttplugin.instrumentations.compose.navdisplay

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class NavDisplayInstrumentation :
    BttClassInstrumentation() {

    companion object {
        const val INSTRUMENTATION_CLASS_NAME = "androidx.navigation3.ui.NavDisplayKt"
        const val INSTRUMENTATION_METHOD_NAME = "NavDisplay"
    }

    override fun isEnabled(parameters: ByteCodeManipulationParameters): Boolean {
        return parameters.composeNavigationInjectionEnabled.get()
    }

    override fun getVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor,
        debugLog: Boolean
    ): ClassVisitor {
        return NavDisplayClassVisitor(nextClassVisitor, debugLog)
    }

    override val className: String
        get() = INSTRUMENTATION_CLASS_NAME
}