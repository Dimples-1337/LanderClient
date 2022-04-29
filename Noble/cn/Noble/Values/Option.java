/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Values;

import cn.Noble.Util.Opacity;

public class Option<V>
extends Value<V> {
    public Opacity Anim;
	
	public Option(String displayName, V enabled) {
        super(displayName);
        this.setValue(enabled);
        Anim = new Opacity((boolean)enabled ? 255 : 0);
    }
}

