package cn.Arctic.Util;

import java.io.InputStream;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import net.minecraft.client.Minecraft;

public class Music {

	public static synchronized void playSound(Class clz, String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							clz.getResourceAsStream("/assets/minecraft/Melody/Sounds/" + url));
					
					clip.open(inputStream);
					FloatControl gainControl = 
						    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-17.0f); 
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}
