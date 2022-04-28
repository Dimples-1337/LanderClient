/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.values;

import cn.Arctic.Util.Opacity;

public class Option<V>
extends Value<V> {
    public Opacity Anim;
	
	public Option(String displayName, V enabled) {
        super(displayName);
        this.setValue(enabled);
        Anim = new Opacity((boolean)enabled ? 255 : 0);
    }

	public boolean get() {
		// TODO 自动生成的方法存根
		return false;
	}
}

