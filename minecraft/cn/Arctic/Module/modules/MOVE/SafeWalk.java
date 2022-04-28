/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.MOVE;

import java.awt.Color;

import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[]{"eagle", "parkour"}, ModuleType.Movement);
        this.setColor(new Color(198, 253, 191).getRGB());
    }
}

