package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase
{
    private EntityWolf theWolf;
    private EntityPlayer player;
    private World worldObject;
    private float minPlayerDistance;
    private int timeoutCounter;

    public EntityAIBeg(EntityWolf wolf, float minDistance)
    {
        this.theWolf = wolf;
        this.worldObject = wolf.worldObj;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        this.player = this.worldObject.getClosestPlayerToEntity(this.theWolf, (double)this.minPlayerDistance);
        return this.player == null ? false : this.hasPlayerGotBoneInHand(this.player);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.player.isEntityAlive() ? false : (this.theWolf.getDistanceSqToEntity(this.player) > (double)(this.minPlayerDistance * this.minPlayerDistance) ? false : this.timeoutCounter > 0 && this.hasPlayerGotBoneInHand(this.player));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theWolf.setBegging(true);
        this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theWolf.setBegging(false);
        this.player = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theWolf.getLookHelper().setLookPosition(this.player.posX, this.player.posY + (double)this.player.getEyeHeight(), this.player.posZ, 10.0F, (float)this.theWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }

    /**
     * Gets if the Player has the Bone in the hand.
     */
    private boolean hasPlayerGotBoneInHand(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.getCurrentItem();
        return itemstack == null ? false : (!this.theWolf.isTamed() && itemstack.getItem() == Items.bone ? true : this.theWolf.isBreedingItem(itemstack));
    }
}
