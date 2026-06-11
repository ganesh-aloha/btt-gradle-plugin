package com.bluetriangle.bttplugin.instrumentations.compose.decompose

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

class DecomposeMethodVisitor(
    api: Int,
    mv: MethodVisitor,
    access: Int,
    name: String,
    descriptor: String,
    private val debugLog: Boolean = false
) : AdviceAdapter(api, mv, access, name, descriptor) {

    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)

        val isChildStackCall = owner.contains("ChildStackFactoryKt") && name == "childStack"
        if (isChildStackCall) {
            println("BTT visiting: $owner.$name$descriptor")
            dup()
            visitMethodInsn(
                INVOKESTATIC,
                "com/bluetriangle/analytics/compose/DecomposeHook",
                "bttTrackStack",
                "(Lcom/arkivanov/decompose/value/Value;)V",
                false
            )
        }
    }
}

