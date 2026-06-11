package com.bluetriangle.bttplugin.instrumentations.compose.navcontroller

import com.bluetriangle.bttplugin.instrumentations.BttClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class NavControllerClassVisitor(
    nextClassVisitor: ClassVisitor, private val debugLog: Boolean = false
) : BttClassVisitor(nextClassVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String?>?
    ): MethodVisitor? {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (name == "rememberNavController" && mv != null) {
            return NavControllerMethodVisitor(
                Opcodes.ASM6,
                mv,
                access,
                name,
                descriptor ?: "",
                debugLog
            )
        }
        return mv
    }
}