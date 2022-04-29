/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.PLAYER;

import java.awt.Color;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.math.MathUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;

public class ChestStealer
extends Module {
    private Numbers<Double> delay = new Numbers<Double>("Delay", 70.0, 0.0, 1000.0, 10.0);
    private Option<Boolean> menucheck = new Option<Boolean>("MenuCheck", true);
    private TimerUtil timer = new TimerUtil();

    public ChestStealer() {
        super("ChestStealer", new String[]{"cheststeal", "chests", "stealer"}, ModuleType.World);
        this.addValues(this.delay, this.menucheck);
        this.setColor(new Color(218, 97, 127).getRGB());
    }

    @EventHandler
    private void onUpdate(EventTick event) {
        this.setSuffix(this.delay.getValue());
        if (mc.player != null && mc.player.openContainer != null) {
            if (Minecraft.getMinecraft().player.openContainer instanceof ContainerChest) {
                int i = 0;
                ContainerChest container = (ContainerChest)Minecraft.getMinecraft().player.openContainer;
                GuiChest guiChest = (GuiChest)ChestStealer.mc.currentScreen;
                ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(i);
                if (StatCollector.translateToLocal("container.chest").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText()) || StatCollector.translateToLocal("container.chestDouble").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText()) && ((Boolean)this.menucheck.getValue()).booleanValue()) {
                    for (i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                        if (container.getLowerChestInventory().getStackInSlot(i) == null || !this.timer.hasReached((Double)this.delay.getValue())) continue;
                        Minecraft.getMinecraft().playerController.windowClick(container.windowId, i, 0, 1, Minecraft.getMinecraft().player);
                        this.timer.reset();
                    }
                    if (this.isEmpty()) {
                        Minecraft.getMinecraft().player.closeScreen();
                    }
                } else {
                    return;
                }
            }
        }
    }

    private boolean isEmpty() {
        if (Minecraft.getMinecraft().player.openContainer != null) {
            if (Minecraft.getMinecraft().player.openContainer instanceof ContainerChest) {
                ContainerChest container = (ContainerChest)Minecraft.getMinecraft().player.openContainer;
                for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                    ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
                    if (itemStack == null || itemStack.getItem() == null) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBad(ItemStack item) {
        ItemStack is = null;
        float lastDamage = -1.0f;
        for (int i = 9; i < 45; ++i) {
            if (!Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is1 = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack();
            if (!(is1.getItem() instanceof ItemSword) || !(item.getItem() instanceof ItemSword) || !(lastDamage < this.getDamage(is1))) continue;
            lastDamage = this.getDamage(is1);
            is = is1;
        }
        if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = this.getDamage(is);
            float itemDamage = this.getDamage(item);
            if (itemDamage > currentDamage) {
                return false;
            }
        }
        return item != null && (item.getItem().getUnlocalizedName().contains("tnt") || item.getItem().getUnlocalizedName().contains("stick") || item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg") || item.getItem().getUnlocalizedName().contains("string") || item.getItem().getUnlocalizedName().contains("flint") || item.getItem().getUnlocalizedName().contains("compass") || item.getItem().getUnlocalizedName().contains("feather") || item.getItem().getUnlocalizedName().contains("bucket") || item.getItem().getUnlocalizedName().contains("snow") || item.getItem().getUnlocalizedName().contains("fish") || item.getItem().getUnlocalizedName().contains("enchant") || item.getItem().getUnlocalizedName().contains("exp") || item.getItem().getUnlocalizedName().contains("shears") || item.getItem().getUnlocalizedName().contains("anvil") || item.getItem().getUnlocalizedName().contains("torch") || item.getItem().getUnlocalizedName().contains("seeds") || item.getItem().getUnlocalizedName().contains("leather") || item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemGlassBottle || item.getItem() instanceof ItemTool || item.getItem().getUnlocalizedName().contains("piston") || item.getItem().getUnlocalizedName().contains("potion") && this.isBadPotion(item));
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (PotionEffect o : potion.getEffects(stack)) {
                    PotionEffect effect = o;
                    if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword)stack.getItem()).getDamageVsEntity();
    }
}

