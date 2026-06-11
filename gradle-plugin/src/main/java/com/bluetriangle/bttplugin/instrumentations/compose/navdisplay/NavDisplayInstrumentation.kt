package com.bluetriangle.bttplugin.instrumentations.compose.navdisplay

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class NavDisplayInstrumentation :
    BttClassInstrumentation() {

    companion object {
        private const val NAV_HOST_CONTROLLER_CLASSNAME =
            "androidx.navigation3.ui.NavDisplayKt"
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
        get() = NAV_HOST_CONTROLLER_CLASSNAME

}