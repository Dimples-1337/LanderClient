package cn.Noble.Module.modules.COMBAT;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Module.modules.PLAYER.InvCleaner;
import cn.Noble.Util.Timer.TimerUtil;
import cn.Noble.Util.rotaions.RotationUtils;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class AutoPotion
extends Module {
    private Numbers<Double> health = new Numbers<Double>("Health", 10.0, 0.0, 20.0, 0.5);
    private Numbers<Double> delay = new Numbers<Double>("Delay", 1000.0, 0.0, 5000.0, 250.0);
    private Option<Boolean> combatproof = new Option<Boolean>("CombatSpoof", true);
    private Option<Boolean> speed = new Option<Boolean>("Speed", true);
    private Option<Boolean> resistance = new Option<Boolean>("Resistance", true);
    private Option<Boolean> fireresistance = new Option<Boolean>("FireResistance", true);
    private Option<Boolean> damageboost = new Option<Boolean>("DamageBoost", true);
    private Option<Boolean> frog = new Option<Boolean>("Frog", false);
    static boolean currentlyPotting = false;
    private boolean isUsing;
//    private static Aura aura;
    private int slot;
    private TimerUtil timer = new TimerUtil();

    public AutoPotion() {
        super("AutoPotion", new String[]{"autopot", "autop","autoheal", "autopotion"}, ModuleType.Combat);
        this.addValues(this.health, this.delay, this.combatproof, this.speed, this.resistance, this.fireresistance, this.damageboost, this.frog);
    }
    
    @Override
    public void onEnable() {
    	//Chocola.protect();
//        if (this.aura == null) {
//            this.aura = (Aura) Client.instance.getModuleManager().getModuleByClass(Aura.class);
//        }
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	if(mc.playerController.isInCreativeMode()) return;
    	if(this.combatproof.getValue() && Aura.curTarget != null) return;
    	if(!this.timer.hasReached(this.delay.getValue().longValue())) return;
    		int spoofSlot = this.getBestSpoofSlot();
            if(!mc.player.isPotionActive(Potion.regeneration)) {
               if((double)mc.player.getHealth() < this.health.getValue()) {
             	 if (hasPot(6)) {
	                     this.getBestPot(spoofSlot, 6);
	                     InvCleaner.timer.reset();
	                     return;
             	 }else if (hasPot(10)) {
	                     this.getBestPot(spoofSlot, 10);
	                     InvCleaner.timer.reset();
	                     return;
             	 }
               } 
            }
            if(!mc.player.isPotionActive(Potion.moveSpeed) && hasPot(1) && this.speed.getValue()) {
         	   this.getBestPot(spoofSlot, 1);
         	   InvCleaner.timer.reset();
         	   return;
	           }
            if(!mc.player.isPotionActive(Potion.resistance) && hasPot(11) && this.resistance.getValue()) {
         	   this.getBestPot(spoofSlot, 11);
         	   InvCleaner.timer.reset();
         	   return;
	           }
            if(!mc.player.isPotionActive(Potion.fireResistance) && hasPot(12) && this.fireresistance.getValue()) {
         	   this.getBestPot(spoofSlot, 12);
         	   InvCleaner.timer.reset();
         	   return;
	           }
            if(!mc.player.isPotionActive(Potion.damageBoost) && hasPot(5) && this.damageboost.getValue()) {
         	   this.getBestPot(spoofSlot, 5);
         	   InvCleaner.timer.reset();
         	   return;
	           }
            if(!mc.player.isPotionActive(Potion.jump) && hasPot(8) && this.frog.getValue()) {
         	   this.getBestPot(spoofSlot, 8);
         	   InvCleaner.timer.reset();
         	   return;
	           }
    }

    public void swap(int slot1, int hotbarSlot) {
	      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.player);
	   }

	   float[] getRotations() {
	      double movedPosX = mc.player.posX + mc.player.motionX * 26.0D;
	      double movedPosY = mc.player.boundingBox.minY - 3.6D;
	      double movedPosZ = mc.player.posZ + mc.player.motionZ * 26.0D;
	         float[] var9 = new float[2];
	         var9[0] = mc.player.rotationYaw;
	         var9[1] = 80.0F;
	         return var9;
	   }

	   int getBestSpoofSlot() {
	      int spoofSlot = 6;

	      for(int i = 36; i < 45; ++i) {
	         if(!mc.player.inventoryContainer.getSlot(i).getHasStack()) {
	            spoofSlot = i - 36;
	            break;
	         }
	      }

	      return spoofSlot;
	   }
	   
	   boolean hasPot(int potID) {
		      for(int i = 9; i < 45; ++i) {
		         Minecraft var10000 = mc;
		         if(mc.player.inventoryContainer.getSlot(i).getHasStack() && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
		            var10000 = mc;
		            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
		            if(is.getItem() instanceof ItemPotion) {
		               ItemPotion pot = (ItemPotion)is.getItem();
		               if(pot.getEffects(is).isEmpty()) {
		                  break;
		               }

		               PotionEffect effect = (PotionEffect)pot.getEffects(is).get(0);
		               int potionID = effect.getPotionID();
		               if(potionID == potID && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is)) {
		                  return true;
		               }
		            }
		         }
		      }
			return false;
		   }

	   void getBestPot(int hotbarSlot, int potID) {
	      for(int i = 9; i < 45; ++i) {
	         Minecraft var10000 = mc;
	         if(mc.player.inventoryContainer.getSlot(i).getHasStack() && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
	            var10000 = mc;
	            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
	            if(is.getItem() instanceof ItemPotion) {
	               ItemPotion pot = (ItemPotion)is.getItem();
	               if(pot.getEffects(is).isEmpty()) {
	                  return;
	               }

	               PotionEffect effect = (PotionEffect)pot.getEffects(is).get(0);
	               int potionID = effect.getPotionID();
	               if(potionID == potID && ItemPotion.isSplash(is.getItemDamage()) && this.isBestPot(pot, is)) {
	                  if(36 + hotbarSlot != i) {
	                     this.swap(i, hotbarSlot);
	                  }

	                  this.timer.reset();
	                  boolean canpot = true;
	                  int oldSlot = mc.player.inventory.currentItem;
	                  mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(hotbarSlot));
	                  mc.player.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(getRotations()[0], getRotations()[1], mc.player.onGround));
	                  mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.player.inventory.getCurrentItem()));
	                  mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(oldSlot));
	                  break;
	               }
	            }
	         }
	      }

	   }

	   boolean isBestPot(ItemPotion potion, ItemStack stack) {
	      if(potion.getEffects(stack) != null && potion.getEffects(stack).size() == 1) {
	         PotionEffect effect = (PotionEffect)potion.getEffects(stack).get(0);
	         int potionID = effect.getPotionID();
	         int amplifier = effect.getAmplifier();
	         int duration = effect.getDuration();

	         for(int i = 9; i < 45; ++i) {
	            Minecraft var10000 = mc;
	            if(mc.player.inventoryContainer.getSlot(i).getHasStack()) {
	               var10000 = mc;
	               ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
	               if(is.getItem() instanceof ItemPotion) {
	                  ItemPotion pot = (ItemPotion)is.getItem();
	                  if(pot.getEffects(is) != null) {
	                     for(Object o : pot.getEffects(is)) {
	                        PotionEffect effects = (PotionEffect)o;
	                        int id = effects.getPotionID();
	                        int ampl = effects.getAmplifier();
	                        int dur = effects.getDuration();
	                        if(id == potionID && ItemPotion.isSplash(is.getItemDamage())) {
	                           if(ampl > amplifier) {
	                              return false;
	                           }

	                           if(ampl == amplifier && dur > duration) {
	                              return false;
	                           }
	                        }
	                     }
	                  }
	               }
	            }
	         }

	         return true;
	      } else {
	         return false;
	      }
	   }
}
