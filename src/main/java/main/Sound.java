package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
        }catch(Exception e){
            
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
