package cn.Arctic.Module.modules.COMBAT;


import java.awt.Color;
import java.util.Random;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Player.PlayerUtil;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {
    private static final String UnicodeFontRenderer = null;
	public static Mode mode = new Mode("Mode", CritMode.values(), CritMode.Hypixel);
    public static boolean Crit;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static TimerUtil timer = new TimerUtil();
    public static Numbers<Double> Delay = new Numbers("Delay", 200,0,2000.0,50.0);

    private Option<Boolean>Random = new Option<Boolean>("Random", true);
    private static java.util.Random random = new Random();

    public Criticals() {
        super("Criticals", new String[] { "criticals", "crit" }, ModuleType.Combat);
        addValues(mode, Delay, Random);
        this.setColor(new Color(0xEFFF08).getRGB());
    }

    private static boolean canCrit() {
        if (Minecraft.player.onGround && !PlayerUtil.isInWater() && !ModuleManager.getModuleByName("Fly").isEnabled() && !ModuleManager.getModuleByName("Speed").isEnabled()) {
            return true;
        }
        return false;
    }

    public static void Crit4(Double[] value) {
        NetworkManager var1 = mc.player.sendQueue.getNetworkManager();
        Double curX = mc.player.posX;
        Double curY = mc.player.posY;
        Double curZ = mc.player.posZ;
        for (Double offset : value) {
            var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
        }
    }
    @EventHandler
    public void onPacket(EventPacketSend e) {
        if (mc.player == null) return;

        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        if (e.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity c02PacketUseEntity = (C02PacketUseEntity) e.getPacket();

            if (c02PacketUseEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
                if (mode.getValue() == CritMode.Jump && mc.player.onGround) {
                    mc.player.jump();
                }

                if (mode.getValue() == CritMode.LowHop && mc.player.onGround) {
                    mc.player.motionY = 0.2;
                }

                if (mode.getValue() == CritMode.Fall && mc.player.onGround) {
                    mc.player.setPosition(x,y + 1,z);
                }

//                if (mode.getValue() == CritMode.Hypixel2&& mc.player.onGround) {
//                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
//                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1154, z, false));
//                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11451, z, false));
//                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.114514, z, false));
//                }

                if (mode.getValue() == CritMode.Hypixel && mc.player.onGround&&!mc.player.isInWater()&&!mc.player.isInLava()&&timer.hasReached((Delay.getValue()).doubleValue())) {
                	if (((C02PacketUseEntity) e.getPacket()).getEntityFromWorld(mc.world).hurtResistantTime < 15) {
                		 mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0412622959183674, z, false));
                         mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
                         mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0412622959183674, z, false));
                         mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
                         mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.001, z, false));

                        timer.reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        setSuffix(this.mode.getValue());
    }

    private double random(double min, double max) {
        Random random = new Random();
        return min + (random.nextDouble() * (max - min));
    }

    public static void Crit(Double[] value) {
        NetworkManager var1 = Minecraft.player.sendQueue.getNetworkManager();
        Double curX = Minecraft.player.posX;
        Double curY = Minecraft.player.posY;
        Double curZ = Minecraft.player.posZ;
        for (Double offset : value) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
        }
        
        /*
        Double RandomY = 0.0;
        for (Double offset : value) {
            if ((Random.getValue()).booleanValue())
                RandomY = RandomUtils.nextDouble(0.0D, 9.999999999E9D) / 1.0E15D;
            if (random.nextBoolean())
                RandomY = -RandomY.doubleValue();
            curY = curY.doubleValue() + offset.doubleValue();
                var1.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX.doubleValue(), curY.doubleValue() + RandomY.doubleValue(), curZ.doubleValue(), false));
        }
         */
    }
/*
    public static void Crit2(final Double[] value) {
        double curX = mc.thePlayer.posX;
        double curY = mc.thePlayer.posY;
        double curZ = mc.thePlayer.posZ;
        for (Double offset : value) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY + offset, curZ, false));
        }
    }

 */



    enum CritMode {
       Hypixel,Fall,LowHop,Jump;
    }
}