package com.bluetriangle.bttplugin.instrumentations.compose.navcontroller

import com.bluetriangle.bttplugin.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.Method

class NavControllerMethodVisitor(
    api: Int,
    methodVisitor: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String,
    private val debugLog: Boolean = false
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {

    override fun onMethodExit(opcode: Int) {
         val replacementOwner = "Lcom/bluetriangle/analytics/compose/ComposeKt;"
        val replacementName = "withBttNavigationTracker"
        val replacementDescriptor = "(Landroidx/navigation/NavHostController;Landroidx/compose/runtime/Composer;I)Landroidx/navigation/NavHostController;"

        if (debugLog) Logger.log("Instrumented ${NavControllerClassInstrumentation.INSTRUMENTATION_METHOD_NAME} method of ${NavControllerClassInstrumentation.INSTRUMENTATION_CLASS_NAME} class")

        loadArg(1)
        loadArg(2)
        invokeStatic(
            Type.getType(replacementOwner),
            Method(
                replacementName,
                replacementDescriptor
            )
        )
        super.onMethodExit(opcode)
    }
}
