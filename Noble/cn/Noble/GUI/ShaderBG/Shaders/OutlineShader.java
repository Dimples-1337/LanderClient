package cn.Noble.GUI.ShaderBG.Shaders;

import org.lwjgl.opengl.GL20;

import cn.Noble.GUI.ShaderBG.FramebufferShader;
import cn.Noble.Util.render.RenderUtils;

/**
 * @author Liquidbounce.net
 * @author Yalan
 */

public final class OutlineShader extends FramebufferShader {

    public static final OutlineShader OUTLINE_SHADER = new OutlineShader();

    public OutlineShader() {
        super("outline.frag");
    }

    @Override
    public void setupUniforms() {
        setupUniform("texture");
        setupUniform("texelSize");
        setupUniform("color");
        setupUniform("divider");
        setupUniform("radius");
        setupUniform("maxSample");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform1i(getUniform("texture"), 0);
        GL20.glUniform2f(getUniform("texelSize"), 1F / mc.getMainWindow().getScaledWidth() * (radius * quality), 1F / mc.getMainWindow().getScaledHeight() * (radius * quality));
        GL20.glUniform4f(getUniform("color"), red, green, blue, alpha);
        GL20.glUniform1f(getUniform("radius"), radius);
    }
}
