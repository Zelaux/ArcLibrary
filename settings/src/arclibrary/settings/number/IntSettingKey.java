//
// Decompiled by Procyon v0.5.36
//

package arclibrary.settings.number;

import arc.func.Prov;


public class IntSettingKey extends NumberSettingKey<Integer> {
    public IntSettingKey( String key,  Prov<Integer> defaultProvider) {
        super(key,Integer::parseInt, defaultProvider);
    }

}
