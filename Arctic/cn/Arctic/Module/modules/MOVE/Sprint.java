/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.MOVE;

import java.awt.*;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Sprint
extends Module {
    private Option<Boolean> omni = new Option<Boolean>("omni", true);

    public Sprint() {
        super("Sprint", new String[]{"run"}, ModuleType.Movement);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.addValues(this.omni);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.player.getFoodStats().getFoodLevel() > 6 && !mc.player.isSneaking() && this.omni.getValue() != false ? MoveUtils.isMoving() : this.mc.player.moveForward > 0.0f) {
            this.mc.player.setSprinting(true);
        }
  
    }
}

