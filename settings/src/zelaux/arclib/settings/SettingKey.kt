package zelaux.arclib.settings

import arc.Core
import arc.func.Cons2
import arc.struct.Seq

open class SettingKey<T>(
    @JvmField val key: String,
    val defaultProvider: () -> T,
    val valueGetter: (SettingKey<T>) -> T,
    val valueSetter: (T, SettingKey<T>) -> Unit,
) {
    companion object {
        @JvmField
        val allKeys = Seq<SettingKey<*>>()
    }

    init {
        allKeys.add(this)
    }

    val updateListeners: Seq<Cons2<T, SettingKey<T>>> = Seq();

    open fun def() = defaultProvider()
    fun get(): T = valueGetter(this)
    fun set(value: T) {
        valueSetter(value, this)
        for (listener in updateListeners) {
            listener[value, this]
        }
    }

    open fun setDefault() {
        Core.settings.defaults(key, def())
    }
}