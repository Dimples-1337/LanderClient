package cn.Noble.GUI.ShaderBG.Shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;

import cn.Noble.GUI.ShaderBG.Shader;

/**
 * @author Liquidbounce.net
 * @author Yalan
 */

public final class BackgroundShader extends Shader {

    public static int deltaTime;
    public final static BackgroundShader BACKGROUND_SHADER = new BackgroundShader();

    private float time;

    public BackgroundShader() {
        super("background.frag");
    }

    @Override
    public void setupUniforms() {
        setupUniform("iResolution");
        setupUniform("iTime");
    }

    @Override
    public void updateUniforms() {
        final int resolutionID = getUniform("iResolution");
        if(resolutionID > -1)
            GL20.glUniform2f(resolutionID, (float) Display.getWidth(), (float) Display.getHeight());
        final int timeID = getUniform("iTime");
        if(timeID > -1) GL20.glUniform1f(timeID, time);

        /* 更新频率 */
        time += 0.01F;
    }
}
