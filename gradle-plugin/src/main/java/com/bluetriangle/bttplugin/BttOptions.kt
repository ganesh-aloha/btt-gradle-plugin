package com.bluetriangle.bttplugin

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

open class BttOptions @Inject constructor(objects: ObjectFactory) {
    val debugLog: Property<Boolean> =
        objects.property(Boolean::class.java)
            .convention(false)

    val composeNavigationInjectionEnabled: Property<Boolean> =
        objects.property(Boolean::class.java)
            .convention(true)
}