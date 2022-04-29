package i.gishreloaded.gishcode;

import i.gishreloaded.gishcode.managers.FileManager;
import i.gishreloaded.gishcode.managers.HackManager;
import i.gishreloaded.gishcode.managers.LoginManager;
import i.gishreloaded.gishcode.utils.LoginUtils;
import i.gishreloaded.gishcode.utils.system.Nan0EventRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, acceptableRemoteVersions = "*")
public class Main {
	
	public static final String MODID = "hache";
	public static final String NAME = "HACHE_C";
	public static final String VERSION = "0.4.9";
	public static final String MCVERSION = "1.12.2";
	public static int initCount = 0;
	public static HackManager hackManager;
	public static FileManager fileManager;
	public static EventsHandler eventsHandler;

	public Main() { init(null); }
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent E) {}

	@Mod.EventHandler
	public void init(FMLInitializationEvent E) {
		if(initCount > 0) { return; } 
		hackManager = new HackManager();
		fileManager = new FileManager();
		eventsHandler = new EventsHandler();
		Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsHandler);
		Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsHandler);
		initCount++;
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent E) {}
}
