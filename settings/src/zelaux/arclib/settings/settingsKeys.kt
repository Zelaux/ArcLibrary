package zelaux.arclib.settings

import arc.*
import arc.util.serialization.*

open class StringSettingKey(key: String, defaultProvider: () -> String) : SettingKey<String>(key, defaultProvider, {
    Core.settings[it.key, it.def()].toString()
}, { value, key ->
    Core.settings.put(key.key, value)
})

open class BooleanSettingKey(key: String, defaultProvider: () -> Boolean) : SettingKey<Boolean>(key, defaultProvider, {
    val value: Boolean = Core.settings[it.key, it.def()].toString().toBooleanStrictOrNull() ?: run {
        Core.settings.put(it.key, it.def())
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value)
})

open class JvalSettingKey(key: String, defaultProvider: () -> Jval) : SettingKey<Jval>(key, defaultProvider, {
    val value: Jval = Core.settings[it.key, it.def()].toString().let { str -> Jval.read(str) } ?: run {
        Core.settings.put(it.key, it.def().toString(Jval.Jformat.formatted))
        return@run it.def()
    }
    value
}, { value, key ->
    Core.settings.put(key.key, value.toString(Jval.Jformat.formatted))
})

