package zelaux.arclib.settings

import arc.*


open class IntSettingKey(key: String, defaultProvider: () -> Int) : SettingKey<Int>(key, defaultProvider, {
    val value: Int = Core.settings[it.key, it.def()].toString().toIntOrNull() ?: run {
        Core.settings.put(it.key, it.def())
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value)
})
open class LongSettingKey(key: String, defaultProvider: () -> Long) : SettingKey<Long>(key, defaultProvider, {
    val value: Long = Core.settings[it.key, it.def()].toString().toLongOrNull() ?: run {
        Core.settings.put(it.key, it.def())
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value)
})

open class FloatSettingKey(key: String, defaultProvider: () -> Float) : SettingKey<Float>(key, defaultProvider, {
    val value: Float = Core.settings[it.key, it.def()].toString().toFloatOrNull() ?: run {
        Core.settings.put(it.key, it.def())
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value)
})
open class DoubleSettingKey(key: String, defaultProvider: () -> Double) : SettingKey<Double>(key, defaultProvider, {
    val value: Double = Core.settings[it.key, it.def()].toString().toDoubleOrNull() ?: run {
        Core.settings.put(it.key, it.def())
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value)
})
