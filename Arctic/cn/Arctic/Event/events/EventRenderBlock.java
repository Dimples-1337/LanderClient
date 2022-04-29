/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Event.events;

import cn.Arctic.Event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

public class EventRenderBlock extends Event {
	
	   private Block block = null;
	   private BlockPos pos = null;

	   public EventRenderBlock(Block block, BlockPos pos) {
	      this.setBlock(block);
	      this.setPos(pos);
	   }

	   public Block getBlock() {
	      return this.block;
	   }

	   public void setBlock(Block block) {
	      this.block = block;
	   }

	   public BlockPos getPos() {
	      return this.pos;
	   }

	   public void setPos(BlockPos pos) {
	      this.pos = pos;
	   }
	
}

