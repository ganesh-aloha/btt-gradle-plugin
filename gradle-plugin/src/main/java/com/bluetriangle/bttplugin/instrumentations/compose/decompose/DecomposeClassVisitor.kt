package com.bluetriangle.bttplugin.instrumentations.compose.decompose

import com.bluetriangle.bttplugin.instrumentations.BttClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class DecomposeClassVisitor(
    cv: ClassVisitor
) : BttClassVisitor(cv) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name == "childStack") {
            return DecomposeMethodVisitor(Opcodes.ASM9, mv, access, name, descriptor)
        }

        return mv
    }
}
