package com.bluetriangle.bttplugin.instrumentations.compose.navdisplay

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.Method
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode

class NavDisplayMethodVisitor(
    api: Int,
    val methodVisitor: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String,
    private val debugLog: Boolean = false
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)

        val replacementOwner = "Lcom/bluetriangle/analytics/compose/ComposeKt;"
        val replacementName = "bttTrackBackStack"
        val replacementDescriptor = "(Landroidx/navigation3/scene/SceneState;Landroidx/compose/runtime/Composer;I)Landroidx/navigation3/scene/SceneState;"

        loadArg(0) // SceneState
        loadArg(8) // Composer

        push(14)
        loadArg(10)
        visitInsn(IAND)

        invokeStatic(
            Type.getType(replacementOwner),
            Method(
                replacementName,
                replacementDescriptor
            )
        )
        pop()
    }
}
