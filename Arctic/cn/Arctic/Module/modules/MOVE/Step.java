
package cn.Arctic.Module.modules.MOVE;



import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import cn.Arctic.Event.EventCategory;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.Step.EventPostStep;
import cn.Arctic.Event.Step.EventPreStep;
import cn.Arctic.Event.events.EventStep;
import cn.Arctic.Event.events.EventUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Step
extends Module {
	private Numbers<Double> height = new Numbers<Double>("Height", 1.0, 1.0, 2.0, 0.5);
	private Numbers<Double> DELAY = new Numbers<Double>("Delay", 2.0, 0.0, 2.0, 0.1);
    boolean resetTimer;
    private TimerUtil time = new TimerUtil();

    public Step() {
        super("Step", new String[]{"step"}, ModuleType.Movement);
        this.addValues(height,DELAY);
        this.setColor(new Color(0xFF6ADC).getRGB());
    }
    @Override
    public void onEnable() {
        this.resetTimer = false;
    }

    @Override
    public void onDisable() {
        if (this.mc.player != null) {
            this.mc.player.stepHeight = 0.0f;
        }
        this.mc.timer.timerSpeed = 1.0f;
    }

    @EventHandler
    public void onUpdate(EventUpdate eventUpdate) {
        if (this.mc.timer.timerSpeed < 1.0f && this.mc.player.onGround) {
            this.mc.timer.timerSpeed = 1.0f;
        }
    }

    @EventHandler
    public void onStep(EventStep event) {
        if (ModuleManager.getModuleByClass(Flight.class).isEnabled()
                || ModuleManager.getModuleByClass(Scaffold.class).isEnabled()) {
            return;
        }
        if (!mc.player.isInWater()) {
            if (this.resetTimer) {
                boolean resetTimer;
                if (!this.resetTimer) {
                    resetTimer = true;
                } else {
                    resetTimer = false;
                }
                this.resetTimer = resetTimer;
                this.mc.timer.timerSpeed = 1.0f;
            }
            if (event.getEventType() == EventCategory.PRE) {
                if (!this.mc.player.onGround || !this.time.isDelayComplete(this.DELAY.getValue().longValue())) {
                    event.setHeight(this.mc.player.stepHeight = 0.5f);
                    return;
                }
                this.mc.player.stepHeight = this.height.getValue().floatValue();
                event.setHeight(this.height.getValue().floatValue());
            } else if (event.getHeight() > 0.5) {
                final double n = this.mc.player.getEntityBoundingBox().minY - this.mc.player.posY;
                if (n >= 0.625) {
                    final float n2 = 0.6f;
                    float n3;
                    if (n >= 1.0) {
                        n3 = Math.abs(1.0f - (float) n) * 0.33f;
                    } else {
                        n3 = 0.0f;
                    }
                    mc.timer.timerSpeed = 1.0f;
                    if (this.mc.timer.timerSpeed <= 0.05f) {
                        this.mc.timer.timerSpeed = 0.05f;
                    }
                    this.resetTimer = true;
                    this.ncpStep(n);
                    this.time.reset();
                }
            }
        }
    }

    void ncpStep(final double n) {
        final List<Double> list = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        final double posX = this.mc.player.posX;
        final double posZ = this.mc.player.posZ;
        double posY = this.mc.player.posY;
        if (n < 1.1) {
            double n2 = 0.42;
            double n3 = 0.75;
            if (n != 1.0) {
                n2 *= n;
                n3 *= n;
                if (n2 > 0.425) {
                    n2 = 0.425;
                }
                if (n3 > 0.78) {
                    n3 = 0.78;
                }
                if (n3 < 0.49) {
                    n3 = 0.49;
                }
            }
            if (n2 == 0.42) {
                n2 = 0.41999998688698;
            }
            this.mc.player.sendQueue
                    .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + n2, posZ, false));
            if (posY + n3 < posY + n) {
                this.mc.player.sendQueue
                        .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + n3, posZ, false));
            }
            return;
        }
        if (n < 1.6) {
            int i = 0;
            while (i < list.size()) {
                posY += list.get(i);
                this.mc.player.sendQueue
                        .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, false));
                ++i;
            }
        } else if (n < 2.1) {
            final double[] array = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            final int length = array.length;
            int j = 0;
            while (j < length) {
                this.mc.player.sendQueue.addToSendQueue(
                        new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array[j], posZ, false));
                ++j;
            }
        } else {
            final double[] array2 = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            final int length2 = array2.length;
            int k = 0;
            while (k < length2) {
                this.mc.player.sendQueue.addToSendQueue(
                        new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + array2[k], posZ, false));
                ++k;
            }
        }
    }
}
