package cn.Arctic.Module.modules.COMBAT;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventEntityBorderSize;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;

;

public class HitBox extends Module {
    public final Numbers<Double> size = new Numbers<>("Size", 0.3, 0.1, 1.0, 0.1);

    public HitBox() {
        super("HitBox", new String[] {"hitBoxes"}, ModuleType.Combat);
        this.addValues(this.size);
    }

    @EventHandler
    public void onUpdate(EventEntityBorderSize event) {
        event.setBorderSize(this.size.getValue().floatValue());
    }
}
