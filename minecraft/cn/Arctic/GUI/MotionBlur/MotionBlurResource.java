package cn.Arctic.GUI.MotionBlur;

import java.io.InputStream;
import java.util.Locale;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import cn.Arctic.Module.modules.GUI.MotionBlur;

public class MotionBlurResource
implements IResource {
    public InputStream getInputStream() {
        double amount = 0.7 + (Double)MotionBlur.amount.getValue() / 100.0 * 3.0 - 0.01;
        return IOUtils.toInputStream((String)String.format((Locale)Locale.ENGLISH, (String)"{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", (Object[])new Object[]{amount, amount, amount}));
    }

    public boolean hasMetadata() {
        return false;
    }

    public IMetadataSection getMetadata(String metadata) {
        return null;
    }

    public ResourceLocation getResourceLocation() {
        return null;
    }

    public String getResourcePackName() {
        return null;
    }
}
