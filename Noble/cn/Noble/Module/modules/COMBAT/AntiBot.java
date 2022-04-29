package cn.Noble.Module.modules.COMBAT;

import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.network.*;
import java.util.*;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventAttack;
import cn.Noble.Event.events.EventPacketRecieve;
import cn.Noble.Event.events.EventTick;
import cn.Noble.GUI.NewNotification.NotificationPublisher;
import cn.Noble.GUI.NewNotification.NotificationType;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Player.PlayerUtil;
import cn.Noble.Util.render.ColorUtils;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;

public class AntiBot extends Module
{
    private final Option<Boolean> tabValue;
    private final Mode<Enum> tabModeValue;
    private final Option<Boolean> entityIDValue;
    private final Option<Boolean> colorValue;
    private final Option<Boolean> livingTimeValue;
    private final Numbers<Double> livingTimeTicksValue;
    private final Option<Boolean> groundValue;
    private final Option<Boolean> airValue;
    private final Option<Boolean> invalidGroundValue;
    private final Option<Boolean> swingValue;
    private final Option<Boolean> healthValue;
    private final Option<Boolean> derpValue;
    private final Option<Boolean> wasInvisibleValue;
    private final Option<Boolean> armorValue;
    private final Option<Boolean> pingValue;
    private final Option<Boolean> needHitValue;
    private final Option<Boolean> duplicateInWorldValue;
    private final Option<Boolean> duplicateInTabValue;
    private final List<Integer> ground;
    private final List<Integer> air;
    private final Map<Integer, Integer> invalidGround;
    private final List<Integer> swing;
    private final List<Integer> invisible;
    private final List<Integer> hitted;
    
