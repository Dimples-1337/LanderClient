package cn.Noble.Module.modules.MOVE;


import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventMove;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Event.events.Update.EventPostUpdate;
import cn.Noble.Font.FontLoaders;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.render.RenderUtil;

public class Scaffold extends Module {
    public static List<Block> blacklistedBlocks = Arrays.asList(new Block[]{Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.sand, Blocks.tnt, Blocks.ladder, Blocks.jukebox, Blocks.noteblock, Blocks.furnace, Blocks.crafting_table, Blocks.anvil, Blocks.ender_chest, Blocks.trapped_chest});
    public Scaffold() {
        super("LegitScaffold", new String[] {""},ModuleType.Movement);
    }

    @EventHandler
	public void onRender2D(EventRender2D event) {
		ScaledResolution res = new ScaledResolution(mc);
		int color = new Color(0, 125, 255).getRGB();
		if (getBlockCount() <= 100 && getBlockCount() > 64) {
			color = new Color(255, 255, 0).getRGB();
		} else if (getBlockCount() <= 64) {
			color = new Color(255, 0, 0).getRGB();
		}
		RenderUtil.drawRoundedRect(res.getScaledWidth() / 2 - 70, 2, res.getScaledWidth() / 2 + 70, 25,
				new Color(255, 255, 255, 225).getRGB(), new Color(255, 255, 255, 225).getRGB());
		RenderUtil.circle(res.getScaledWidth() / 2 - 57, 12, 1.0f, color);
		FontLoaders.NMSL16.drawString(String.valueOf(getBlockCount()) + "  Blocks left", res.getScaledWidth() / 2 - 30,
				11, new Color(25, 25, 25).getRGB());
	}
        public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.player.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklistedBlocks.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
    public Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public Block getBlockUnderPlayer(EntityPlayer player) {
        return getBlock(new BlockPos(player.posX, player.posY - 1.0d, player.posZ));
    }

    @EventHandler
    public void onUpdate(EventPostUpdate event) {
        if (getBlockUnderPlayer(mc.player) instanceof BlockAir) {
            if (mc.player.onGround) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
        } else {
            if (mc.player.onGround) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
        }
    }

    @Override
    public void onEnable() {
        mc.player.setSneaking(false);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        super.onDisable();
    }
}
