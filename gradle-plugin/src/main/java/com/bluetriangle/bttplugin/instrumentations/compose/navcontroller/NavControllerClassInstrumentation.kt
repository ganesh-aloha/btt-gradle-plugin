package com.bluetriangle.bttplugin.instrumentations.compose.navcontroller

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class NavControllerClassInstrumentation :
    BttClassInstrumentation() {

    companion object {
        const val INSTRUMENTATION_CLASS_NAME = "androidx.navigation.compose.NavHostControllerKt"
        const val INSTRUMENTATION_METHOD_NAME = "rememberNavController"
    }

    override fun isEnabled(parameters: ByteCodeManipulationParameters): Boolean {
        return parameters.composeNavigationInjectionEnabled.get()
    }

    override fun getVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor,
        debugLog: Boolean
    ): ClassVisitor {
        return NavControllerClassVisitor(nextClassVisitor, debugLog)
    }

    override val className: String
        get() = INSTRUMENTATION_CLASS_NAME
}