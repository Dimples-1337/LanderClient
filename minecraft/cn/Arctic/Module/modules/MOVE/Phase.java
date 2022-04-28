package cn.Arctic.Module.modules.MOVE;


import static java.lang.Math.cos;
import static java.lang.Math.sin;

import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Phase extends Module {
    private Mode<Enum> mode = new Mode<Enum>("Mode", ClipMode.values(), ClipMode.TP);
    private static Numbers<Double> horizontal = new Numbers("horizontal",1.0,-10.0, 10.0, 0.01);
    private static Numbers<Double> vertical = new Numbers("vertical",1.0, -10.0, 10, 0.01);

    public Phase() {
        super("phase",new String[]{}, ModuleType.Movement);
        addValues(mode,horizontal,vertical);
    }
    @Override
    public void onEnable() {
        double yaw = Math.toRadians(mc.player.rotationYaw);
        double x = -sin(yaw) * horizontal.getValue().doubleValue();
        double z = cos(yaw) * horizontal.getValue().doubleValue();
        if(this.mode.getValue() == ClipMode.TP) {
            mc.player.setPosition(mc.player.posX + x,mc.player.posY + vertical.getValue().doubleValue(), mc.player.posZ + z);
        }
        if(this.mode.getValue() == ClipMode.Flag) {
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,mc.player.posY,mc.player.posZ, true));
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5,0,0.5, true));
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,mc.player.posY,mc.player.posZ, true));
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX + x,mc.player.posY + vertical.getValue().doubleValue(),mc.player.posZ + z, true));
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(0.5,0,0.5, true));
            mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX + 0.5,mc.player.posY,mc.player.posZ + 0.5, true));
            mc.player.setPosition(mc.player.posX + -sin(yaw) * 0.04,mc.player.posY,mc.player.posZ + cos(yaw) * 0.04);
        }
        if (this.mode.getValue() == ClipMode.Redesky) {
            double playerYaw = MoveUtils.getDirection();

            mc.player.setPosition(mc.player.posX, mc.player.posY + vertical.getValue(), mc.player.posZ);

            mc.player.setPosition(mc.player.posX + horizontal.getValue() * -Math.sin(playerYaw), mc.player.posY, mc.player.posZ + horizontal.getValue() * Math.cos(playerYaw));

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX + horizontal.getValue() * -Math.sin(playerYaw), mc.player.posY, mc.player.posZ + horizontal.getValue() * Math.cos(playerYaw), false));

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX, mc.player.posY+vertical.getValue(), mc.player.posZ, false));




        }
        setEnabled(false);
    }
    static enum ClipMode{
        TP,Flag,Redesky
    }
}
