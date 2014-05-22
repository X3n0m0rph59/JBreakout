package org.x3n0m0rph59.breakout;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


enum Sounds { 
	WELCOME, 
	BRICK_HIT, 
	SOLID_BRICK_HIT, 
	PADDLE_HIT, 
	WALL_HIT,	
	BALL_LOST,	
	POWERUP_SPAWNED,	
	BRICK_DESTROYED,
	BULLET_FIRED,
	QUIT
}

enum Musics { 
//	INTRO, 
	BACKGROUND, 
//	OUTRO 
}

class SoundSprite {
	private Sound sound;
	
	public SoundSprite(String filename) {
		try {
			sound = new Sound("sounds/" + filename);
		} catch (SlickException e) {		
			e.printStackTrace();
		}
	}
	
	public void play(float pitch, float volume) {
		sound.play(pitch, volume);
	}
	
	public void playAt(float pitch, float volume, float x, float y, float z) {
		sound.playAt(pitch, volume, x, y, z);
	}
	
	public void stop() {
		if (sound.playing())
			sound.stop();
	}
	
	public boolean isPlaying() {
		return sound.playing();
	}
}

class MusicStream {
	private Music music;
	
	public MusicStream(String filename) {
		try {
			music = new Music("music/" + filename, false);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		music.loop();
	}
	
	public void stop() {
		if (music.playing())
			music.stop();
	}
	
	public void fadeOut() {
		if (music.playing())
			music.fade(500, 0, true);
	}
	
	public boolean isPlaying() {
		return music.playing();
	}
	
	public void setPitch(float pitch) {
		float pos = music.getPosition();
		music.loop(pitch, 1.0f);
		music.setPosition(pos);
	}
}

public final class SoundLayer {
	private static final SoundLayer instance = new SoundLayer();
	
	private Map<Sounds, SoundSprite> soundMap = new HashMap<>();
	private Map<Musics, MusicStream> musicMap = new HashMap<>();
	
	
	public SoundLayer() {
		loadSounds();
		loadMusics();
	}
	
	public static SoundLayer getInstance() {
		return instance;
	}

	private void loadSounds() {
		soundMap.put(Sounds.WELCOME, new SoundSprite("welcome.ogg"));
		soundMap.put(Sounds.BRICK_HIT, new SoundSprite("brick_hit.ogg"));
		soundMap.put(Sounds.SOLID_BRICK_HIT, new SoundSprite("solid_brick_hit.ogg"));
		soundMap.put(Sounds.PADDLE_HIT, new SoundSprite("paddle_hit.ogg"));
		soundMap.put(Sounds.WALL_HIT, new SoundSprite("wall_hit.ogg"));
		soundMap.put(Sounds.BALL_LOST, new SoundSprite("ball_lost.ogg"));
		soundMap.put(Sounds.POWERUP_SPAWNED, new SoundSprite("powerup_spawned.ogg"));
		soundMap.put(Sounds.BRICK_DESTROYED, new SoundSprite("brick_destroyed.ogg"));
		soundMap.put(Sounds.BULLET_FIRED, new SoundSprite("bullet_fired.ogg"));
		soundMap.put(Sounds.QUIT, new SoundSprite("quit.ogg"));
	}
	
	private void loadMusics() {
		musicMap.put(Musics.BACKGROUND, new MusicStream("music.ogg"));
	}

	public static void playSound(Sounds sound) {
		playSound(sound, 1.0f, 1.0f);
	}
	
	public static void playSound(Sounds sound, float pitch, float gain) {
		Logger.log("Playing sound: " + sound);
		
		SoundSprite s = SoundLayer.getInstance().soundMap.get(sound);
		if (s != null) 
			s.play(pitch, gain);			
	}
	
	public static void playMusic(Musics music) {
		Logger.log("Playing music: " + music);
		
		MusicStream m = SoundLayer.getInstance().musicMap.get(music);
		if (m != null) 
			m.play();
	}
	
	public void fadeMusic(Musics music) {
		MusicStream m = SoundLayer.getInstance().musicMap.get(music);
		if (m != null) 
			m.fadeOut();
	}
	
	public void stopMusic(Musics music) {
		MusicStream m = SoundLayer.getInstance().musicMap.get(music);
		if (m != null) 
			m.stop();	
	}
	
	public void setMusicPitch(Musics music, float pitch) {
		MusicStream m = SoundLayer.getInstance().musicMap.get(music);
		if (m != null) 
			m.setPitch(pitch);
	}
	
	public void stopAllMusic()
	{
		for (MusicStream ms : musicMap.values()) {
			ms.stop();
		}
	}
	
	public void setCurrentMusicPitch(float pitch) {
		setMusicPitch(Musics.BACKGROUND, pitch);	
	}	
}