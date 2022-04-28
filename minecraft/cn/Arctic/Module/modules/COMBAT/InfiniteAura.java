package cn.Arctic.Module.modules.COMBAT;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import cn.Arctic.Client;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventMove;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.GUI.NewNotification.NotificationPublisher;
import cn.Arctic.GUI.NewNotification.NotificationType;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.PLAYER.Teams;
import cn.Arctic.Util.Colors;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.Util.dick.JigsawReach;
import cn.Arctic.Util.math.RotationUtil;
import cn.Arctic.Util.render.RenderTools;
import cn.Arctic.Util.render.RenderUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.optifine.util.MathUtils;

public class InfiniteAura extends Module {
    private TimeHelper timer = new TimeHelper();
    private TimerUtil stoptimer = new TimerUtil();
    boolean canaura;
    private Numbers<Double> reach = new Numbers("Range", 50.0D, 10.0D, 100.0D,5);
    private Numbers<Double> delay = new Numbers("Delay", 100D, 0.1D, 1000.0D,1);
    public Numbers<Double> maxTargets = new Numbers("Targets", Double.valueOf(3.0D), Double.valueOf(1.0D), Double.valueOf(20.0D), 1.0D);
    private Option<Boolean> block = new Option("AutoBlock", true);
    private EntityOtherPlayerMP ent;

    public InfiniteAura() {
        super("InfiniteAura",new String[]{"tpaura","reachaura","infinityaura"}, ModuleType.Combat);
        this.addValues(reach,delay,maxTargets,block);
    }

    public static EntityLivingBase getClosestEntity1() {
        EntityLivingBase closestEntity = null;
        Iterator<?> var2 = Minecraft.getMinecraft().world.loadedEntityList.iterator();

        while (var2.hasNext()) {
            Object o = var2.next();
            EntityLivingBase entityplayer = (EntityLivingBase) o;
            if (!(o instanceof ClientPlayerEntity) && !entityplayer.isDead && entityplayer.getHealth() > 0.0F && Minecraft.getMinecraft().player.canEntityBeSeen(entityplayer) && !entityplayer.getName().equals(Minecraft.getMinecraft().player.getName()) && (closestEntity == null || Minecraft.getMinecraft().player.getDistanceToEntity(entityplayer) < Minecraft.getMinecraft().player.getDistanceToEntity(closestEntity))) {
                closestEntity = entityplayer;
            }
        }

        return closestEntity;
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
    	if(!canaura) return;
    	if(stoptimer.hasReached(2500)) this.setEnabled(false);
        float xAdd = new Random().nextInt(2) - new Random().nextFloat() * 2;
        Entity en = getOptimalTarget();
        if ((en == null) || (Minecraft.getMinecraft().player.getDistanceToEntity(en) > reach.getValue() && !Minecraft.getMinecraft().player.isInvisible())) {
            return;
        }

        float[] rots = RotationUtil.getRotations(en);
        ent.setPositionAndRotation(en.posX, en.posY, en.posZ, rots[0], rots[1]);
        ent.rotationYawHead = rots[0];
        ent.rotationYaw = rots[0];
        ent.rotationPitch = rots[1];
        ent.motionX = ent.motionY = 100;
        ent.swingItem();

        if(timer.isDelayComplete(delay.getValue().longValue())) {
            ArrayList<Vec3> posBack = new ArrayList<Vec3>();
            posBack.add(new Vec3(mc.player.posX,mc.player.posY,mc.player.posZ));

            ArrayList<Vec3> pos = new ArrayList<Vec3>();
            pos.add(new Vec3(en.posX,en.posY,en.posZ));

            if(block.getValue()) mc.gameSettings.keyBindUseItem.pressed = true;
            if(mc.player.getDistanceToEntity(en) < 5) {
                JigsawReach.attackInf((EntityLivingBase) en);
            } else {
                JigsawReach.infiniteReach(reach.getValue(), 9.5, 9, posBack, pos, (EntityLivingBase)en);
            }



            mc.player.swingItem();

            timer.reset();
        }

    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindUseItem.pressed = false;
        this.mc.world.removeEntityFromWorld(-1);
        super.onDisable();
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onEnable() {
        
        if (mc.player == null)
			return;
		canaura = false;
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
		playerCapabilities.isFlying = true;
		playerCapabilities.allowFlying = true;
//		playerCapabilities.setFlySpeed((float) MathUtils.randomNumber(0.1, 9.0));
		mc.getNetHandler().addToSendQueue(new C0FPacketConfirmTransaction(0, (short)(-1), false));
		mc.getNetHandler().addToSendQueue(new C13PacketPlayerAbilities(playerCapabilities));
		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,
			mc.player.posY + 0.17, mc.player.posZ, true));
		mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.player.posX,
			mc.player.posY + 0.06, mc.player.posZ, true));
		mc.player.stepHeight = 0.0f;
		mc.player.motionX = 0.0;
		mc.player.motionZ = 0.0;
		
		double x = this.mc.player.posX;
        double y = this.mc.player.posY;
        double z = this.mc.player.posZ;
        float yaw = this.mc.player.rotationYaw;
        float pitch = this.mc.player.rotationPitch;
        ent = new EntityOtherPlayerMP(this.mc.world, this.mc.player.getGameProfile());
        ent.inventory = this.mc.player.inventory;
        ent.inventoryContainer = this.mc.player.inventoryContainer;
        this.mc.world.addEntityToWorld(-1, ent);
        super.onEnable();
    }
    
    @EventHandler
	public final void onMove(EventMove event) {
//		MovementUtils.setSpeed(event, 0.0);
//		mc.player.motionY = 0.0;
//		event.y = 0.0;
	}
    
    @EventHandler
	public final void onSendPacket(EventPacketSend event) {
		if (!canaura && event.getPacket() instanceof C03PacketPlayer) {
			event.setCancelled(true);
		}
	}

    @EventHandler
	public final void onReceivePacket(EventPacketRecieve event) {
		if (event.getPacket() instanceof S08PacketPlayerPosLook) {
			if (!this.canaura) {
				this.canaura = true;
				this.stoptimer.reset();
			}
		}
	}

    private Entity getOptimalTarget() {
        List<Entity> load = new ArrayList<Entity>();
        for (Entity o : mc.world.getLoadedEntityList()) {
            if (o instanceof Entity) {
                Entity ent = o;
                if (!this.validEntity(ent)) {
                    continue;
                }
                load.add(ent);
            }
        }
        if (load.isEmpty()) {
            return null;
        }
        return this.getTarget(load);
    }

    private Entity getTarget(List<Entity> list) {
        this.sortList(list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(new Random().nextInt(maxTargets.getValue().intValue()));
    }

    private void sortList(List<Entity> weed) {
        weed.sort((o1, o2) -> (int)(o1.getDistanceToEntity(mc.player) - o2.getDistanceToEntity(mc.player)));
    }

    private boolean validEntity(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this.mc.player) {
            return false;
        }
        if ((double)this.mc.player.getDistanceToEntity(entity) > reach.getValue() + 1.0) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (this.mc.player.isDead) {
            return false;
        }
//        if (entity instanceof EntityPlayer 
//        		&& (Teams.isOnSameTeam(entity) || AntiBot.isServerBot(entity))
//        		) {
//            return false;
//        }
        if (!(entity instanceof EntityLivingBase)) {
            return false;
        }
        return true;
    }
}
