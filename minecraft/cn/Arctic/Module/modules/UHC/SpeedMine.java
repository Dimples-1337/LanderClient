package cn.Arctic.Module.modules.UHC;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventDamageBlock;
import cn.Arctic.Event.events.EventPacketSend;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class SpeedMine extends Module
{

    public static Numbers<Double> speed = new Numbers<Double>("Speed", 0.7, 0.0, 1.0, 0.1);
    public static Numbers<Double> Pot = new Numbers<Double>("Potion", 1.0, 0.0, 4.0, 1.0);
    public static Mode<Enum> mode = new Mode("Mode", AutoPlayMode.values(), AutoPlayMode.Hypixel);
    private boolean bzs = false;
    private float bzx = 0.0f;
    public BlockPos blockPos;
    public EnumFacing facing;
    public static Option<Boolean> SendPacket = new Option<Boolean>("SendPacket", false);
    public SpeedMine() {
        super("SpeedMine", new String[] { "SpeedMine", "FastBreak" }, ModuleType.World);
        this.setColor(new Color(223, 233, 233).getRGB());
        super.addValues(speed,mode,Pot,SendPacket);
    }
	public Block getBlock(double x,double y,double z){
		BlockPos bp = new BlockPos(x, y, z);
		return mc.world.getBlockState(bp).getBlock();
	}
    @EventHandler
    private void onUpdate(EventDamageBlock e) {
    	if(SendPacket.getValue())mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, e.getpos(), e.getfac()));
    	if(Pot.getValue().intValue()!=0) {
    		mc.player.addPotionEffect(new PotionEffect(Potion.digSpeed.getId(),100,Pot.getValue().intValue()-1));
    	}else
    	{
    		mc.player.removePotionEffect(Potion.digSpeed.getId());
    	}
    	if(this.mode.getValue() == AutoPlayMode.Packet) {
    		mc.playerController.blockHitDelay = 0;
            if (mc.playerController.curBlockDamageMP >= SpeedMine.speed.getValue()) {
            	mc.playerController.curBlockDamageMP = 1.0f;
            }

    	}
    	if(this.mode.getValue() == AutoPlayMode.NewPacket2) {
 		   if(mc.playerController.curBlockDamageMP == 0.2f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.4f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.6f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   if(mc.playerController.curBlockDamageMP == 0.8f) {
 			  mc.playerController.curBlockDamageMP += 0.1f;
 		   }
 		   
 	  }   
	   if(this.mode.getValue() == AutoPlayMode.NewPacket) {
    		   if(mc.playerController.curBlockDamageMP == 0.1f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }
    		   if(mc.playerController.curBlockDamageMP == 0.4f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }
    		   if(mc.playerController.curBlockDamageMP == 0.7f) {
    			   mc.playerController.curBlockDamageMP += 0.1f;
    		   }
	      }
     }


    @EventHandler
    public void onDamageBlock(EventPacketSend event) {
    	if(this.mode.getValue() == AutoPlayMode.Hypixel) {
        if (event.packet instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
            C07PacketPlayerDigging c07PacketPlayerDigging = (C07PacketPlayerDigging)event.packet;
            if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                this.bzs = true;
                this.blockPos = c07PacketPlayerDigging.getPosition();
                this.facing = c07PacketPlayerDigging.getFacing();
                this.bzx = 0.0f;
            } else if (c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || c07PacketPlayerDigging.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.bzs = false;
                this.blockPos = null;
                this.facing = null;
            }
        }
    	}
    }
    
    @EventHandler
    public void onUpdate(EventPreUpdate event) {
    	this.setSuffix(mode.getValue());
    	if(this.mode.getValue() == AutoPlayMode.Hypixel) {
        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;
        } else if (this.bzs) {
            Block block = this.mc.world.getBlockState(this.blockPos).getBlock();
            this.bzx += (float)((double)block.getPlayerRelativeBlockHardness(mc.player, this.mc.world, this.blockPos) * 1.4);
            if (this.bzx >= 1.0f) {
                this.mc.world.setBlockState(this.blockPos, Blocks.air.getDefaultState(), 11);
                mc.player.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.facing));
                this.bzx = 0.0f;
                this.bzs = false;
            }
        }
    	}
    }
    @Override
    public void onDisable() {
    	mc.playerController.curBlockDamageMP = 0.0f;
        super.onDisable();
    }
    public static enum AutoPlayMode {
    	Packet,NewPacket,NewPacket2,Hypixel,Speed;
    }
}
