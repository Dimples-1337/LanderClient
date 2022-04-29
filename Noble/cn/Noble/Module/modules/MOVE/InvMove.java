package cn.Noble.Module.modules.MOVE;

import org.lwjgl.input.Keyboard;

import cn.Noble.Event.Listener.EventHandler;
import cn.Noble.Event.events.Update.EventPreUpdate;
import cn.Noble.Module.Module;
import cn.Noble.Module.ModuleType;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

import java.util.Objects;

public class InvMove extends Module{

	public InvMove() {
		super("InvMove", new String[] {}, ModuleType.Movement);
	}
	 @Override
	    public void onEnable() {
	    	// TODO Auto-generated method stub
	    	super.onEnable();
	    }
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		if (Objects.nonNull(mc.currentScreen) && !(mc.currentScreen instanceof GuiChat))
			for (KeyBinding keyBinding : new KeyBinding[]{mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump})
				keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
	}
}
