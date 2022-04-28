package cn.Arctic.Module.modules.MOVE;

import org.lwjgl.input.Keyboard;

import cn.Arctic.Event.Listener.EventHandler;
import cn.Arctic.Event.events.EventUpdate;
import cn.Arctic.Event.events.Update.EventPostUpdate;
import cn.Arctic.Event.events.Update.EventPreUpdate;
import cn.Arctic.Module.Module;
import cn.Arctic.Module.ModuleType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ContainerChest;

import java.util.Objects;

public class InvMove extends Module{
    private boolean isWalking;
	public InvMove() {
		super("InvMove", new String[] {}, ModuleType.Movement);
	}
	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
			KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
			KeyBinding[] array;
			for (int length = (array = key).length, i = 0; i < length; ++i) {
				KeyBinding b = array[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
		}
	}
}
