package com.bluetriangle.bttplugin.util

enum class SdkMode {
    DEV,
    PROD,
    UNAVAILABLE
}

data class Version(
    val major: Int,
    val minor: Int = 0,
    val patch: Int = 0
): Comparable<Version> {

    companion object {
        val ZERO = Version(0, 0, 0)
        val INFINITE = Version(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    operator fun compareTo(other: String): Int {
        return this.compareTo(other.version())
    }

    override operator fun compareTo(other: Version): Int {
        if(major != other.major) {
            return major.compareTo(other.major)
        }
        if(minor != other.minor) {
            return minor.compareTo(other.minor)
        }
        if(patch != other.patch) {
            return patch.compareTo(other.patch)
        }
        return 0
    }
}

fun String.version(): Version {
    val parts = split(".").map { it.toInt() }
    return Version(
        parts.getOrElse(0) { 0 },
        parts.getOrElse(1) { 0 },
        parts.getOrElse(2) { 0 }
    )
}

data class SdkMetaData(
    val sdkMode: SdkMode,
    val sdkVersion: Version
)