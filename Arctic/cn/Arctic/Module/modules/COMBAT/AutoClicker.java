package cn.Arctic.Module.modules.COMBAT;


import java.util.concurrent.ThreadLocalRandom;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Util.Timer.TimeHelper;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoClicker extends Module {
    private final Numbers<Double> randomize = new Numbers<>("Random", 1.2, 0.0, 5.0, 0.1);
    private final Numbers<Double> cps = new Numbers<>("CPS", 9.5, 0.1, 20.0, 0.5);
    private Option<Boolean> block = new Option<Boolean>("AttackBlocking", true);
    private final TimeHelper timer = new TimeHelper();

    public AutoClicker() {
        super("AutoClicker", new String[] {"autoClick", "clicker"}, ModuleType.Combat);
        this.addValues(this.cps, this.randomize, this.block);
    }

    @EventHandler
    public void onUpdate(EventTick event) {
    	super.setSuffix(this.cps.getValue()+" - "+randomize.getValue());
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            if (timer.isDelayComplete((float)(long)(1000.0 / this.cps.getValue() + ThreadLocalRandom.current().nextDouble(0.0, 2.0 * this.randomize.getValue())))) {
                if (mc.player.isBlocking()&&this.block.getValue()) {
                    mc.player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.UP));
                    mc.player.swingItem();
                    mc.clickMouse();
                    mc.player.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.player.getHeldItem()));
                } else if (!mc.player.isBlocking()){
                    mc.player.swingItem();
                    mc.clickMouse();
                }
                timer.reset();
            }
        }
    }
}
