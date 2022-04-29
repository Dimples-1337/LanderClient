/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package cn.Arctic.Manager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;

import org.lwjgl.input.Keyboard;

import cn.Arctic.Client;
import cn.Arctic.Event.EventBus;
import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventKey;
import cn.Arctic.Event.events.EventRender2D;
import cn.Arctic.Event.events.EventRender3D;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import cn.Arctic.Module.modules.COMBAT.*;
import cn.Arctic.Module.modules.GUI.*;
import cn.Arctic.Module.modules.MOVE.*;
import cn.Arctic.Module.modules.PLAYER.*;
import cn.Arctic.Module.modules.RENDER.*;
import cn.Arctic.Module.modules.UHC.*;
import cn.Arctic.Module.modules.WORLD.*;
import cn.Arctic.Util.Chat.Helper;
import cn.Arctic.Util.render.gl.GLUtils;
import cn.Arctic.values.Mode;
import cn.Arctic.values.Numbers;
import cn.Arctic.values.Option;
import cn.Arctic.values.Value;

public class ModuleManager implements Manager {
	public static List<Module> modules = new ArrayList<Module>();
	private boolean enabledNeededMod = true;
	public boolean nicetry = true;
	public static boolean loaded;

	public boolean isEnabled(Class<? extends Module> cls) {
		return getModuleByClass(cls).isEnabled();
	}

	@Override
	public void init() {
		modules.add(new InfiniteAura());
		modules.add(new AntiVanish());
		modules.add(new Blink());
		modules.add(new ACR());
		modules.add(new Step());
		modules.add(new SafeWalk());
		modules.add(new Jesus());
		modules.add(new AutoPlay());
		// COMBAT
		modules.add(new Aura());
		modules.add(new AntiAim());
		modules.add(new AutoClicker());
		modules.add(new Reach());
		modules.add(new HitBox());
		modules.add(new FastPlace());
		modules.add(new Phase());
		modules.add(new AutoHead());
		modules.add(new AntiBot());
		modules.add(new Criticals());
		modules.add(new AutoPotion());
		modules.add(new TargetStrafe());
		// MOVE
		modules.add(new Flight());
		modules.add(new Sprint());
		modules.add(new Speed());
		modules.add(new NoSlowDown());
		modules.add(new Longjump());
		modules.add(new InvMove());
		modules.add(new Scaffold());
		modules.add(new ClickTeleport());
		// PLAYER
		modules.add(new NoRotate());
		modules.add(new HackerFinder());
		modules.add(new Regen());
		modules.add(new AutoMine());
		
		modules.add(new ChestStealer());
		modules.add(new InvCleaner());
		modules.add(new Velocity());
		modules.add(new NoFall());
		modules.add(new Freecam());
		modules.add(new SpeedMine());
		modules.add(new Teams());
		modules.add(new Timer());
		// RENDER
		modules.add(new ItemPhysics());
		modules.add(new ItemEsp());
		modules.add(new NoHurtCam());
		modules.add(new MotionBlur());
		modules.add(new Cape());
		modules.add(new HUD());
		modules.add(new ChinaHat());
		modules.add(new Skeltal());
		modules.add(new Crosshair());
		modules.add(new Health());
		modules.add(new DMGParticle());
		modules.add(new Nametags());
		modules.add(new Tracers());
		modules.add(new StatusHUD());
		modules.add(new ESP());
		modules.add(new FullBright());
		modules.add(new Chams());
		modules.add(new ChestESP());
		modules.add(new Xray());
		
		modules.add(new cn.Arctic.Module.modules.RENDER.ClickGUI());

		// WORLD
		modules.add(new AutoArmor());
		modules.add(new AntiFall());
		modules.add(new Rejoin());
		modules.add(new Fucker());
		modules.add(new AutoTool());
		modules.add(new LightningChecker());
		modules.add(new WorldTime());
		modules.add(new AntiObsidian());
		modules.add(new Disabler());
		modules.add(new PingSpoof());
		
		//Gui
		modules.add(new customgui());
		// Addon
		this.readSettings();
		for (Module m : modules) {
			m.makeCommand();
		}
		EventBus.getInstance().register(this);
		loaded = true;
	}

	public static List<Module> getModules() {
		return modules;
	}

