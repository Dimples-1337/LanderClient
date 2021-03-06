package cn.Noble.Module;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import cn.Noble.Client;
import cn.Noble.Commands.Command;
import cn.Noble.Event.EventBus;
import cn.Noble.GUI.notifications.Notification;
import cn.Noble.Manager.FileManager;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Module.modules.WORLD.Blink;
import cn.Noble.Util.Music;
import cn.Noble.Util.Opacity;
import cn.Noble.Util.Chat.Helper;
import cn.Noble.Util.animate.Translate;
import cn.Noble.Util.math.MathUtil;
import cn.Noble.Values.Mode;
import cn.Noble.Values.Numbers;
import cn.Noble.Values.Option;
import cn.Noble.Values.Value;

public class Module {
	public String name;
	private String suffix;
	private String suffix2;
	private int color;
	public boolean enabled;
	public float posX = (enabled ? 1 : 0);
	private String[] alias;

	public boolean enabledOnStartup = false;
	private int key;
	public List<Value> values;
	public ModuleType type;
	private boolean removed;
	public float anim;
	public int clickanim;
	public Opacity opaAni;
	public Translate mouseAni;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static Random random = new Random();

	private String enable = "Enable.wav";
	private String disable = "Disable.wav";
	
	public Module(String name, String[] alias, ModuleType type) {
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.suffix = "";
		this.key = 0;
		this.removed = false;
		this.enabled = false;
		this.values = new ArrayList();
		this.mouseAni = new Translate(0, 0);
		this.opaAni = new Opacity(0);
	}

	public Module(String name, ModuleType type) {
		this.name = name;
		this.alias = new String[] {};
		this.type = type;
		this.suffix = "";
		this.key = 0;
		this.removed = false;
		this.enabled = false;
		this.values = new ArrayList();
		this.mouseAni = new Translate(0, 0);
		this.opaAni = new Opacity(0);
	}

	public String getName() {
		return this.name;
	}

	public String[] getAlias() {
		return this.alias;
	}

