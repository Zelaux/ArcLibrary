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


public class IntSettingKey extends NumberSettingKey<Integer> {
    public IntSettingKey( String key,  Prov<Integer> defaultProvider) {
        super(key,Integer::parseInt, defaultProvider);
    }

}
