/*
 * Decompiled with CFR 0_132.
 */
package cn.Noble.Module.modules.PLAYER;



import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Comparator;
import java.util.HashSet;

import java.util.List;

import java.util.Objects;
import java.util.Optional;

import java.util.Random;
import java.util.Set;

import java.util.stream.Collectors;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventTick;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.COMBAT.Aura;
import cn.Noble.Util.MoveUtils;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
public class InvCleaner
extends Module {
    public static int weaponSlot = 36;
    public static int pickaxeSlot = 37;
    public static int axeSlot = 38;
    public static int shovelSlot = 39;
    public static int gappleSlot = 40;
    private static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    ArrayList<Integer> whitelistedItems = new ArrayList();
    public final static TimerUtil timer = new TimerUtil();
    private Numbers<Double> maxblocks = new Numbers<Double>("MaxBlocks", 512.0, 0.0, 512.0, 1.0);
    private Numbers<Double> delay = new Numbers<Double>("Delay", 200.0, 0.0, 500.0, 50.0);
    private Option<Boolean> openinv = new Option<Boolean>("OpenInv", false);

    public InvCleaner() {
        super("InvManager", new String[]{"inventorycleaner", "invcleaner", "inventorymanager", "invmanager"}, ModuleType.Player);
        this.setColor(Color.BLUE.getRGB());
        this.addValues(maxblocks,delay,openinv);
    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
    	
    	if(Aura.curTarget != null) return;
    	
        if (this.openinv.getValue().booleanValue() && !(this.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiInventory || this.mc.currentScreen instanceof GuiChat) {
            if (this.timer.delay(this.delay.getValue().intValue()) && weaponSlot >= 36) {
                if (!Minecraft.getMinecraft().player.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    this.getBestWeapon(weaponSlot);
                } else if (!this.isBestWeapon(Minecraft.getMinecraft().player.inventoryContainer.getSlot(weaponSlot).getStack())) {
                    this.getBestWeapon(weaponSlot);
                }
            }
            if (this.timer.delay(this.delay.getValue().intValue()) && pickaxeSlot >= 36) {
                this.getBestPickaxe(pickaxeSlot);
            }
            if (this.timer.delay(this.delay.getValue().intValue()) && shovelSlot >= 36) {
                this.getBestShovel(shovelSlot);
            }
            if (this.timer.delay(this.delay.getValue().intValue()) && axeSlot >= 36) {
                this.getBestAxe(axeSlot);
            }
//            if (this.timer.delay(this.delay.getValue().intValue()) && gappleSlot >= 36) {
//                this.getGapple(gappleSlot);
//            }
            if (this.timer.delay(this.delay.getValue().intValue())) {
                int i = 9;
                while (i < 45) {
                    ItemStack is;
                    if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.shouldDrop(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack(), i)) {
                		
                    	this.shiftClick(0);
                		
                    	this.drop(i);
                        this.timer.reset();
                        if (this.delay.getValue() > 0.0) break;
                    }
                    ++i;
                }
            }
        }
    }

    public void shiftClick(int slot) {
    	mc.player.motionX = 0.0F;
    	mc.player.motionZ = 0.0F;
    }

    public void swap(int slot1, int hotbarSlot) {
        mc.playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot1, hotbarSlot, 2, Minecraft.getMinecraft().player);
    }

    public void drop(int slot) {
        mc.playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, slot, 1, 4, Minecraft.getMinecraft().player);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.getDamage(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) > damage && is.getItem() instanceof ItemSword) {
                return false;
            }
            ++i;
        }
        if (stack.getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }

    public void getBestWeapon(int slot) {
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.isBestWeapon(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) && this.getDamage(is) > 0.0f && is.getItem() instanceof ItemSword) {
                this.swap(i, slot - 36);
                this.timer.reset();
                break;
            }
            ++i;
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool)item;
            damage += tool.getDamage();
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getAttackDamage();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (stack.getDisplayName().toLowerCase().contains("k||")) {
            return false;
        }
        if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemAppleGold) {
            return false;
        }
        if (stack.getItem() instanceof ItemBlock && (double)this.getBlockCount() > this.maxblocks.getValue() && !(stack.getItem() instanceof ItemSkull)) {
            return true;
        }
        if (stack.getItem() instanceof ItemBlock && (double)this.getBlockCount() <= this.maxblocks.getValue() || stack.getItem() instanceof ItemSkull) {
            return false;
        }
        if (slot == weaponSlot && this.isBestWeapon(Minecraft.getMinecraft().player.inventoryContainer.getSlot(weaponSlot).getStack()) || slot == pickaxeSlot && this.isBestPickaxe(Minecraft.getMinecraft().player.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0 || slot == axeSlot && this.isBestAxe(Minecraft.getMinecraft().player.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0 || slot == shovelSlot && this.isBestShovel(Minecraft.getMinecraft().player.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            int type = 1;
            while (type < 5) {
                ItemStack is;
                if (!(Minecraft.getMinecraft().player.inventoryContainer.getSlot(4 + type).getHasStack() && isBestArmor(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(4 + type).getStack(), type) || !isBestArmor(stack, type))) {
                    return false;
                }
                ++type;
            }
        }
        if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if (stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston")) {
            return true;
        }
        return false;
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        int i = 5;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && getProtection(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) > prot && is.getUnlocalizedName().contains(strType)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + ((double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return prot;
    }

    public ArrayList<Integer> getWhitelistedItem() {
        return this.whitelistedItems;
    }

    private int getBlockCount() {
        int blockCount = 0;
        int i = 0;
        while (i < 45) {
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !blacklistedBlocks.contains(((ItemBlock)item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
            ++i;
        }
        return blockCount;
    }

    private void getBestPickaxe(int slot) {
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.isBestPickaxe(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) && pickaxeSlot != i && !this.isBestWeapon(is)) {
                if (!Minecraft.getMinecraft().player.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                    this.swap(i, pickaxeSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                } else if (!this.isBestPickaxe(Minecraft.getMinecraft().player.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
                    this.swap(i, pickaxeSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                }
            }
            ++i;
        }
    }
    
    private void getGapple(int slot) {
//    	if(mc.player.inventoryContainer.getSlot(36 + gappleSlot).getHasStack()) return;
    	for(int i = 9; i < 45; ++i) {
	         if(mc.player.inventoryContainer.getSlot(i).getHasStack()) {
	            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
	            Item pot = is.getItem();
	               if(pot == Items.golden_apple) {
	                  if(36 + gappleSlot != i) {
	                     this.swap(i, gappleSlot - 36);
	                     this.timer.reset();
	                  }

	                  
	                  if (this.delay.getValue().intValue() > 0) {
	                        return;
	                    }
	                  break;
	               }
	            }
	         }
	      }
    

    private void getBestShovel(int slot) {
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.isBestShovel(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) && shovelSlot != i && !this.isBestWeapon(is)) {
                if (!Minecraft.getMinecraft().player.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                    this.swap(i, shovelSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                } else if (!this.isBestShovel(Minecraft.getMinecraft().player.inventoryContainer.getSlot(shovelSlot).getStack())) {
                    this.swap(i, shovelSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                }
            }
            ++i;
        }
    }

    private void getBestAxe(int slot) {
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.isBestAxe(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) && axeSlot != i && !this.isBestWeapon(is)) {
                if (!Minecraft.getMinecraft().player.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                    this.swap(i, axeSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                } else if (!this.isBestAxe(Minecraft.getMinecraft().player.inventoryContainer.getSlot(axeSlot).getStack())) {
                    this.swap(i, axeSlot - 36);
                    this.timer.reset();
                    if (this.delay.getValue().intValue() > 0) {
                        return;
                    }
                }
            }
            ++i;
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.getToolEffect(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) > value && is.getItem() instanceof ItemPickaxe) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.getToolEffect(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) > value && is.getItem() instanceof ItemSpade) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        int i = 9;
        while (i < 45) {
            ItemStack is;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && this.getToolEffect(is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()) > value && is.getItem() instanceof ItemAxe && !this.isBestWeapon(stack)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (potion.getEffects(stack) == null) {
                return true;
            }
            for (PotionEffect o : potion.getEffects(stack)) {
                PotionEffect effect = o;
                if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                return true;
            }
        }
        return false;
    }

    boolean invContainsType(int type) {
        int i = 9;
        while (i < 45) {
            ItemStack is;
            Item item;
            if (Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getHasStack() && (item = (is = Minecraft.getMinecraft().player.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor)item;
                if (type == armor.armorType) {
                    return true;
                }
            }
            ++i;
        }
        return false;
    }
}

