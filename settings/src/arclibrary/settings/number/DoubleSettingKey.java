//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings.number;

import arc.func.Prov;
import org.jetbrains.annotations.NotNull;


public class DoubleSettingKey extends NumberSettingKey<Double> {
    public DoubleSettingKey(@NotNull final String key, @NotNull final Prov<Double> defaultProvider) {
        super(key,Double::parseDouble, defaultProvider);
    }
}
