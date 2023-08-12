//
// Decompiled by Procyon v0.5.36
//

package zelaux.arclib.settings.number;

import kotlin.text.StringsKt;
import arc.Core;
import kotlin.jvm.internal.Intrinsics;
import arc.func.Prov;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import zelaux.arclib.settings.SettingKey;


public class FloatSettingKey extends NumberSettingKey<Float> {
    public FloatSettingKey(@NotNull final String key, @NotNull final Prov<Float> defaultProvider) {
        super(key,Float::parseFloat, defaultProvider);
    }
}
