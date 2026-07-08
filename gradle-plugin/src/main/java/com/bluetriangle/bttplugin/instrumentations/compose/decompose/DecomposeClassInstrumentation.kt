package com.bluetriangle.bttplugin.instrumentations.compose.decompose

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class DecomposeClassInstrumentation : BttClassInstrumentation() {

    companion object {
        const val INSTRUMENTATION_CLASS_NAME = "com.arkivanov.decompose.router.stack.ChildStackFactoryKt"
        const val INSTRUMENTATION_METHOD_NAME = "childStack"
    }

    override fun isEnabled(parameters: ByteCodeManipulationParameters): Boolean =
        parameters.composeNavigationInjectionEnabled.get() && parameters.decomposeVersionSupported.get()

    override fun getVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor,
        debugLog: Boolean
    ): ClassVisitor = DecomposeClassVisitor(nextClassVisitor, debugLog)

    override val className: String
        get() = INSTRUMENTATION_CLASS_NAME
}