package cn.Arctic.Module.modules.MOVE;



import java.awt.Color;

import org.lwjgl.input.Mouse;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ClickTeleport
extends Module {
    boolean istrue,down;
    TimerUtil timer = new TimerUtil();
    public ClickTeleport() {
        super("ClickTeleport", new String[]{"Clickteleport"}, ModuleType.Movement);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        MovingObjectPosition ray = this.rayTrace(500.0);
        if (ray == null) {
            return;
        }
        if (Mouse.isButtonDown(2) && !this.down) {
            if (this.istrue == true && timer.delay(500)) {
                istrue = false;
                timer.reset();
                return;
            }
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            double x_new = (double)ray.getBlockPos().getX() + 0.5;
            double y_new = ray.getBlockPos().getY() + 1;
            double z_new = (double)ray.getBlockPos().getZ() + 0.5;
            double distance = this.mc.player.getDistance(x_new, y_new, z_new);
            double d = 0.0;
            while (d < distance) {
                this.setPos(this.mc.player.posX + (x_new - (double)this.mc.player.getHorizontalFacing().getFrontOffsetX() - this.mc.player.posX) * d / distance, this.mc.player.posY + (y_new - this.mc.player.posY) * d / distance, this.mc.player.posZ + (z_new - (double)this.mc.player.getHorizontalFacing().getFrontOffsetZ() - this.mc.player.posZ) * d / distance);
                d += 2.0;
            }
            this.setPos(x_new, y_new, z_new);
            this.mc.renderGlobal.loadRenderers();
            istrue = true;
            this.down = true;
        }

        if (!Mouse.isButtonDown((int)2)) {
            this.down = false;
        }
    }

    public MovingObjectPosition rayTrace(double blockReachDistance) {
        Vec3 vec3 = this.mc.player.getPositionEyes(1.0f);
        Vec3 vec4 = this.mc.player.getLookVec();
        Vec3 vec5 = vec3.addVector(vec4.xCoord * blockReachDistance, vec4.yCoord * blockReachDistance, vec4.zCoord * blockReachDistance);
        return this.mc.world.rayTraceBlocks(vec3, vec5, !this.mc.player.isInWater(), false, false);
    }

    public void setPos(double x, double y, double z) {
        this.mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
        this.mc.player.setPosition(x, y + 1, z);
    }
}

