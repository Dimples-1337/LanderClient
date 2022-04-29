package cn.Noble.Module.modules.UHC;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.GUI.NewNotification.NotificationPublisher;
import cn.Noble.GUI.NewNotification.NotificationType;
import cn.Noble.GUI.notifications.Notification;
import cn.Noble.GUI.notifications.Notification.Type;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Chat.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.AxisAlignedBB;

public class LightningChecker extends Module {
    public LightningChecker() {
        super("LightningDetect", new String[] { "lightning", "ld"}, ModuleType.World);
        setColor(new Color(223, 233, 233).getRGB());
    }
	
    @EventHandler
    public void onPacketReceive(EventPacketRecieve packetEvent) {
        if (packetEvent.getPacket() instanceof S2CPacketSpawnGlobalEntity) {
            final S2CPacketSpawnGlobalEntity packetIn = (S2CPacketSpawnGlobalEntity)packetEvent.getPacket();
            if (packetIn.func_149053_g() == 1) {
                final int x = packetIn.func_149051_d() / 32;
                final int y = packetIn.func_149050_e() / 32;
                final int z = packetIn.func_149049_f() / 32;
             NotificationPublisher.queue("LightningCheck","\u00a7aX : \u00a77" + x + " \u00a7a, Y : \u00a77" + y + " \u00a7a, Z : \u00a77" + z, NotificationType.INFO, 10000);
             Helper.sendMessageWithoutPrefix("\u00a7bFoundLightning! \u00a7a X : \u00a77" + x + " \u00a7a, Y : \u00a77" + y + " \u00a7a, Z : \u00a77" + z);
          }
       }

    }
}