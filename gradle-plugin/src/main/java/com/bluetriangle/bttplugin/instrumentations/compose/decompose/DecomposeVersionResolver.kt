package com.bluetriangle.bttplugin.instrumentations.compose.decompose

import com.bluetriangle.bttplugin.util.SdkMetaData
import com.bluetriangle.bttplugin.util.SdkMode
import com.bluetriangle.bttplugin.util.Version
import com.bluetriangle.bttplugin.util.version
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.provider.Provider

object DecomposeVersionResolver {
    private const val DECOMPOSE_GROUP = "com.arkivanov.decompose"
    private const val DECOMPOSE_ARTIFACT = "decompose"
    private const val SUPPORTED_VERSION = "3.0.0"

    fun resolve(project: Project): Provider<SdkMetaData> {
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

    fun isVersionSupported(data: SdkMetaData): Boolean {
        return data.sdkVersion >= SUPPORTED_VERSION
    }
}