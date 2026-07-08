package com.bluetriangle.bttplugin.instrumentations.compose.navdisplay

import com.bluetriangle.bttplugin.instrumentations.BttClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class NavDisplayClassVisitor(
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

        if (name == "NavDisplay" &&
            signature == "<T:Ljava/lang/Object;>(Landroidx/navigation3/scene/SceneState<TT;>;Landroidx/navigationevent/compose/NavigationEventState<Landroidx/navigation3/scene/SceneInfo<TT;>;>;Landroidx/compose/ui/Modifier;Landroidx/compose/ui/Alignment;Landroidx/compose/animation/SizeTransform;Lkotlin/jvm/functions/Function1<-Landroidx/compose/animation/AnimatedContentTransitionScope<Landroidx/navigation3/scene/Scene<TT;>;>;Landroidx/compose/animation/ContentTransform;>;Lkotlin/jvm/functions/Function1<-Landroidx/compose/animation/AnimatedContentTransitionScope<Landroidx/navigation3/scene/Scene<TT;>;>;Landroidx/compose/animation/ContentTransform;>;Lkotlin/jvm/functions/Function2<-Landroidx/compose/animation/AnimatedContentTransitionScope<Landroidx/navigation3/scene/Scene<TT;>;>;-Ljava/lang/Integer;Landroidx/compose/animation/ContentTransform;>;Landroidx/compose/runtime/Composer;II)V" &&
            mv != null
        ) {
            return NavDisplayMethodVisitor(
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