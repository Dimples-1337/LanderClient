
package cn.Arctic.Module.modules.WORLD;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventChat;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Option;
import net.minecraft.client.Minecraft;



public class Rejoin
extends Module {
    public Rejoin() {
        super("Reconect", new String[]{"zkkp"}, ModuleType.World);
    }

    @Override
    public void onEnable() {
    	mc.player.sendChatMessage("/lobby");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
            	mc.player.sendChatMessage("/rejoin");
            }
        }, 1000L);
    	this.setEnabled(false);
    }
    
    @Override
    public void onDisable() {
    	//TODO 自动生成的马
    }

}


