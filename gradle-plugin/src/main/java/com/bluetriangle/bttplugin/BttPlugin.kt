package com.bluetriangle.bttplugin

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.bluetriangle.bttplugin.instrumentations.BttClassVisitorFactory
import com.bluetriangle.bttplugin.instrumentations.compose.decompose.DecomposeVersionResolver
import com.bluetriangle.bttplugin.util.SdkResolver
import com.bluetriangle.bttplugin.util.Version
import com.bluetriangle.bttplugin.util.version
import com.bluetriangle.bttplugin.versiontask.GenerateBttManifestTask
import com.github.blue_triangle_tech.gradle_plugin.BuildConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import kotlin.jvm.java

class BttPlugin : Plugin<Project> {

    companion object {
        const val BTT_EXTENSION = "btt"
        const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    }

    override fun apply(project: Project) {

        val extension = project.extensions
            .create(BTT_EXTENSION, BttPluginExtension::class.java)

        project.pluginManager.withPlugin(ANDROID_APPLICATION_PLUGIN) {
            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)

            androidComponents.onVariants { variant ->

                injectPluginVersionIntoManifest(project, variant)

                registerInstrumentation(project, variant, extension)
            }

            project.tasks.matching { it.name.startsWith("assemble") }
                .configureEach {
                    doLast { println("BttPlugin started: $name") }
                }
        }
    }

    fun registerInstrumentation(project: Project, variant: Variant, extension: BttPluginExtension) {
        val sdkVersion = SdkResolver.resolve(project)
        val decomposeVersion = DecomposeVersionResolver.resolve(project)
        println("BttPlugin: sdkVersion: ${sdkVersion.get()}")
        variant.instrumentation.transformClassesWith(
            BttClassVisitorFactory::class.java,
            InstrumentationScope.ALL
        ) { parameters ->
            parameters.composeNavigationInjectionEnabled.set(
                extension.composeNavigationInjectionEnabled.map {
                    sdkVersion.get().sdkVersion >= "2.19.5" && it
                }
            )

            parameters.decomposeVersionSupported.set(
                decomposeVersion.map { DecomposeVersionResolver.isVersionSupported(it) }
            )
        }
    }

    fun injectPluginVersionIntoManifest(project: Project, variant: Variant) {
        val pluginVersion = BuildConfig.VERSION
        val taskProvider = project.tasks.register(
            "generateBttManifest${variant.name.replaceFirstChar { it.uppercase() }}",
            GenerateBttManifestTask::class.java
        ) {
            outputFile.set(
                project.layout.buildDirectory.file(
                    "generated/bttManifest/${variant.name}/AndroidManifest.xml"
                )
            )
            version.set(pluginVersion)
        }

        variant.sources.manifests.addGeneratedManifestFile(
            taskProvider,
            GenerateBttManifestTask::outputFile
        )
    }
}