package cn.Arctic.Module.modules.WORLD;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
public class AutoTool 
extends Module{
	public AutoTool() {
		super("AutoTool",new String[]{"fxxu", "atou", "bugu"}, ModuleType.World);
	}
    public Class type() {
        return EventPacketSend.class;
    }
    @Override
    public void onEnable() {
    	// TODO Auto-generated method stub
    	super.onEnable();
    }
public void onDisable() {
	// TODO Auto-generated method stub
	super.onDisable();
}
    @EventHandler
    public void handle(EventPacketSend event) {
        if (!(event.getPacket() instanceof C07PacketPlayerDigging) || event.getType() != 0) {
            return;
        }
        C07PacketPlayerDigging packet = (C07PacketPlayerDigging)event.getPacket();
        if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            this.autotool(packet.getPosition());
        }
    }

    private  void autotool(BlockPos position) {
       Block block = mc.world.getBlockState(position).getBlock();
        int itemIndex = getStrongestItem(block);
        if (itemIndex < 0) {
            return;
        }
        float itemStrength = AutoTool.getStrengthAgainstBlock(block, mc.player.inventory.mainInventory[itemIndex]);
        if (mc.player.getHeldItem() != null && AutoTool.getStrengthAgainstBlock(block, mc.player.getHeldItem()) >= itemStrength) {
            return;
        }
        mc.player.inventory.currentItem = itemIndex;
        mc.player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(itemIndex));
    }

    private  int getStrongestItem(Block block) {
        float strength = Float.NEGATIVE_INFINITY;
        int strongest = -1;
        int i = 0;
        while (i < 8) {
            float itemStrength;
            ItemStack itemStack = mc.player.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() != null && (itemStrength = AutoTool.getStrengthAgainstBlock(block, itemStack)) > strength && itemStrength != 1.0f) {
                strongest = i;
                strength = itemStrength;
            }
            ++i;
        }
        return strongest;
    }

    public static float getStrengthAgainstBlock(Block block, ItemStack item) {
        float strength = item.getStrVsBlock(block);
        if (!EnchantmentHelper.getEnchantments(item).containsKey(Enchantment.efficiency.effectId) || strength == 1.0f) {
            return strength;
        }
        int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, item);
        return strength + (float)(enchantLevel * enchantLevel + 1);
    }
}
