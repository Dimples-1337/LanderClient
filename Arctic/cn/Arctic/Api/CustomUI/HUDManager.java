package cn.Arctic.Api.CustomUI;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

import cn.Arctic.Client;
import cn.Arctic.Api.CustomUI.Functions.*;
import cn.Arctic.Api.CustomUI.Functions.UI.*;
import cn.Arctic.Event.EventBus;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Manager.FileManager;
import cn.Arctic.Manager.Manager;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Module.modules.GUI.HUD;

public class HUDManager implements Manager {

    public static List<HUDApi> apis = new ArrayList<HUDApi>();

    @Override
    public void init() {

        apis.add(new TargetHUD());
        apis.add(new KeyStrokes());
        apis.add(new PlayerInfo());
        apis.add(new PacketGraph());
        apis.add(new WayInfo());
        apis.add(new InventoryHUD());
        apis.add(new PlayerList());

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
