package cn.Arctic.Module.modules.COMBAT;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;

public class setColor extends Module
{
    public static Numbers<Double> r;
    public static Numbers<Double> g;
    public static Numbers<Double> b;
    public static Numbers<Double> a;
    
    static {
        setColor.r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
        setColor.g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
        setColor.b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
        setColor.a = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 1.0);
    }
    
    public setColor() {
        super("CustomColor", new String[] { "SetColor" }, ModuleType.Render);
        this.addValues(setColor.r, setColor.g, setColor.b, setColor.a);
    }
    
    @EventHandler
    private void onUpdate(final EventPreUpdate e) {
        this.setEnabled(false);
    }
}