	public static Module getModuleByClass(Class<? extends Module> cls) {
		for (Module m : modules) {
			if (m.getClass() != cls)
				continue;
			return m;
		}
		return null;
	}

	public static Module getModuleByName(String name) {
		for (Module m : modules) {
			if (!m.getName().equalsIgnoreCase(name))
				continue;
			return m;
		}
		return null;
	}

	public Module getAlias(String name) {
		for (Module f : modules) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f;
			}
			String[] alias = f.getAlias();
			int length = alias.length;
			int i = 0;
			while (i < length) {
				String s = alias[i];
				if (s.equalsIgnoreCase(name)) {
					return f;
				}
				++i;
			}
		}
		return null;
	}

	public static List<Module> getModulesInType(ModuleType t) {
		ArrayList<Module> output = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.getType() != t)
				continue;
			output.add(m);
		}
		return output;
	}

	@EventHandler
	private void onKeyPress(EventKey e) {
//		Helper.sendMessage(e.getKey());
		for (Module m : modules) {
			if (m.getKey() != e.getKey())
				continue;
			m.setEnabled(!m.isEnabled());
		}
	}

	@EventHandler
	private void onGLHack(EventRender3D e) {
		GlStateManager.getFloat(2982, (FloatBuffer) GLUtils.MODELVIEW.clear());
		GlStateManager.getFloat(2983, (FloatBuffer) GLUtils.PROJECTION.clear());
		GlStateManager.glGetInteger(2978, (IntBuffer) GLUtils.VIEWPORT.clear());
	}

	@EventHandler
	private void on2DRender(EventRender2D e) {
		if (this.enabledNeededMod) {
			this.enabledNeededMod = false;
			for (Module m : modules) {
				if (!m.enabledOnStartup)
					continue;
				m.setEnabled(true);
			}
		}
	}

	private void readSettings() {
		Client.instance.logger.info("[Lander] Loading Client Binds...");
        final List<String> binds = FileManager.read("Binds.json");
        for (final String v : binds) {
            final String name = v.split(":")[0];
            final String bind = v.split(":")[1];
            final Module m = getModuleByName(name);
            if (m == null) {
                continue;
            }
            m.setKey(Keyboard.getKeyIndex(bind.toUpperCase()));
            Client.instance.logger.info("[Lander] Set " + m.getName() + " To Key " + bind.toUpperCase() + ".");
        }
        if (getModuleByClass(ClickGUI.class).getKey() == 0) {
            getModuleByClass(ClickGUI.class).setKey(Keyboard.getKeyIndex("RSHIFT"));
        }
        Client.instance.logger.info("[Lander] Binds Load Succeffully.");
        
        Client.instance.logger.info("[Lander] Loading Should Enable Modules...");
        final List<String> enabled = FileManager.read("Enabled.json");
        for (final String v2 : enabled) {
            final Module i = getModuleByName(v2);
            if (i == null) {
                continue;
            }
            i.enabledOnStartup = true;
            Client.instance.logger.info("[Lander] Set " + i.getName() + " Enable on StartUp = true.");
        }
        Client.instance.logger.info("[Lander] Enables Load Succeffully.");
        
        Client.instance.logger.info("[Lander] Loading Module Values...");
        final List<String> vals = FileManager.read("Values.json");
        for (final String v3 : vals) {
            final String name2 = v3.split(":")[0];
            final String values = v3.split(":")[1];
            final Module j = getModuleByName(name2);
            if (j == null) {
                continue;
            }
            for (final Value value : j.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) {
                    continue;
                }
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v3.split(":")[2]));
                    Client.instance.logger.info("[Lander] Set " + name2 + " Value " + value.getName() + " To " + Boolean.parseBoolean(v3.split(":")[2]));
                }
                else if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v3.split(":")[2]));
                    Client.instance.logger.info("[Lander] Set " + name2 + " Value " + value.getName() + " To " + Double.parseDouble(v3.split(":")[2]));
                }
                else {
                    ((Mode)value).setMode(v3.split(":")[2]);
                    Client.instance.logger.info("[Lander] Set " + name2 + " Value " + value.getName() + " To " + v3.split(":")[2]);
                }
            }
        }
        Client.instance.logger.info("[Lander] Values Load Succeffully.");
	}
}
