//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings.number;

import arc.func.Prov;
import org.jetbrains.annotations.NotNull;


public class LongSettingKey extends NumberSettingKey<Long> {
    public LongSettingKey(@NotNull final String key, @NotNull final Prov<Long> defaultProvider) {
        super(key, Long::parseLong, defaultProvider);
    }
}
