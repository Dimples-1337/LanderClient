package cn.Arctic.Manager;
import java.io.File; 
import java.io.FileInputStream; 
import java.io.IOException; 
import java.io.InputStream;

import cn.Arctic.Util.Music;
import sun.audio.AudioPlayer;



public class SoundManger { 

private InputStream inputStream = null; 
private static InputStream sb = null; 
public SoundManger(){ 
} 

public void play() throws IOException{ 
inputStream = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/ez4ence.wav");
AudioPlayer.player.start(inputStream); 
} 
public void playtwo() throws IOException{ 
inputStream = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/Disable.wav"); 
AudioPlayer.player.start(inputStream); 
} 
public void playgui() throws IOException{ 
inputStream = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/clickguiopen.wav"); 
AudioPlayer.player.start(inputStream); 
} 
public void notification() throws IOException{ 
inputStream = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/notification.wav"); 
AudioPlayer.player.start(inputStream); 
} 

public String Start() throws IOException{ 
inputStream = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/enter.wav"); 
AudioPlayer.player.start(inputStream);
return null; 
} 

public static void hit() throws IOException{
	sb = Music.class.getResourceAsStream("/assets/minecraft/Melody/Sounds/notification.wav");
}

public static void main(String[] args) { 
try { 
new SoundManger().play(); 
} catch (IOException e) { 
e.printStackTrace(); 
} 

} 

}