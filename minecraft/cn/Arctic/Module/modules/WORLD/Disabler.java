package cn.Arctic.Module.modules.WORLD;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.Arctic.Event.Angle;
import cn.Arctic.Event.AngleUtility;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketRecieve;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.EventRespawn;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Event.SoterObfuscator;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.MOVE.Speed;
import cn.Arctic.Module.modules.UHC.TargetStrafe;
import cn.Arctic.Util.Helper;
import cn.Arctic.Util.MoveUtils;
import cn.Arctic.Util.Chat.ChatUtils;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;

public class Disabler extends Module {
    public static AngleUtility angleUtility = new AngleUtility(110, 120, 30, 40);
    public static Angle lastAngle;
    public static float yawDiff;

    public static Option<Boolean> timerBypass = new Option<Boolean>("Hypixel Timer Disabler", true);
    public static Option<Boolean> lobbyCheck = new Option<Boolean>("Hypixel Lobby Check", true);

    Queue<C0FPacketConfirmTransaction> confirmTransactionQueue = new ConcurrentLinkedQueue<>();
    Queue<C00PacketKeepAlive> keepAliveQueue = new ConcurrentLinkedQueue<>();
    public static TimeHelper timer = new TimeHelper();
    TimeHelper lastRelease = new TimeHelper();

    int lastUid, cancelledPackets;
    public static boolean hasDisabled;

    public Disabler() {
    	super("Disabler", new String[] { "Disabler" }, ModuleType.World);
    }

    public CopyOnWriteArrayList<C0EPacketClickWindow> clickWindowPackets = new CopyOnWriteArrayList<>();

    public TimeHelper timedOutTimer = new TimeHelper();
    public boolean isCraftingItem = false;

    @EventHandler
    public void onRespawn(EventRespawn e) {
        confirmTransactionQueue.clear();
        keepAliveQueue.clear();

        hasDisabled = false;
        lastUid = cancelledPackets = 0;

        clickWindowPackets.clear();
        isCraftingItem = false;
    }

    @EventHandler
    public void onSendPacket(EventPacketSend e) {
        if (cn.Arctic.Module.modules.GUI.HUD.getRemoteIp() == "hypixel.net") {
            doInvMove(e);

            if (timerBypass.getValue() && hasDisabled) {
                if (e.getPacket() instanceof C03PacketPlayer && !(e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                    cancelledPackets ++;
                    e.setCancelled(true);
                }
            }

            // Disabler
            if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                processConfirmTransactionPacket(e);
            } else if (e.getPacket() instanceof C00PacketKeepAlive) {
                processKeepAlivePacket(e);
            } else if (e.getPacket() instanceof C03PacketPlayer) {
                processPlayerPosLooksPacket(e);
            }
        }
    }

    public void doInvMove(EventPacketSend e) {
        if (e.getPacket() instanceof C16PacketClientStatus) {
            C16PacketClientStatus clientStatus = ((C16PacketClientStatus) e.getPacket());
            if (clientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) {
                e.setCancelled(true);
            }
        }

        if (e.getPacket() instanceof C0DPacketCloseWindow) {
            C0DPacketCloseWindow closeWindow = ((C0DPacketCloseWindow) e.getPacket());
            if (closeWindow.windowId == 0) {
                if (isCraftingItem) {
                    isCraftingItem = false;
                }
                e.setCancelled(true);
            }
        }

        if (e.getPacket() instanceof C0EPacketClickWindow) {
            C0EPacketClickWindow clickWindow = ((C0EPacketClickWindow) e.getPacket());
            if (clickWindow.getWindowId() == 0) {
                if (!isCraftingItem && clickWindow.getSlotId() >= 1 && clickWindow.getSlotId() <= 4) {
                    isCraftingItem = true;
                }

                if (isCraftingItem && clickWindow.getSlotId() == 0 && clickWindow.getClickedItem() != null) {
                    isCraftingItem = false;
                }

                timedOutTimer.reset();
                e.setCancelled(true);
                clickWindowPackets.add(clickWindow);
            }
        }

        boolean isDraggingItem = false;

        if (Minecraft.getMinecraft().currentScreen instanceof GuiInventory) {
            if (Minecraft.getMinecraft().player.inventory.getItemStack() != null) {
                isDraggingItem = true;
            }
        }

        if (mc.player.ticksExisted % 5 == 0 && !clickWindowPackets.isEmpty() && !isDraggingItem && !isCraftingItem) {
            Helper.sendMessage ("Release Click Packets");
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            for (C0EPacketClickWindow clickWindowPacket : clickWindowPackets) {
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(clickWindowPacket);
            }
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacketNoEvent(new C0DPacketCloseWindow(0));
            clickWindowPackets.clear();
            timedOutTimer.reset();
        }
    }

