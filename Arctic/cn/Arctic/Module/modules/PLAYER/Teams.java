package cn.Arctic.Module.modules.PLAYER;

import cn.Arctic.Client;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class Teams extends Module{
    private static Mode<Enum> mode = new Mode("Mode", (Enum[])TeamMode.values(), (Enum)TeamMode.Basic);
	public Teams() {
		super("Teams", new String[] {"Teams"}, ModuleType.Player);
		addValues(mode);
	}
	public static boolean isOnSameTeam(EntityLivingBase entity) {
		   switch (mode.getModeAsString()){
           case "Basic":{
        if (!Client.instance.getModuleManager().getModuleByClass(Teams.class).isEnabled()) return false;
        if (!mc.player.getDisplayName().getUnformattedText().isEmpty() && mc.player.getDisplayName().getUnformattedText().charAt(0) == '\247') {
            if (mc.player.getDisplayName().getUnformattedText().length() <= 2
                    || entity.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }
            return mc.player.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2));
        }
           }
           case "Color":{
			 EntityPlayer entityPlayer = (EntityPlayer) entity;
                       if (mc.player.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null) {
                    	   ItemStack myHead = mc.player.inventory.armorInventory[3];
                    	   ItemArmor myItemArmor = (ItemArmor) myHead.item;

                    	   ItemStack  entityHead = entityPlayer.inventory.armorInventory[3];
                           ItemArmor entityItemArmor = (ItemArmor) myHead.item;

                           if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)) {
                               return true;
                           }
                       }
           }
		   }
        return false;
	}


    static enum TeamMode {
       Basic,
       Color;
    }
}