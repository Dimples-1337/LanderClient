package cn.Noble.Api.CustomUI;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

import cn.Noble.Client;
import cn.Noble.Api.CustomUI.Functions.*;
import cn.Noble.Api.CustomUI.Functions.UI.*;
import cn.Noble.Event.EventBus;
import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.EventRender2D;
import cn.Noble.Manager.FileManager;
import cn.Noble.Manager.Manager;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.modules.GUI.HUD;

public class HUDManager implements Manager {

    public static List<HUDApi> apis = new ArrayList<HUDApi>();

    @Override
    public void init() {

        apis.add(new TargetHUD());
        apis.add(new KeyStrokes());
        apis.add(new HUDArrayList());
        apis.add(new PlayerInfo());
        apis.add(new PacketGraph());
        apis.add(new WayInfo());

        this.readXYE();

        EventBus.getInstance().register(this);
    }

    @EventHandler
    public void onRender(EventRender2D e){
        if(Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled() && !(Minecraft.getMinecraft().currentScreen instanceof HUDScreen)){
            for(HUDApi api : apis){
                if(api.enabled){
                    api.onRender();
                }
            }
        }
    }

    public static List<HUDApi> getApis() {
        return apis;
    }

    public static HUDApi getApiByName(String name) {
        for (HUDApi h : apis) {
            if (!h.getName().equalsIgnoreCase(name)) continue;
            return h;
        }
        return null;
    }

    private void readXYE() {
        List<String> hud = FileManager.read("HUD.cfg");
        for (String v : hud) {

            String name = v.split(":")[0];

            String x1 = v.split(":")[1];   //name:"x:y:e"
            String x = x1.split(":")[0];     //name:"x":y:e

            String y1 = v.split(":")[2];      //name:x:"y:e"
            String y = y1.split(":")[0];      //name:x:"y":e

            String e = v.split(":")[3];    //name:x:y:"e"

            HUDApi m = HUDManager.getApiByName(name);
            if (m == null) continue;
            m.x = Integer.parseInt(x);
            m.y = Integer.parseInt(y);
            m.enabled = Boolean.parseBoolean(e);
        }
    }
}