	public ModuleType getType() {
		return this.type;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public float getAnim() {
		return anim;
	}

	public void setAnim(float anim) {
		this.anim = anim;
	}

	public boolean wasRemoved() {
		return this.removed;
	}

	public String getNameWithSuffix() {
		return this.getSuffix().isEmpty() ? this.getName() : String.format("%s %s", this.getName(), this.getSuffix());
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(Object obj) {
		String suffix = obj.toString();
		if (suffix.isEmpty()) {
			this.suffix = suffix;
		} else {
			this.suffix = String.format("\u00a77 \u00a7f%s\u00a77", new Object[] {
					EnumChatFormatting.WHITE + "" + EnumChatFormatting.GRAY + suffix + EnumChatFormatting.WHITE + "" });
		}

	}

	public void setdoubleSuffix(Object obj, Object obj2) {
		String suffix = obj.toString();
		String suffix2 = obj2.toString();
		if (suffix.isEmpty() && suffix2.isEmpty()) {
			this.suffix = suffix;
			this.suffix2 = suffix2;
		} else {
			this.suffix = String.format("\u00a77\u00a7f%s\u00a77",
					new Object[] { EnumChatFormatting.GRAY + suffix + " " + EnumChatFormatting.GRAY + suffix2 });
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (enabled) {
			this.onEnable();
				Client.sendClientMessage(this.getName() + (Object) EnumChatFormatting.GREEN + " Enabled",
					Notification.Type.SUCCESS, 800);
			if (ModuleManager.loaded) {
//				mc.player.playSound("random.click", 9.0F, enabled ? 0.7F : 0.6F);
				Music.playSound(this.getClass(), this.enable);
			}
			if (anim == -1) {
				anim = 0;
			}
			EventBus.getInstance().register(new Object[] { this });
		} else {
			this.onDisable();
			EventBus.getInstance().unregister(new Object[] { this });
					Client.sendClientMessage(this.getName() + (Object) EnumChatFormatting.RED + " Disabled",
							Notification.Type.ERROR, 800);
			if (ModuleManager.loaded) {
//				mc.player.playSound("random.click", 9.0F, enabled ? 0.7F : 0.6F);
				Music.playSound(this.getClass(), this.disable);
			}
		}
	}

	public final void toggle() {
		this.setEnabled(!this.enabled);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return this.color;
	}

	protected void addValues(Value... values) {
		Value[] var5 = values;
		int var4 = values.length;

		for (int var3 = 0; var3 < var4; ++var3) {
			Value value = var5[var3];
			this.values.add(value);
		}

	}

	public List<Value> getValues() {
		return this.values;
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
		String content = "";
		Client.instance.getModuleManager();

		Module m;
		for (Iterator var4 = ModuleManager.getModules().iterator(); var4.hasNext(); content = content + String.format(
				"%s:%s%s", new Object[] { m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator() })) {
			m = (Module) var4.next();
		}

		FileManager.save("Binds.json", content, false);
	}

	public void onEnable() {

	}

	public void onDisable() {
	}

	public void makeCommand() {
		if (this.values.size() > 0) {
			String options = "";
			String other = "";
			Iterator var4 = this.values.iterator();

			Value v;
			while (var4.hasNext()) {
				v = (Value) var4.next();
				if (!(v instanceof Mode)) {
					if (options.isEmpty()) {
						options = String.valueOf(options) + v.getName();
					} else {
						options = String.valueOf(options) + String.format(", %s", new Object[] { v.getName() });
					}
				}
			}

			var4 = this.values.iterator();

			while (true) {
				do {
					if (!var4.hasNext()) {
						Client.instance.getCommandManager().add(new Module$1(this, this.name, this.alias, String.format(
								"%s%s",
								new Object[] { options.isEmpty() ? "" : String.format("%s,", new Object[] { options }),
										other.isEmpty() ? "" : String.format("%s", new Object[] { other }) }),
								"Setup this module"));
						return;
					}

					v = (Value) var4.next();
				} while (!(v instanceof Mode));

				Mode mode = (Mode) v;
				Enum[] modes;
				int length = (modes = mode.getModes()).length;

				for (int i = 0; i < length; ++i) {
					Enum e = modes[i];
					if (other.isEmpty()) {
						other = String.valueOf(other) + e.name().toLowerCase();
					} else {
						other = String.valueOf(other) + String.format(", %s", new Object[] { e.name().toLowerCase() });
					}
				}
			}
		}
	}

    public boolean isHidden() {
        return this.removed;
    }

    public final Value<?> getOptionByLabel(String label) {
        for (Value option : this.values) {
            if (!option.getName().equals(label)) continue;
            return option;
        }
        throw new NullPointerException();
    }

    public String getCustomName() {
        return this.name;
    }
}

class Module$1 extends Command {
	private final Module m;
	final Module this$0;

	Module$1(Module var1, String $anonymous0, String[] $anonymous1, String $anonymous2, String $anonymous3) {
		super($anonymous0, $anonymous1, $anonymous2, $anonymous3);
		this.this$0 = var1;
		this.m = var1;
	}

	public String execute(String[] args) {
		Option option;
		if (args.length >= 2) {
			option = null;
			Numbers fuck = null;
			Mode xd = null;
			Iterator var6 = this.m.values.iterator();

			Value v;
			while (var6.hasNext()) {
				v = (Value) var6.next();
				if (v instanceof Option && v.getName().equalsIgnoreCase(args[0])) {
					option = (Option) v;
				}
			}

			if (option != null) {
				option.setValue(Boolean.valueOf(!((Boolean) option.getValue()).booleanValue()));
				Helper.sendMessage(
						String.format("> %s has been set to %s", new Object[] { option.getName(), option.getValue() }));
			} else {
				var6 = this.m.values.iterator();

				while (var6.hasNext()) {
					v = (Value) var6.next();
					if (v instanceof Numbers && v.getName().equalsIgnoreCase(args[0])) {
						fuck = (Numbers) v;
					}
				}

				if (fuck != null) {
					if (MathUtil.parsable(args[1], (byte) 4)) {
						double v1 = MathUtil.round(Double.parseDouble(args[1]), 1);
						fuck.setValue(Double.valueOf(v1 > ((Double) fuck.getMaximum()).doubleValue()
								? ((Double) fuck.getMaximum()).doubleValue()
								: v1));
						Helper.sendMessage(String.format("> %s has been set to %s",
								new Object[] { fuck.getName(), fuck.getValue() }));
					} else {
						Helper.sendMessage("> " + args[1] + " is not a number");
					}
				}

				var6 = this.m.values.iterator();

				while (var6.hasNext()) {
					v = (Value) var6.next();
					if (args[0].equalsIgnoreCase(v.getName()) && v instanceof Mode) {
						xd = (Mode) v;
					}
				}

				if (xd != null) {
					if (xd.isValid(args[1])) {
						xd.setMode(args[1]);
						Helper.sendMessage(String.format("> %s set to %s", xd.getName(), xd.getModeAsString()));
					} else {
						Helper.sendMessage("> " + args[1] + " is an invalid mode");
					}
				}
			}

			if (fuck == null && option == null && xd == null) {
				this.syntaxError("Valid .<module> <setting> <mode if needed>");
			}
		} else if (args.length >= 1) {
			option = null;
			Iterator xd1 = this.m.values.iterator();

			while (xd1.hasNext()) {
				Value fuck1 = (Value) xd1.next();
				if (fuck1 instanceof Option && fuck1.getName().equalsIgnoreCase(args[0])) {
					option = (Option) fuck1;
				}
			}

			if (option != null) {
				option.setValue(Boolean.valueOf(!((Boolean) option.getValue()).booleanValue()));
				String fuck2 = option.getName().substring(1);
				String xd2 = option.getName().substring(0, 1).toUpperCase();
				if (((Boolean) option.getValue()).booleanValue()) {
					Helper.sendMessage(String.format("> %s has been set to \u00a7a%s",
							new Object[] { xd2 + fuck2, option.getValue() }));
				} else {
					Helper.sendMessage(String.format("> %s has been set to \u00a7c%s",
							new Object[] { xd2 + fuck2, option.getValue() }));
				}
			} else {
				this.syntaxError("Valid .<module> <setting> <mode if needed>");
			}
		} else {
			Helper.sendMessage(String.format("%s Values: \n %s",
					new Object[] {
							this.getName().substring(0, 1).toUpperCase() + this.getName().substring(1).toLowerCase(),
							this.getSyntax(), "false" }));
		}

		return null;
	}
}
