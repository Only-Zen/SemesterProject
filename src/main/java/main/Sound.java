package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    
    public Sound(){
        soundURL[0] = getClass().getResource("/music/451 Hiraeth.wav");
        soundURL[1] = getClass().getResource("/music/01 Once Upon a Time.wav");
        soundURL[2] = getClass().getResource("/entities/enemy/sounds/die.wav");
        soundURL[3] = getClass().getResource("/tower/sounds/shoot.wav");
        soundURL[4] = getClass().getResource("/tower/sounds/tackShoot.wav");
        soundURL[5] = getClass().getResource("/miscSounds/lostHealth.wav");
        soundURL[6] = getClass().getResource("/tower/sounds/charging.wav");
        
    }
    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            
            // Add a LineListener to auto-close the clip when done playing
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    // Check if the clip has stopped playing
                    if (event.getType() == LineEvent.Type.STOP) {
                        // Close the clip to release system resources
                        clip.close();
                    }
                }
            });
        }catch(Exception e){
            
        }
    }
    
    public void playSound(int index, int volume) {
        try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
        Clip newClip = AudioSystem.getClip();
        newClip.open(ais);
        
        // Set volume if supported
        if (newClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) newClip.getControl(FloatControl.Type.MASTER_GAIN);
            // Clamp volume percentage to [1, 100]
            if (volume < 1) volume = 1;
            if (volume > 100) volume = 100;
            
            float min = gainControl.getMinimum(); // e.g., around -80 dB
            float max = gainControl.getMaximum(); // e.g., around +6 dB
            float dB;
            
            if (volume == 50) {
                dB = 0f; // default volume
            } else if (volume < 50) {
                // Map [1, 50] to [min, 0]
                float fraction = (volume - 1) / 49.0f;
                dB = min + fraction * (0 - min);
            } else {
                // Map [50, 100] to [0, max]
                float fraction = (volume - 50) / 50.0f;
                dB = 0 + fraction * (max - 0);
            }
            
            gainControl.setValue(dB);
        }
        
        // Add a listener to automatically close the clip when done
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

    
    public void play(){
        clip.start();
    }
    public void loop(){
        if (clip != null && clip.isOpen()){
            // Reset to the beginning of the clip
            clip.setFramePosition(0);
            // Loop continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop(){
        clip.stop();
        clip.close();
    }
    
    public void setVolume(int volumePercentage) {
        if (clip != null && clip.isOpen()) {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                
                // Clamp the percentage between 1 and 100
                if(volumePercentage < 1) volumePercentage = 1;
                if(volumePercentage > 100) volumePercentage = 100;
                
                float min = gainControl.getMinimum(); // e.g., around -80.0 dB
                float max = gainControl.getMaximum(); // e.g., around +6.0 dB
                float dB = 0f;
                
                if (volumePercentage == 50) {
                    // Default volume (no change)
                    dB = 0f;
                } else if (volumePercentage < 50) {
                    // Map volume from [1, 50] to [min, 0]
                    // Calculate the fraction (subtracting 1 to ensure 1 maps exactly to min)
                    float fraction = (volumePercentage - 1) / 49.0f;
                    dB = min + fraction * (0 - min);
                } else {
                    // Map volume from [50, 100] to [0, max]
                    float fraction = (volumePercentage - 50) / 50.0f;
                    dB = 0 + fraction * (max - 0);
                }
                
                gainControl.setValue(dB);
                } else {
                    System.out.println("Volume control not supported for this clip.");
                }
        }
    }
}
