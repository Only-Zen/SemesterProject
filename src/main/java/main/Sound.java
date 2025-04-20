package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Handles loading and playing sound effects and music clips for the game.
 * <p>
 * Maintains an array of resource URLs for various audio files, allows
 * loading a specific clip, playing sounds once at a given volume, looping
 * background music, and stopping playback.
 * </p>
 */
public class Sound {
    /**
     * The currently loaded clip (for {@link #play()}, {@link #loop()}, etc.).
     */
    private Clip clip;

    /**
     * An array of URLs pointing to audio resources packaged with the application.
     */
    private final URL[] soundURL = new URL[30];

    /**
     * Initializes the sound system by preloading the URLs of supported audio files.
     * <p>
     * The indices 0–6 are reserved for specific music and effect files;
     * additional indices can be populated as needed.
     * </p>
     */
    public Sound() {
        soundURL[0] = getClass().getResource("/music/451 Hiraeth.wav");
        soundURL[1] = getClass().getResource("/music/01 Once Upon a Time.wav");
        soundURL[2] = getClass().getResource("/entities/enemy/sounds/die.wav");
        soundURL[3] = getClass().getResource("/tower/sounds/shoot.wav");
        soundURL[4] = getClass().getResource("/tower/sounds/tackShoot.wav");
        soundURL[5] = getClass().getResource("/miscSounds/lostHealth.wav");
        soundURL[6] = getClass().getResource("/tower/sounds/charging.wav");
    }

    /**
     * Loads the clip at the specified index into memory and prepares it for playback.
     * <p>
     * Automatically closes the clip when playback completes to free system resources.
     * </p>
     *
     * @param i the index of the sound in {@link #soundURL} to load
     */
    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound at the given index exactly once at the specified volume.
     * <p>
     * Creates a new {@link Clip} instance for each invocation, allowing multiple
     * sounds to overlap. Closes the clip automatically when playback finishes.
     * </p>
     *
     * @param index            the index of the sound in {@link #soundURL} to play
     * @param volumePercentage desired volume, from 1 (quietest) to 100 (loudest)
     */
    public void playSound(int index, int volumePercentage) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
            Clip newClip = AudioSystem.getClip();
            newClip.open(ais);

            if (newClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) newClip.getControl(FloatControl.Type.MASTER_GAIN);

                // Clamp volume percentage to [1, 100]
                volumePercentage = Math.max(1, Math.min(volumePercentage, 100));

                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                float dB;

                if (volumePercentage == 50) {
                    dB = 0f;
                } else if (volumePercentage < 50) {
                    float fraction = (volumePercentage - 1) / 49f;
                    dB = min + fraction * (0 - min);
                } else {
                    float fraction = (volumePercentage - 50) / 50f;
                    dB = fraction * max;
                }

                gainControl.setValue(dB);
            }

            newClip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        newClip.close();
                    }
                }
            });

            newClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts playback of the currently loaded clip once from its current position.
     * <p>
     * {@link #setFile(int)} must have been called beforehand.
     * </p>
     */
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    /**
     * Continuously loops the currently loaded clip from the beginning.
     * <p>
     * Resets the frame position and sets infinite looping.
     * </p>
     */
    public void loop() {
        if (clip != null && clip.isOpen()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops playback of the currently loaded clip and releases its resources.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Adjusts the volume of the currently loaded clip without restarting it.
     * <p>
     * Clamps the percentage to the range [1, 100] and maps it linearly to the
     * clip's supported gain range.
     * </p>
     *
     * @param volumePercentage desired volume, from 1 (quietest) to 100 (loudest)
     */
    public void setVolume(int volumePercentage) {
        if (clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumePercentage = Math.max(1, Math.min(volumePercentage, 100));

            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float dB;

            if (volumePercentage == 50) {
                dB = 0f;
            } else if (volumePercentage < 50) {
                float fraction = (volumePercentage - 1) / 49f;
                dB = min + fraction * (0 - min);
            } else {
                float fraction = (volumePercentage - 50) / 50f;
                dB = fraction * max;
            }

            gainControl.setValue(dB);
        }
    }
}
