package cn.Noble.Util.rotaions;

import net.minecraft.entity.Entity;

public class Rotation {
	private float yaw;
	private float pitch;

	public Rotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Rotation(Entity ent) {
		this.yaw = ent.rotationYaw;
		this.pitch = ent.rotationPitch;
	}

	public void add(float yaw, float pitch) {
		this.yaw += yaw;
		this.pitch += pitch;
	}

	public void remove(float yaw, float pitch) {
		this.yaw -= yaw;
		this.pitch -= pitch;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void fixedSensitivity(Float sensitivity) {
		float f = sensitivity * 0.6F + 0.2F;
		float gcd = f * f * f * 1.2F;

		yaw -= yaw % gcd;
		pitch -= pitch % gcd;
	}
}
