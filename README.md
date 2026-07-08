# Blue Triangle Tech Android Gradle Plug-in

[![jitpack.io build status](https://jitpack.io/v/blue-triangle-tech/btt-gradle-plugin.svg)](https://jitpack.io/#blue-triangle-tech/btt-gradle-plugin)

The Blue Triangle Gradle Plugin for Android to enables application screen autotracking for Compose.

## Supported Navigation Libraries
- NavController with minimum Compose navigation version 2.8.7
- Nav3
- Decompose

**Minimum Requirements**
- Blue Triangle Gradle Plugin required Android Gradle Plugin version 8.6.0 or higher

## How to Integrate

### Pluig-in 

Add the Maven repository and resolutionStrategy to the project's `settings.gradle` file:

```groovy

pluginManagement {
    repositories {
        //...
        maven { url = uri("https://jitpack.io") }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.github.blue-triangle-tech.btt-gradle-plugin") {
                useModule(
                    "com.github.blue-triangle-tech:btt-gradle-plugin:${requested.version}"
                )
            }
        }
    }
}

```

Add the plugin to your application's `build.gradle` file:

```groovy

plugins {
    //...
    id("com.github.blue-triangle-tech.btt-gradle-plugin") version "1.0.0"
}

```

To enable/disable screen tracking for modern frameworks add following configuration to your application's `build.gradle` file:

```groovy

bttOptions {
    composeNavigationInjectionEnabled = true
}

```
