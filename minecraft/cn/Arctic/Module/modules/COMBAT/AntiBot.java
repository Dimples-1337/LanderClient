package cn.Arctic.Module.modules.COMBAT;


import java.util.ArrayList;
import java.util.List;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Mode;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    public Mode<Enum> mode;
    public static ArrayList<EntityPlayer> Bots;
    private TimerUtil timer;
    public static List<EntityPlayer> invalid = new ArrayList();
    private static List<EntityPlayer> removed = new ArrayList();
    int bots;

    static {
        Bots = new ArrayList<>();
    }

    public AntiBot() {
        super("AntiBot", new String[]{"AntiBot"}, ModuleType.Combat);
        this.mode = new Mode<Enum>("Mode", AntiMode.values(), AntiMode.Hypixel);
        this.timer = new TimerUtil();
        this.bots = 0;
        this.addValues(this.mode);

    }

    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == AntiMode.Hypixel) {
            for (final Object entities : mc.world.loadedEntityList) {
                if (entities instanceof EntityPlayer) {
                    final EntityPlayer entityPlayer2;
                    final EntityPlayer entity = entityPlayer2 = (EntityPlayer) entities;
                    if (entityPlayer2 != mc.player) {
                        if (mc.player.getDistanceToEntity(entity) < 10) {
                            if (!entity.getDisplayName().getFormattedText().contains("\u00a7") || entity.isInvisible() //startwith
                                    || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")
                                    || entity.getDisplayName().getFormattedText().toLowerCase().contains("\u00a7r")) {
                                AntiBot.Bots.add(entity);
                            }
                        }
                        if (!AntiBot.Bots.contains(entity)) {
                            continue;
                        }
                        AntiBot.Bots.remove(entity);
                    }
                }
                this.bots = 0;
                for (final Entity entity2 : mc.world.getLoadedEntityList()) {
                    if (entity2 instanceof EntityPlayer) {
                        final EntityPlayer entityPlayer;
                        final EntityPlayer ent = entityPlayer = (EntityPlayer) entity2;
                        if (entityPlayer == mc.player) {
                            continue;
                        }
                        if (!ent.isInvisible()) {
                            continue;
                        }
                        if (ent.ticksExisted <= 105) {
                            continue;
                        }
                        if (getTabPlayerList().contains(ent)) {
                            if (!ent.isInvisible()) {
                                continue;
                            }
                            ent.setInvisible(false);
                        } else {
                            ent.setInvisible(false);
                            ++this.bots;
                            mc.world.removeEntity(ent);
                        }
                    }
                }
                if (this.bots != 0) {

                }
            }
        }

        if (this.mode.getValue() == AntiMode.HYT) {
            for (Entity entity : mc.world.getLoadedEntityList()) {

            }
        }
    }

    public boolean isInGodMode(final Entity entity) {
        return this.isEnabled() && this.mode.getValue() == AntiMode.Hypixel && entity.ticksExisted <= 25;
    }

    public static boolean isServerBot(Entity entity) {
        return ((AntiBot) ModuleManager.getModuleByClass(AntiBot.class)).isBot(entity);
    }

    public boolean isBot(Entity entity) {
        if (isEnabled()) {
            if (mode.getValue() == AntiMode.Hypixel) {
                if (entity instanceof EntityMob) {
                    return false;
                }

                if (entity instanceof EntityAnimal) {
                    return false;
                }

                if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible()
                        && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[NPC]")) {
                    if (isInGodMode(entity)) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            if (this.mode.getValue() == AntiMode.HYT) {
       		  if (((EntityLivingBase)entity).isPlayerSleeping()) {
       			  return true;
       		  }

            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        AntiBot.Bots.clear();
    }

    @Override
    public void onDisable() {
        AntiBot.Bots.clear();
    }

    public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = mc.player.sendQueue;
        final ArrayList list = new ArrayList();
        final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            final ArrayList list2 = list;
            list2.add(mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return (List<EntityPlayer>) list;
    }

    enum AntiMode {
        Hypixel("Hypixel", 0),
        HYT("HYT", 1);

        private AntiMode(final String s, final int n) {
        }
    }
}