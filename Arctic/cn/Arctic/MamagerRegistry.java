package cn.Arctic;

import java.util.Date;

import com.ibm.icu.text.SimpleDateFormat;

import cn.Arctic.Api.CustomUI.HUDManager;
import cn.Arctic.Manager.CommandManager;
import cn.Arctic.Manager.FileManager;
import cn.Arctic.Manager.FriendManager;
import cn.Arctic.Manager.ModuleManager;
import cn.Arctic.Manager.TextureManager;

public class MamagerRegistry {
	public FileManager fileManager;
	public CommandManager commandManager = Client.instance.getCommandManager();
	public ModuleManager moduleManager = Client.instance.getModuleManager();
	public FriendManager friendManager = new FriendManager();
}
