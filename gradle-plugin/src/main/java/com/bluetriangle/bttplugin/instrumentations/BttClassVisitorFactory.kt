package com.bluetriangle.bttplugin.instrumentations

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.bluetriangle.bttplugin.instrumentations.compose.navcontroller.NavControllerClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.navdisplay.NavDisplayInstrumentation
import com.bluetriangle.bttplugin.util.Version
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor

interface ByteCodeManipulationParameters: InstrumentationParameters {
    @get:Input val composeNavigationInjectionEnabled: Property<Boolean>
}

abstract class BttClassVisitorFactory: AsmClassVisitorFactory<ByteCodeManipulationParameters> {

    private fun getInstrumentations() = listOf(
        NavControllerClassInstrumentation(),
        NavDisplayInstrumentation()
    )

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val instrumentation = getInstrumentations().find { classContext.currentClassData.className == it.className }

        if(instrumentation == null || !instrumentation.isEnabled(parameters.get())) {
            return nextClassVisitor
        }

        return instrumentation.getVisitor(classContext, nextClassVisitor)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return getInstrumentations().any {
            classData.className == it.className
        }
    }

}