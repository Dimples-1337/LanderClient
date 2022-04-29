package cn.Arctic.Event.events;

import net.minecraft.client.multiplayer.WorldClient;

public class WorldEvent {
	    private final WorldClient worldClient;

	    public final WorldClient getWorldClient() {
	        return this.worldClient;
	    }

	    public WorldEvent(WorldClient worldClient) {
	        this.worldClient = worldClient;
	    }

}
