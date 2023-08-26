//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings.number;

import arc.func.Prov;
import org.jetbrains.annotations.NotNull;


public class FloatSettingKey extends NumberSettingKey<Float> {
    public FloatSettingKey(@NotNull final String key, @NotNull final Prov<Float> defaultProvider) {
        super(key,Float::parseFloat, defaultProvider);
    }
}
