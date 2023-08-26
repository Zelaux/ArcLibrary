package arclibrary.ui.defaults

import arc.*
import arc.graphics.*
import arc.scene.style.*
import java.io.*
import kotlin.reflect.*

object DefaultBackground {
    @JvmStatic
    @get:JvmName("white")
    @set:JvmName("white")
    var white: Drawable by mutableLazy {
        val pixmap = Pixmap(1, 1)
        pixmap[0, 0] = Color.whiteRgba

        TextureRegionDrawable(Core.atlas.white())
    }

    @JvmStatic
    @get:JvmName("black6")
    @set:JvmName("black6")
    var black6: Drawable by mutableLazy {
        TextureRegionDrawable(Core.atlas.white()).tint(0f, 0f, 0f, 0.6f)
    }
}

internal object UNINITIALIZED_VALUE
internal class MutableLazy<out T>(initializer: () -> T, lock: Any? = null) : Serializable {
    private var initializer: (() -> T)? = initializer

    @Volatile
    private var _value: Any? = UNINITIALIZED_VALUE


    operator fun setValue(t: Any, property: KProperty<Any?>, v: @UnsafeVariance T) {
        value = v;
    }

    operator fun getValue(t: Any, property: KProperty<Any?>): @UnsafeVariance T = this.value

    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    var value: @UnsafeVariance T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }
        set(value) {
            _value = value;
        }

    fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "MutableLazyImpl value not initialized yet."

}

internal fun <T> mutableLazy(initializer: () -> T): MutableLazy<T> = MutableLazy(initializer)