    public AntiBot() {
        super("AntiBot", new String[] { "AAB" }, ModuleType.Combat);
        this.tabValue = new Option<Boolean>("Tab", true);
        this.tabModeValue = new Mode<Enum>("TabMode", AABmode.values(), AABmode.Contains);
        this.entityIDValue = new Option<Boolean>("EntityID", true);
        this.colorValue = new Option<Boolean>("Color", false);
        this.livingTimeValue = new Option<Boolean>("LivingTime", false);
        this.livingTimeTicksValue = new Numbers<Double>("LivingTimeTicks", 40.0, 1.0, 200.0, 0.1);
        this.groundValue = new Option<Boolean>("Ground", true);
        this.airValue = new Option<Boolean>("Air", false);
        this.invalidGroundValue = new Option<Boolean>("InvalidGround", true);
        this.swingValue = new Option<Boolean>("Swing", false);
        this.healthValue = new Option<Boolean>("Health", false);
        this.derpValue = new Option<Boolean>("Derp", true);
        this.wasInvisibleValue = new Option<Boolean>("WasInvisible", false);
        this.armorValue = new Option<Boolean>("Armor", false);
        this.pingValue = new Option<Boolean>("Ping", false);
        this.needHitValue = new Option<Boolean>("NeedHit", false);
        this.duplicateInWorldValue = new Option<Boolean>("DuplicateInWorld", false);
        this.duplicateInTabValue = new Option<Boolean>("DuplicateInTab", false);
        this.ground = new ArrayList<Integer>();
        this.air = new ArrayList<Integer>();
        this.invalidGround = new HashMap<Integer, Integer>();
        this.swing = new ArrayList<Integer>();
        this.invisible = new ArrayList<Integer>();
        this.hitted = new ArrayList<Integer>();
        this.addValues(this.tabValue, this.tabModeValue, this.entityIDValue, this.colorValue, this.livingTimeValue, this.livingTimeTicksValue, this.groundValue, this.airValue, this.invalidGroundValue, this.swingValue, this.healthValue, this.derpValue, this.wasInvisibleValue, this.armorValue, this.pingValue, this.needHitValue, this.duplicateInWorldValue, this.duplicateInTabValue);
    }
    
    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }
    
    @EventHandler
    public void onPacket(final EventPacketRecieve event) {
        if (AntiBot.mc.player == null || AntiBot.mc.world == null) {
            return;
        }
        final Packet<?> packet = (Packet<?>)event.getPacket();
        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity packetEntity = (S14PacketEntity)event.getPacket();
            final Entity entity = packetEntity.getEntity(AntiBot.mc.world);
            if (entity instanceof EntityPlayer) {
                if (packetEntity.getOnGround() && !this.ground.contains(entity.getEntityId())) {
                    this.ground.add(entity.getEntityId());
                }
                if (!packetEntity.getOnGround() && !this.air.contains(entity.getEntityId())) {
                    this.air.add(entity.getEntityId());
                }
                if (packetEntity.getOnGround()) {
                    if (entity.prevPosY != entity.posY) {
                        this.invalidGround.put(entity.getEntityId(), this.invalidGround.getOrDefault(entity.getEntityId(), 0) + 1);
                    }
                }
                else {
                    final int currentVL = this.invalidGround.getOrDefault(entity.getEntityId(), 0) / 2;
                    if (currentVL <= 0) {
                        this.invalidGround.remove(entity.getEntityId());
                    }
                    else {
                        this.invalidGround.put(entity.getEntityId(), currentVL);
                    }
                }
                if (entity.isInvisible() && !this.invisible.contains(entity.getEntityId())) {
                    this.invisible.add(entity.getEntityId());
                }
            }
        }
        if (packet instanceof S0BPacketAnimation) {
            final S0BPacketAnimation packetAnimation = (S0BPacketAnimation)event.getPacket();
            final Entity entity = AntiBot.mc.world.getEntityByID(packetAnimation.getEntityID());
            if (entity instanceof EntityLivingBase && packetAnimation.getAnimationType() == 0 && !this.swing.contains(entity.getEntityId())) {
                this.swing.add(entity.getEntityId());
            }
        }
    }
    
    @EventHandler
    public void onAttack(EventAttack e) {
        final Entity entity = e.getEntity();
        if (entity instanceof EntityLivingBase && !this.hitted.contains(entity.getEntityId())) {
            this.hitted.add(entity.getEntityId());
        }
    }

	@EventHandler
	private void onTick(EventTick event) {
			if (mc.world == null) {
				this.clearAll();
				return;
			}
			if (mc.player.ticksExisted <= 1) {
				this.clearAll();
				return;
			}
		
	}
    
    private void clearAll() {
        this.hitted.clear();
        this.swing.clear();
        this.ground.clear();
        this.invalidGround.clear();
        this.invisible.clear();
    }
    
    public static boolean isEntityBot(final Entity entity) {
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        AntiBot antiBot = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
        if (antiBot == null || !antiBot.isEnabled()) {
            return false;
        }
        if (antiBot.colorValue.getValue() && !entity.getDisplayName().getFormattedText().replace("\u6402r", "").contains("\u6402")) {
            return true;
        }
        if (antiBot.livingTimeValue.getValue() && entity.ticksExisted < antiBot.livingTimeTicksValue.getValue()) {
            return true;
        }
        if (antiBot.groundValue.getValue() && !antiBot.ground.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.airValue.getValue() && !antiBot.air.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.swingValue.getValue() && !antiBot.swing.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.healthValue.getValue() && ((EntityLivingBase)entity).getHealth() > 20.0f) {
            return true;
        }
        if (antiBot.entityIDValue.getValue() && (entity.getEntityId() >= 1000000000 || entity.getEntityId() <= -1)) {
            return true;
        }
        if (antiBot.derpValue.getValue() && (entity.rotationPitch > 90.0f || entity.rotationPitch < -90.0f)) {
            return true;
        }
        if (antiBot.wasInvisibleValue.getValue() && antiBot.invisible.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.armorValue.getValue()) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.inventory.armorInventory[0] == null && player.inventory.armorInventory[1] == null && player.inventory.armorInventory[2] == null && player.inventory.armorInventory[3] == null) {
                return true;
            }
        }
        if (antiBot.pingValue.getValue()) {
            final EntityPlayer player = (EntityPlayer)entity;
//            if (AntiBot.mc.getNetHandler().getPlayerInfo(player.getUniqueID()).getResponseTime() == 0) {
//                return true;
//            }
        }
        if (antiBot.needHitValue.getValue() && !antiBot.hitted.contains(entity.getEntityId())) {
            return true;
        }
        if (antiBot.invalidGroundValue.getValue() && antiBot.invalidGround.getOrDefault(entity.getEntityId(), 0) >= 10) {
            return true;
        }
        if (antiBot.tabValue.getValue()) {
            final boolean equals = antiBot.tabModeValue.getValue() == AABmode.Equals;
            final String targetName = ColorUtils.stripColor(entity.getDisplayName().getFormattedText());
            if (targetName != null) {
                for (final NetworkPlayerInfo networkPlayerInfo : AntiBot.mc.getNetHandler().getPlayerInfoMap()) {
                    final String networkName = ColorUtils.stripColor(PlayerUtil.getName(networkPlayerInfo));
                    if (networkName == null) {
                        continue;
                    }
                    if (equals) {
                        if (targetName.equals(networkName)) {
                            return false;
                        }
                        continue;
                    }
                    else {
                        if (targetName.contains(networkName)) {
                            return false;
                        }
                        continue;
                    }
                }
                return true;
            }
        }
        return (antiBot.duplicateInWorldValue.getValue() && AntiBot.mc.world.loadedEntityList.stream().filter(currEntity -> currEntity instanceof EntityPlayer && currEntity.getDisplayName().equals(currEntity.getDisplayName())).count() > 1L) || (antiBot.duplicateInTabValue.getValue() && AntiBot.mc.getNetHandler().getPlayerInfoMap().stream().filter(networkPlayer -> entity.getName().equals(ColorUtils.stripColor(PlayerUtil.getName(networkPlayer)))).count() > 1L) || entity.getName().isEmpty() || entity.getName().equals(AntiBot.mc.player.getName());
    }
    
    private static /* synthetic */ boolean lambda$isServerBot$1(final Entity entity, final NetworkPlayerInfo networkPlayer) {
        return entity.getName().equals(ColorUtils.stripColor(PlayerUtil.getName(networkPlayer)));
    }
    
    private static /* synthetic */ boolean lambda$isServerBot$0(final Entity currEntity) {
        return currEntity instanceof EntityPlayer && ((EntityPlayer)currEntity).getDisplayName().equals(((EntityPlayer)currEntity).getDisplayName());
    }
    
    enum AABmode
    {
        Equals, 
        Contains;
    }
}
