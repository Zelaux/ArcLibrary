package zelaux.arclib.settings

import arc.Core
import arc.struct.ObjectMap


open class EnumIdSettingKey<T : Enum<T>>(key: String, values: Array<T>, defaultProvider: () -> T) : SettingKey<T>(key, defaultProvider, {
    values[Core.settings.getInt(it.key, it.def().ordinal)]
}, { value, key ->
    Core.settings.put(key.key, value.ordinal)
})

open class EnumNameSettingKey<T : Enum<T>>(key: String, values: Array<T>, defaultProvider: () -> T) : SettingKey<T>(key, defaultProvider,
    { it: SettingKey<T> ->
        it as EnumNameSettingKey<T>
        val def = it.def()
        it.map[Core.settings.getString(it.key, def.name), def]
    }, { value, key ->
        Core.settings.put(key.key, value.name)
    }) {
    val map = ObjectMap<String, T>().also {
        for (value in values) {
            it.put(value.name, value)
        }
    }
}
open class NamedElementSettingKey<T : NamedElementSettingKey.NamedElement>(key: String, values: Array<T>, defaultProvider: () -> T) : SettingKey<T>(key, defaultProvider,
    { it: SettingKey<T> ->
        it as NamedElementSettingKey<T>
        val def = it.def()
        it.map[Core.settings.getString(it.key, def.settingsKey()), def]
    }, { value, key ->
        Core.settings.put(key.key, value.settingsKey())
    }) {
    val map = ObjectMap<String, T>().also {
        for (value in values) {
            it.put(value.settingsKey(), value)
        }
    }
    interface NamedElement{
        fun settingsKey():String
    }
}