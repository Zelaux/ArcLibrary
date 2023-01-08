package zelaux.arclib.settings

import arc.*
import arc.struct.*

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


    open fun def() = defaultProvider()
    fun get(): T = valueGetter(this)
    fun set(value: T) = valueSetter(value, this)

    open fun setDefault() {
        Core.settings.defaults(key, def())
    }
}