    @EventHandler
    public void onPacket(EventPacketRecieve e) {
        if (cn.Arctic.Module.modules.GUI.HUD.getRemoteIp() == "hypixel.net") {
            if (e.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook packet = ((S08PacketPlayerPosLook) e.getPacket());
                Helper.sendMessage("S08: " + packet.getX() + " " + packet.getY() + " " + packet.getZ());
                if (!hasDisabled && mc.player.ticksExisted > 20) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onUpdate(EventPreUpdate e) {
        if (cn.Arctic.Module.modules.GUI.HUD.getRemoteIp() == "hypixel.net") {
            if (mc.player.ticksExisted % 40 == 0) {
                int rate = (int) ((cancelledPackets / 40f) * 100);
               Helper.sendMessage("Movement Handler: " + rate + "%");
                cancelledPackets = 0;
            }

            if (MoveUtils.isMoving() &&  ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
                if (!ModuleManager.getModuleByClass(Speed.class).isEnabled()) {
                    float targetYaw = e.getYaw();
                    if (Speed.isTargetStrafing) {
                        targetYaw = TargetStrafe.currentYaw;
                    } else {
                        if (mc.gameSettings.keyBindBack.pressed) {
                            targetYaw += 180;
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw += 45;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw -= 45;
                            }
                        } else if (mc.gameSettings.keyBindForward.pressed) {
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw -= 45;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw += 45;
                            }
                        } else {
                            if (mc.gameSettings.keyBindLeft.pressed) {
                                targetYaw -= 90;
                            }
                            if (mc.gameSettings.keyBindRight.pressed) {
                                targetYaw += 90;
                            }
                        }
                    }
                    Angle angle = angleUtility.smoothAngle(new Angle(targetYaw, e.getYaw()), lastAngle, 120, 360);
                    yawDiff = MathHelper.wrapAngleTo180_float(targetYaw - angle.getYaw());
                    e.setYaw(angle.getYaw());
                }
            }

            lastAngle = new Angle(e.getYaw(), e.getPitch());


            if (hasDisabled) {
                if (confirmTransactionQueue.isEmpty()) {
                    lastRelease.reset();
                } else {
                    if (confirmTransactionQueue.size() >= 6) {
                        while (!keepAliveQueue.isEmpty())
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(keepAliveQueue.poll());

                        while (!confirmTransactionQueue.isEmpty()) {
                            C0FPacketConfirmTransaction poll = confirmTransactionQueue.poll();
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(poll);
                        }
                    }
                }
            }
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processConfirmTransactionPacket(EventPacketSend e) {
        C0FPacketConfirmTransaction packet = ((C0FPacketConfirmTransaction) e.getPacket());
        int windowId = packet.getWindowId();
        int uid = packet.getUid();

        if (windowId != 0 || uid >= 0) {
            Helper.sendMessage("Inventory synchronized!");
        } else {
            if (uid == --lastUid) {
                if (!hasDisabled) {
                    Helper.sendMessage("Watchdog disabled.");
                    hasDisabled = true;
                }
                confirmTransactionQueue.offer(packet);
                e.setCancelled(true);
            }
            lastUid = uid;
        }
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processKeepAlivePacket(EventPacketSend e) {
        C00PacketKeepAlive packet = ((C00PacketKeepAlive) e.getPacket());
        if (hasDisabled) {
            keepAliveQueue.offer(packet);
            e.setCancelled(true);
        }
    }
	public static boolean isHypixelLobby() {
		if (!Disabler.lobbyCheck.getValue()) return false;
		String[] strings = new String[] {"CLICK TO PLAY", "点击开始游戏"};
		for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
			if (entity.getName().startsWith("§e§l")) {
				for (String string : strings) {
					if (entity.getName().equals("§e§l" + string)) {
						return true;
					}
				}
			}
		}
		return false;
	}
    @SoterObfuscator.Obfuscation(flags = "+native")
    public void processPlayerPosLooksPacket(EventPacketSend e) {
        if (!hasDisabled && !this.isHypixelLobby()) {
            e.setCancelled(true);
        }
    }
}