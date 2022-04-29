package cn.Noble.Event;

import cn.Noble.Event.Vector.Vector2;

public class Angle extends Vector2<Float> {

	public Angle(Float x, Float y) {
		super(x, y);
	}
	
    public Angle() {
        this(0.0f, 0.0f);
    }

	public Angle setYaw(Float yaw) {
		this.setX(yaw);
		return this;
	}

	public Angle setPitch(Float pitch) {
		this.setY(pitch);
		return this;
	}

	public Float getYaw() {
		return this.getX().floatValue();
	}

	public Float getPitch() {
		return this.getY().floatValue();
	}

	public Angle constrantAngle() {

		this.setYaw(this.getYaw() % 360F);
		this.setPitch(this.getPitch() % 360F);

		while (this.getYaw() <= -180F) {
			this.setYaw(this.getYaw() + 360F);
		}

		while (this.getPitch() <= -180F) {
			this.setPitch(this.getPitch() + 360F);
		}

		while (this.getYaw() > 180F) {
			this.setYaw(this.getYaw() - 360F);
		}

		while (this.getPitch() > 180F) {
			this.setPitch(this.getPitch() - 360F);
		}

		return this;
	}
}