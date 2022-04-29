package cn.Noble.Event.Xray;

import cn.Noble.Event.Event;
import net.minecraft.util.BlockPos;

public class EventSetOpaqueCube extends Event {

    private final BlockPos pos;

    public EventSetOpaqueCube( BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}