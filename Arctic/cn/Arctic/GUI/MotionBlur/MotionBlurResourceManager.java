package cn.Arctic.GUI.MotionBlur;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class MotionBlurResourceManager
implements IResourceManager {
	
    public Set<String> getResourceDomains() {
        return null;
    }

    public IResource getResource(ResourceLocation location) throws IOException {
        return new MotionBlurResource();
    }

    public List<IResource> getAllResources(ResourceLocation location) throws IOException {
        return null;
    }
}
