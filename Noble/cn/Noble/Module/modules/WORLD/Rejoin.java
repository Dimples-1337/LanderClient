
package cn.Noble.Module.modules.WORLD;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventChat;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import cn.Noble.Values.Option;
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


