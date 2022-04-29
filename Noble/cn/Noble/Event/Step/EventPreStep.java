package cn.Noble.Event.Step;

import cn.Noble.Event.Event;

public class EventPreStep extends Event {
	private float height;

	public EventPreStep(float height) {
		this.height = height;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
