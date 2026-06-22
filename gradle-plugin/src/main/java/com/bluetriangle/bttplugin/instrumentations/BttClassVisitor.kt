package com.bluetriangle.bttplugin.instrumentations

import com.bluetriangle.bttplugin.InstrumentationSummary
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

open class BttClassVisitor(nextClassVisitor: ClassVisitor): ClassVisitor(Opcodes.ASM9, nextClassVisitor){
    val start = System.nanoTime()

    override fun visitEnd() {
        super.visitEnd()

        InstrumentationSummary.record(
            System.nanoTime() - start
        )
    }
}