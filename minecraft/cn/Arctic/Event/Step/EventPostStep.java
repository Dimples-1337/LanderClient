package cn.Arctic.Event.Step;

import cn.Arctic.Event.Event;

public class EventPostStep extends Event {
	private float height;

	public EventPostStep(float height) {
		this.height = height;
	}

	public float getHeight() {
		return this.height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
