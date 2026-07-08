package com.bluetriangle.bttplugin

import com.bluetriangle.bttplugin.instrumentations.BttClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.decompose.DecomposeClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.navcontroller.NavControllerClassInstrumentation
import com.bluetriangle.bttplugin.instrumentations.compose.navdisplay.NavDisplayInstrumentation
import com.bluetriangle.bttplugin.util.SdkMetaData
import com.bluetriangle.bttplugin.util.SdkMode
import com.bluetriangle.bttplugin.util.Version
import com.bluetriangle.bttplugin.util.isValidVersion
import com.bluetriangle.bttplugin.util.version
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.provider.Provider

object BttHelper {
    private const val BTT_GROUP = "blue-triangle-tech"
    private const val BTT_ARTIFACT = "btt-android-sdk"
    private const val BTT_SDK_SUPPORTED_VERSION = "2.19.6"
    private const val BTT_SDK_DECOMPOSE_SUPPORTED_VERSION = "2.19.6"
    private const val DECOMPOSE_GROUP = "com.arkivanov.decompose"
    private const val DECOMPOSE_ARTIFACT = "decompose"
    private const val DECOMPOSE_SUPPORTED_VERSION = "3.0.0"
    private const val VOYAGER_GROUP = "cafe.adriel.voyager"
    private const val VOYAGER_ARTIFACT = "voyager-navigator"

    fun resolveBttSdk(project: Project): Provider<SdkMetaData> {
        return project.provider {
            val sdkDependency = project.configurations
                .flatMap { it.dependencies }
                .filterIsInstance<ExternalModuleDependency>()
                .find { it.name == BTT_ARTIFACT }

            if (sdkDependency != null) {
                return@provider if (sdkDependency.group.contains(BTT_GROUP)) {
                    SdkMetaData(
                        SdkMode.PROD,
                        sdkDependency.version?.version() ?: Version.INFINITE
                    )
                } else {
                    SdkMetaData(
                        SdkMode.DEV,
                        Version.INFINITE
                    )
                }
            }

            Logger.log("Blue Triangle SDK dependency is missing from app gradle file")

            return@provider SdkMetaData(
                sdkMode = SdkMode.UNAVAILABLE,
                sdkVersion = Version.ZERO
            )
        }
    }

    fun resolveDecomposeLibrary(project: Project): Provider<SdkMetaData> {
        return project.provider {
            val sdkDependency = project.configurations
                .flatMap { it.dependencies }
                .filterIsInstance<ExternalModuleDependency>()
                .find {
                    it.group == DECOMPOSE_GROUP && it.name == DECOMPOSE_ARTIFACT
                }

            if (sdkDependency != null) {
                return@provider SdkMetaData(
                    SdkMode.PROD,
                    sdkDependency.version?.version() ?: Version.ZERO
                )
            }

            return@provider SdkMetaData(
                sdkMode = SdkMode.UNAVAILABLE,
                sdkVersion = Version.ZERO
            )
        }
    }

    fun resolveVoyagerLibrary(project: Project): Provider<SdkMetaData> {
        return project.provider {
            val sdkDependency = project.configurations
                .flatMap { it.dependencies }
                .filterIsInstance<ExternalModuleDependency>()
                .find {
                    it.group == VOYAGER_GROUP && it.name == VOYAGER_ARTIFACT
                }

            if (sdkDependency != null) {
                return@provider SdkMetaData(
                    SdkMode.PROD,
                    sdkDependency.version?.version() ?: Version.ZERO
                )
            }

            return@provider SdkMetaData(
                sdkMode = SdkMode.UNAVAILABLE,
                sdkVersion = Version.ZERO
            )
        }
    }

    fun isBttSdkVersionSupported(data: SdkMetaData): Boolean {
        val result = data.sdkVersion >= BTT_SDK_SUPPORTED_VERSION
        if (data.sdkVersion != Version.ZERO && !result)
            Logger.log("Blue Triangle SDK Version ${data.sdkVersion} is not supporting screen auto tracking, please upgrade to $BTT_SDK_SUPPORTED_VERSION or higher version")
        return result
    }

    fun isDecomposeVersionSupported(data: SdkMetaData): Boolean {
        val result = data.sdkVersion >= DECOMPOSE_SUPPORTED_VERSION
        if (data.sdkVersion != Version.ZERO && !result)
            Logger.log("Decompose Version ${data.sdkVersion} not supported upgrade to $DECOMPOSE_SUPPORTED_VERSION")
        return result
    }

    fun getSupportedInstrumentations(sdkVersion: Version): List<BttClassInstrumentation> {
        if (!sdkVersion.isValidVersion) return emptyList()

        val instrumentations = mutableListOf<BttClassInstrumentation>()
        if (BTT_SDK_SUPPORTED_VERSION.isValidVersion && sdkVersion >= BTT_SDK_SUPPORTED_VERSION) {
            instrumentations.add(NavControllerClassInstrumentation())
            instrumentations.add(NavDisplayInstrumentation())
        }

        if (BTT_SDK_DECOMPOSE_SUPPORTED_VERSION.isValidVersion && sdkVersion >= BTT_SDK_DECOMPOSE_SUPPORTED_VERSION)
            instrumentations.add(DecomposeClassInstrumentation())

        return instrumentations
    }
}