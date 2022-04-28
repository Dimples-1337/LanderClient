/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;
import net.optifine.shaders.Shaders;

public class EventRender3D
extends Event {
    public float ticks;
    private boolean isUsingShaders;

    public EventRender3D() {
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }
    public static EventRender3D instance = new EventRender3D();
    public static EventRender3D getInstance(){
        return instance;
    }



    public EventRender3D(float ticks) {
        this.ticks = ticks;
        this.isUsingShaders = Shaders.getShaderPackName() != null;
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    public boolean isUsingShaders() {
        return this.isUsingShaders;
    }
}

