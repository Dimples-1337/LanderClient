/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.values;

import cn.Arctic.Util.animate.Translate;

public abstract class Value<V> {
    public int anim;
    private String displayName;
    private String name;
    public V value;

	public Translate Anim = new Translate(0,0);
	public boolean isOpen = false;
    
    public Value(String name) {
        this.name = name;
    }
    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}

