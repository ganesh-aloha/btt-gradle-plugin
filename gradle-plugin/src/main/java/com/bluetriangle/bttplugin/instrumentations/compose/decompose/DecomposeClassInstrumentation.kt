package com.bluetriangle.bttplugin.instrumentations.compose.decompose

import com.android.build.api.instrumentation.ClassContext
import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.ByteCodeManipulationParameters
import org.objectweb.asm.ClassVisitor

class DecomposeClassInstrumentation : BttClassInstrumentation() {

    companion object {
        private const val CHILDREN_KT_CLASSNAME =
            "com.arkivanov.decompose.router.stack.ChildStackFactoryKt"
    }

    override fun isEnabled(parameters: ByteCodeManipulationParameters): Boolean =
        parameters.composeNavigationInjectionEnabled.get() && parameters.decomposeVersionSupported.get()

    override fun getVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor = DecomposeClassVisitor(nextClassVisitor)

    override val className: String
        get() = CHILDREN_KT_CLASSNAME
}