/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.MOVE;

import java.awt.Color;

import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", new String[]{"eagle", "parkour"}, ModuleType.Movement);
        this.setColor(new Color(198, 253, 191).getRGB());
    }
}

