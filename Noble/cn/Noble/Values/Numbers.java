/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Values;

public class Numbers<T extends Number>
extends Value<T> {
    private String name;
    public T min;
    public T max;
    public T inc;
    private boolean integer;
	public float hover = 1;
	public float lastHover = 1;
    
    public Numbers(String displayName, T value, T min, T max, T inc) {
        super(displayName);
        this.setValue(value);
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.integer = false;
    }

    public T getMinimum() {
        return this.min;
    }

    public T getMaximum() {
        return this.max;
    }

    public void setIncrement(T inc) {
        this.inc = inc;
    }

    public T getIncrement() {
        return this.inc;
    }

    public String getId() {
        return this.name;
    }
    public boolean isInteger() {
        return this.integer;
    }
}

