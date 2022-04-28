package cn.Arctic.Util.rotaions;

import net.minecraft.util.*;

public class VecRotation
{
    Vec3 vec;
    Rotation rotation;
    
    public VecRotation(final Vec3 vec, final Rotation rotation) {
        super();
        this.vec = vec;
        this.rotation = rotation;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
}
