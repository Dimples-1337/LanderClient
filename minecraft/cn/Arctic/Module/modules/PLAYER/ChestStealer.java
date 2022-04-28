
package cn.Arctic.Module.modules.PLAYER;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimerUtil;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

public class ChestStealer extends Module {
	private Numbers<Double> delay = new Numbers<Double>("Delay", 50.0, 0.0, 1000.0, 10.0);
	private Option<Boolean> menucheck = new Option<Boolean>("MenuCheck", true);
	public static Option<Boolean> slient = new Option<Boolean>("Slient", true);
	private TimerUtil timer = new TimerUtil();
	public int width;

	public ChestStealer() {
		super("ChestStealer", new String[] { "cheststeal", "chests", "stealer" }, ModuleType.World);
		this.addValues(this.delay,this.menucheck,slient);
		this.setColor(new Color(218, 97, 127).getRGB());
		
	}
	

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (this.mc.player.openContainer != null && this.mc.player.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) this.mc.player.openContainer;
			int i = 0;
			//SuIcFuNcE.outSystem(container.getLowerChestInventory().getDisplayName().getUnformattedText());
			if (menucheck.getValue()) {
			//	Helper.sendMessage(container.getLowerChestInventory().getName());
				if (!(StatCollector.translateToLocal("container.chest").equalsIgnoreCase(container.getLowerChestInventory().getDisplayName().getUnformattedText()))) {
					return;
				}
			}
					while (i < container.getLowerChestInventory().getSizeInventory()) {
						if (container.getLowerChestInventory().getStackInSlot(i) != null
								&& this.timer.hasReached(this.delay.getValue())) {
							this.mc.playerController.windowClick(container.windowId, i, 0, 1, this.mc.player);
							this.timer.reset();
						}
						++i;
					}

					if (this.isEmpty()) {
						this.mc.player.closeScreen();
					}
		}
	}

	private boolean isEmpty() {
		if (this.mc.player.openContainer != null && this.mc.player.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) this.mc.player.openContainer;
			int i = 0;
			while (i < container.getLowerChestInventory().getSizeInventory()) {
				ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i);
				if (itemStack != null && itemStack.getItem() != null) {
					return false;
				}
				++i;
			}
		}
		return true;
	}


    private boolean isBad(ItemStack item) {
        ItemStack is = null;
        float lastDamage = -1;
        for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is1 = mc.player.inventoryContainer.getSlot(i).getStack();
                if (is1.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
                    if (lastDamage < getDamage(is1)) {
                        lastDamage = getDamage(is1);
                        is = is1;
                    }
                }
            }
        }


        if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = getDamage(is);
            float itemDamage = getDamage(item);
            if (itemDamage > currentDamage) {
                return false;
            }
        }


        return item != null &&
                ((item.getItem().getUnlocalizedName().contains("tnt")) ||
                        (item.getItem().getUnlocalizedName().contains("stick")) ||
                        (item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg")) ||
                        (item.getItem().getUnlocalizedName().contains("string")) ||
                        (item.getItem().getUnlocalizedName().contains("flint")) ||
                        (item.getItem().getUnlocalizedName().contains("compass")) ||
                        (item.getItem().getUnlocalizedName().contains("feather")) ||
                        (item.getItem().getUnlocalizedName().contains("bucket")) ||
                        (item.getItem().getUnlocalizedName().contains("snow")) ||
                        (item.getItem().getUnlocalizedName().contains("fish")) ||
                        (item.getItem().getUnlocalizedName().contains("enchant")) ||
                        (item.getItem().getUnlocalizedName().contains("exp")) ||
                        (item.getItem().getUnlocalizedName().contains("shears")) ||
                        (item.getItem().getUnlocalizedName().contains("anvil")) ||
                        (item.getItem().getUnlocalizedName().contains("torch")) ||
                        (item.getItem().getUnlocalizedName().contains("seeds")) ||
                        (item.getItem().getUnlocalizedName().contains("leather")) ||
                        ((item.getItem() instanceof ItemPickaxe)) ||
                        ((item.getItem() instanceof ItemGlassBottle)) ||
                        ((item.getItem() instanceof ItemTool)) ||
                        (item.getItem().getUnlocalizedName().contains("piston")) ||
                        ((item.getItem().getUnlocalizedName().contains("potion"))
                                && (isBadPotion(item))));
    }
    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect) o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword) stack.getItem()).getDamageVsEntity();
    }
	
    
    
    
}


