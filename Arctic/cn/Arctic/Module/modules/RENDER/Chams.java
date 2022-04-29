
package cn.Arctic.Module.modules.RENDER;



import java.awt.Color;
import org.lwjgl.opengl.GL11;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventPostRenderPlayer;
import cn.Arctic.Event.events.EventPreRenderPlayer;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Option;


public class Chams
extends Module {
    public static Mode<Enum> mode = new Mode("Mode", (Enum[])ChamsMode.values(), (Enum)ChamsMode.Textured);
    
    public static Option<Boolean> rainbow = new Option<Boolean>("Rainbow", true);

    public Chams() {
        super("Chams", new String[]{"seethru", "cham"}, ModuleType.Render);
        this.addValues(this.mode,this.rainbow);
        this.setColor(new Color(159, 190, 192).getRGB());
        removed = true;
        
           
            
    }

    @EventHandler
    private void preRenderPlayer(EventPreRenderPlayer e) {

        GL11.glEnable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)-1100000.0f);


    }

    @EventHandler
    private void postRenderPlayer(EventPostRenderPlayer e) {

        GL11.glDisable((int)32823);
        GL11.glPolygonOffset((float)1.0f, (float)1100000.0f);


    }

    public static enum ChamsMode {
        Textured,
        Normal;
    }

}

