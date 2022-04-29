package cn.Noble.Module.modules.WORLD;

import cn.Noble.Client;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoDig extends Module{
	
    public static Numbers<Double> radius = new Numbers<Double>("Radius", 5.0, 1.0, 10.0, 0.5);
    public static Numbers<Double> ID = new Numbers<Double>("BlockID", 26.0, 1.0, 151.0, 1.0);
    public static int x;
    public static int y;
    public static int z;
    
    public AutoDig() {
        super("AutoDig", new String[] { "autobed","fucker","" }, ModuleType.World);
        this.addValues(radius,ID);
    }
    
    @EventHandler
    private void onUpdate(final EventPreUpdate event) {
        for (int x = -this.radius.getValue().intValue(); x < this.radius.getValue().intValue(); ++x) {
            for (int y = this.radius.getValue().intValue(); y > -this.radius.getValue().intValue(); --y) {
                for (int z = -this.radius.getValue().intValue(); z < this.radius.getValue().intValue(); ++z) {
                    this.x = (int)this.mc.player.posX + x;
                    this.y = (int)this.mc.player.posY + y;
                    this.z = (int)this.mc.player.posZ + z;
                    BlockPos pos = new BlockPos(this.x, this.y, this.z);
                    Block block = this.mc.world.getBlockState(pos).getBlock();
                    if (block.getBlockState().getBlock() == Block.getBlockById(ID.getValue().intValue())) {
                        this.mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                        this.mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                        mc.player.swingItem();
                    }
                }
            }
        }
    }
}
