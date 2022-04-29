package cn.Noble.Module.modules.UHC;

import net.minecraft.client.settings.*;
import org.lwjgl.input.*;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.potion.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;

public class AutoHead extends Module
{
    private boolean eatingApple;
    private int switched;
    public static boolean doingStuff;
    private final TimerUtil timer;
    private final Option<Boolean> eatHeads;
    private final Option<Boolean> eatApples;
    private final Numbers<Double> health;
    private final Numbers<Double> delay;
    
    public AutoHead() {
        super("AutoHead", new String[] { "AutoHead", "EH", "eathead" }, ModuleType.Combat);
        this.switched = -1;
        this.timer = new TimerUtil();
        this.eatHeads = new Option<Boolean>("Eatheads", true);
        this.eatApples = new Option<Boolean>("Eatapples", true);
        this.health = new Numbers<Double>("Health", 10.0, 1.0, 20.0, 1.0);
        this.delay = new Numbers<Double>("Delay", 750.0, 10.0, 2000.0, 25.0);
        this.addValues(this.health, this.delay, this.eatApples, this.eatHeads);
    }
    
    @Override
    public void onEnable() {
        this.eatingApple = (AutoHead.doingStuff = false);
        this.switched = -1;
        this.timer.reset();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AutoHead.doingStuff = false;
        if (this.eatingApple) {
            this.repairItemPress();
            this.repairItemSwitch();
        }
        super.onDisable();
    }
    
    private void repairItemPress() {
        if (AutoHead.mc.gameSettings != null) {
            final KeyBinding keyBindUseItem = AutoHead.mc.gameSettings.keyBindUseItem;
            if (keyBindUseItem != null) {
                keyBindUseItem.pressed = false;
            }
        }
    }
    
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        if (mc.player == null) {
            return;
        }
        final InventoryPlayer inventory = mc.player.inventory;
        if (inventory == null) {
            return;
        }
        AutoHead.doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            final KeyBinding useItem = AutoHead.mc.gameSettings.keyBindUseItem;
            if (!this.timer.hasReached(this.delay.getValue())) {
                this.eatingApple = false;
                this.repairItemPress();
                this.repairItemSwitch();
                return;
            }
            if (mc.player.capabilities.isCreativeMode || mc.player.isPotionActive(Potion.regeneration) || mc.player.getHealth() >= this.health.getValue()) {
                this.timer.reset();
                if (this.eatingApple) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                }
                return;
            }
            for (int i = 0; i < 2; ++i) {
                final boolean doEatHeads = i != 0;
                if (doEatHeads) {
                    if (!this.eatHeads.getValue()) {
                        continue;
                    }
                }
                else if (!this.eatApples.getValue()) {
                    this.eatingApple = false;
                    this.repairItemPress();
                    this.repairItemSwitch();
                    continue;
                }
                int slot;
                if (doEatHeads) {
                    slot = this.getItemFromHotbar(397);
                }
                else {
                    slot = this.getItemFromHotbar(322);
                }
                if (slot != -1) {
                    final int tempSlot = inventory.currentItem;
                    AutoHead.doingStuff = true;
                    if (doEatHeads) {
                        mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                        mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                        mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                        this.timer.reset();
                    }
                    else {
                        inventory.currentItem = slot;
                        useItem.pressed = true;
                        if (!this.eatingApple) {
                            this.eatingApple = true;
                            this.switched = tempSlot;
                        }
                    }
                }
            }
        }
    }
    
    private void repairItemSwitch() {
        ClientPlayerEntity p = AutoHead.mc.player;
        if (p == null) {
            return;
        }
        final InventoryPlayer inventory = p.inventory;
        if (inventory == null) {
            return;
        }
        int switched = this.switched;
        if (switched == -1) {
            return;
        }
        inventory.currentItem = switched;
        switched = -1;
        this.switched = switched;
    }
    
    private int getItemFromHotbar(final int id) {
        for (int i = 0; i < 9; ++i) {
            if (AutoHead.mc.player.inventory.mainInventory[i] != null) {
                final ItemStack is = AutoHead.mc.player.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == id) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    static {
        AutoHead.doingStuff = false;
    }
}
