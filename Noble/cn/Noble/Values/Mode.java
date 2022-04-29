/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Values;

import cn.Noble.Font.CFontRenderer;

public class Mode<V extends Enum>
extends Value<V> {
    private V[] modes;
    
    public Mode(String displayName, V[] modes, V value) {
        super(displayName);
        this.modes = modes;
        this.setValue(value);
    }

    public V[] getModes() {
        return this.modes;
    }

    public String getModeAsString() {
        return ((Enum)this.getValue()).name();
    }

    public void setMode(String mode) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(mode)) {
                this.setValue(e);
            }
            ++n2;
        }
    }

    public boolean isValid(String name) {
        V[] arrV = this.modes;
        int n = arrV.length;
        int n2 = 0;
        while (n2 < n) {
            V e = arrV[n2];
            if (e.name().equalsIgnoreCase(name)) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public String getModeName() {
        return ((Enum) this.getValue()).name();
    }

    public Enum getMode() {
        return this.getValue();
    }

	public int getLongestLen(CFontRenderer font)
	{
		int len = 0;
		
		for (V v: modes)
		{
			int l = (int) font.getStringWidth(v.toString());
			if(len < l)
				len = l;
		}
		return len;
	}
}

