package cn.Noble;

import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

import cn.Noble.Api.CustomUI.HUDManager;
import cn.Noble.Manager.CommandManager;
import cn.Noble.Manager.FileManager;
import cn.Noble.Manager.FriendManager;
import cn.Noble.Manager.Manager;
import cn.Noble.Manager.ModuleManager;
import cn.Noble.Manager.TextureManager;

public class MamagerRegistry {
	public FileManager fileManager;
	public CommandManager commandManager = Client.instance.getCommandManager();
	public ModuleManager moduleManager = Client.instance.getModuleManager();
	public FriendManager friendManager = new FriendManager();
}
