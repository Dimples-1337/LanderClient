package cn.Arctic.Module.modules.PLAYER;

import java.awt.Color;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventTick;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Numbers;



public class Timer
extends Module {
	private Numbers<Double> speed = new Numbers<Double>("Speed", 1.0,0.1,10.0,0.1);
	private long time = -1L;
    public Timer() {
        super("GameSpeed", new String[]{"timer"}, ModuleType.Player);
        this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
        this.addValues(speed);
        removed = true;
    }
    
    @Override
	public void onEnable() {
		mc.timer.timerSpeed = speed.getValue().floatValue();
	}
	
	@Override
	public void onDisable() {
        mc.timer.timerSpeed = 1F;
	}

	@EventHandler
	public void onTick(EventTick e) {
		this.setSuffix(" - "+speed.getValue());
		mc.timer.timerSpeed = speed.getValue().floatValue();
	}

	 public void reset() {
	        time = System.currentTimeMillis();
	    }
}
