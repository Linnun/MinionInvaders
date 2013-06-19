package net.danielfigge.minioninvaders.util;

import java.io.IOException;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class AudioManager {

	private static final AudioManager single = new AudioManager();

	public static AudioManager get() {
		return single;
	}

	private AudioManager() {
	}

	public Audio initMusic(String file) {
		Audio audio = null;
		try {
			audio = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("resources/audio/music/" + file + ".ogg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return audio;
	}

	public Audio initSound(String file) {
		Audio audio = null;
		try {
			audio = SoundStore.get().getOgg("resources/audio/sounds/" + file + ".ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return audio;
	}

	public void playMusic(Audio audio) {
		audio.playAsMusic(1.0f, 1.0f, true);
	}

	public void playSound(Audio audio) {
		audio.playAsSoundEffect(1.0f, 1.0f, false);
	}

	public void poll() {
		SoundStore.get().poll(0);
	}

	public void setMusicVolume(float volume) {
		SoundStore.get().setMusicVolume(volume);
	}

	public void setSoundVolume(float volume) {
		SoundStore.get().setSoundVolume(volume);
	}

	public float getMusicVolume() {
		return SoundStore.get().getMusicVolume();
	}

	public float getSoundVolume() {
		return SoundStore.get().getSoundVolume();
	}

	public void cleanUp() {
		try {
			destroyOpenAL();
		} catch (SlickException e) {
			e.printStackTrace();
		} catch (UnsatisfiedLinkError e) {
		}
	}

	private void destroyOpenAL(Audio... buffers) throws SlickException, UnsatisfiedLinkError {
		int max = SoundStore.get().getSourceCount();
		IntBuffer buf = BufferUtils.createIntBuffer(max);
		for (int i = 0; i < max; i++) {
			int source = SoundStore.get().getSource(i);
			buf.put(source);
			detachBuffer(source);
		}
		buf.flip();
		AL10.alDeleteSources(buf);
		int exc = AL10.alGetError();
		if (exc != AL10.AL_NO_ERROR) {
			throw new SlickException("Could not clear SoundStore sources, err: " + exc);
		}

		if (buffers != null) {
			for (Audio a : buffers) {
				clearBuffer(a);
			}
		}
		AL.destroy();
		SoundStore.get().clear();
	}

	private void clearBuffer(Audio audio) throws SlickException {
		if (audio == null || audio.getBufferID() == 0)
			return;
		IntBuffer buf = BufferUtils.createIntBuffer(1).put(audio.getBufferID());
		buf.flip();
		AL10.alDeleteBuffers(buf);
		int exc = AL10.alGetError();
		if (exc != AL10.AL_NO_ERROR) {
			throw new SlickException("Could not clear buffer " + audio.getBufferID() + ", err: " + exc);
		}
	}

	private void detachBuffer(int sourceID) {
		AL10.alSourceStop(sourceID);
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, 0);
	}
}