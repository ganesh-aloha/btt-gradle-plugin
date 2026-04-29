// SdkVersionResolver.kt
package com.bluetriangle.bttplugin.util

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.provider.Provider

object SdkResolver {

    fun resolve(project: Project): Provider<SdkMetaData> {
        return project.provider {
            val sdkDependency = project.configurations
                .flatMap { it.dependencies }
                .filterIsInstance<ExternalModuleDependency>()
                .find {
                    it.name == "btt-android-sdk"
                }

            if (sdkDependency != null) {
                return@provider if (sdkDependency.group?.contains("blue-triangle-tech") == true) {
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
            return@provider SdkMetaData(
                sdkMode = SdkMode.UNAVAILABLE,
                sdkVersion = Version.ZERO
            )
        }
    }

}