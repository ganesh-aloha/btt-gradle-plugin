package com.bluetriangle.bttplugin.instrumentations

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import com.bluetriangle.bttplugin.BttHelper
import com.bluetriangle.bttplugin.instrumentations.compose.decompose.DecomposeClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.navcontroller.NavControllerClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.navdisplay.NavDisplayInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.voyager.VoyagerClassInstrumentation
import com.bluetriangle.bttplugin.util.Version
import com.bluetriangle.bttplugin.util.version
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor

interface ByteCodeManipulationParameters: InstrumentationParameters {
    @get:Input val debugLog: Property<Boolean>
    @get:Input val composeNavigationInjectionEnabled: Property<Boolean>
    @get:Input val sdkVersion: Property<String>
    @get:Input val decomposeVersionSupported: Property<Boolean>
}

abstract class BttClassVisitorFactory: AsmClassVisitorFactory<ByteCodeManipulationParameters> {

    private fun getInstrumentations() = BttHelper.getSupportedInstrumentations(parameters.get().sdkVersion.get().version())

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val instrumentation = getInstrumentations().find { classContext.currentClassData.className == it.className }

        if(instrumentation == null || !instrumentation.isEnabled(parameters.get())) {
            return nextClassVisitor
        }

        // Check if the BTT SDK hook class exists in the classpath
//        val hookClassExists = try {
//            classContext.loadClassData("com.bluetriangle.analytics.compose.ComposeKt") != null
//        } catch (e: Exception) {
//            false
//        }
//
//        if (!hookClassExists) {
//            return nextClassVisitor
//        }

        return instrumentation.getVisitor(classContext, nextClassVisitor, parameters.get().debugLog.get())
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return getInstrumentations().any {
            classData.className == it.className
        }
    }

}