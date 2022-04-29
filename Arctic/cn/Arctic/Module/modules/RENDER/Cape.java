/*
 * Decompiled with CFR 0_132.
 */
package cn.Arctic.Module.modules.RENDER;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRenderCape;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import net.minecraft.util.ResourceLocation;



public class Cape
        extends Module {
	//public static Mode<Enum> capemode = new Mode("Mode", "mode", (Enum[])capemode.values(), (Enum)capemode.Lander);
	 public static Mode<Enum> capemode = new Mode("cape", (Enum[]) CapeMode.values(),
				(Enum) CapeMode.Lander);
	private final ResourceLocation Lander = new ResourceLocation("Lander1/Cape/Lander.png");
	private final ResourceLocation ecy = new ResourceLocation("Lander1/Cape/jiaran.png");
	private final ResourceLocation Misaka = new ResourceLocation("Lander1/Cape/Misaka.png");
	private final ResourceLocation Morn = new ResourceLocation("Lander1/Cape/Morn..png");
	private final ResourceLocation Pdx666 = new ResourceLocation("Lander1/Cape/Pdx666.png");
	private final ResourceLocation sb = new ResourceLocation("Lander1/Cape/sb.png");
	private final ResourceLocation yellow = new ResourceLocation("Lander1/Cape/yellow.png");
	
    public Cape() {
        super("Cape", new String[]{"Cape"}, ModuleType.Render);
        addValues(capemode);
        removed = true;
        
           
            
    }

    @EventHandler
    public void onRender(EventRenderCape event) {
        if (this.mc.world != null) {
            if (FriendManager.isFriend(event.getPlayer().getName()) || event.getPlayer() == this.mc.player)
            	 switch (capemode.getModeAsString()){
                 case "Lander":{
                	 event.setLocation(Lander);
                	 }
                 case "ecy":{
                	 event.setLocation(ecy);
                	 }
                 case "Misaka":{
                	 event.setLocation(Misaka);
                	 }
                 case "Morn":{
                	 event.setLocation(Morn);
                	 }
                 case "Pdx666":{
                	 event.setLocation(Pdx666);
                	 }
                 case "sb":{
                	 event.setLocation(sb);
                	 }
                 case "yellow":{
                	 event.setLocation(yellow);
                	 }
                 }
            event.setCancelled(true);
        }
    }
    public static enum CapeMode {
		Lander,
    	ecy,
    	Misaka,
    	Morn,
    	Pdx666,
    	sb,
    	yellow;
    	
	}
}


