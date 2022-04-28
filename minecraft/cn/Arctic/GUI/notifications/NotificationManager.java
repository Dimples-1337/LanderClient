package cn.Arctic.GUI.notifications;

import java.util.concurrent.LinkedBlockingQueue;

import cn.Arctic.Client;

public class NotificationManager {
	private static LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
	private static Notification currentNotification = null;

	public static void show(Notification notification) {
		pendingNotifications.add(notification);
	}

	public static void update() {
		if (currentNotification != null && !currentNotification.isShown()) {
			currentNotification = null;
		}

		if (currentNotification == null && !pendingNotifications.isEmpty()) {
			currentNotification = pendingNotifications.poll();
			currentNotification.show();
		}

	}

	public static void render() {
		update();

		if (currentNotification != null)
			Client.drawNotifications();
	}
}
