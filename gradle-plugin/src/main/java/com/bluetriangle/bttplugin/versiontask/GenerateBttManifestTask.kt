package com.bluetriangle.bttplugin.versiontask

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GenerateBttManifestTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val version: Property<String>

    @TaskAction
    fun generate() {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()

        file.writeText(
            """
            <manifest xmlns:android="http://schemas.android.com/apk/res/android">
                <application>
                    <meta-data
                        android:name="com.blue-triangle.plugin.version"
                        android:value="${version.get()}" />
                </application>
            </manifest>
            """.trimIndent()
        )
    }
}