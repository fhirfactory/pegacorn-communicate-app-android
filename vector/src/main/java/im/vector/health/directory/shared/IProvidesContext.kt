package im.vector.health.directory.shared

import android.content.Context

interface IProvidesContext {
    fun getCurrentContext(): Context
}