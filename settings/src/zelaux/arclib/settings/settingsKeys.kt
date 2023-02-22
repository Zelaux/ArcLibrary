package zelaux.arclib.settings

import arc.Core
import arc.graphics.Color
import arc.util.serialization.Jval

open class ColorSettingKey(key: String, tmpColor: Color? = null, defaultProvider: () -> Color) : SettingKey<Color>(key, defaultProvider, {
    if (tmpColor == null) {
        Color(Core.settings.getInt(it.key, it.def().rgba()))
    } else {
        tmpColor.set(Core.settings.getInt(it.key, it.def().rgba()))
    }
}, { value, key ->
    Core.settings.put(key.key, value.rgba())
}) {
    override fun setDefault() {
        Core.settings.defaults(key, def().rgba())
    }
}

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
}) {
    override fun setDefault() {
        Core.settings.defaults(key, def().toString(Jval.Jformat.formatted))
    }
}

