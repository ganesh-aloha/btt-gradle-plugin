package com.bluetriangle.bttplugin.instrumentations.compose.navcontroller

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class NavControllerClassInstrumentation :
    BttClassInstrumentation() {

    companion object {
        private const val NAV_HOST_CONTROLLER_CLASSNAME =
            "androidx.navigation.compose.NavHostControllerKt"
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
        get() = NAV_HOST_CONTROLLER_CLASSNAME

}