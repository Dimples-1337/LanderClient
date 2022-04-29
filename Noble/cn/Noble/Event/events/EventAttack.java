package cn.Noble.Event.events;

import cn.Noble.Event.Event;
import net.minecraft.entity.*;

public class EventAttack extends Event
{
    private Entity entity;
    private boolean preAttack;
    
    public EventAttack(final Entity targetEntity, final boolean preAttack) {
        super();
        this.entity = targetEntity;
        this.preAttack = preAttack;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public boolean isPreAttack() {
        return this.preAttack;
    }
    
    public boolean isPostAttack() {
        return !this.preAttack;
    }
}
