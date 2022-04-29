/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.MOVE;

import java.awt.*;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.MoveUtils;
import cn.Noble.Util.Player.PlayerUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
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

