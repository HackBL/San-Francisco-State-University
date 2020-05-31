package TankGame.application.graphics;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound{
    private String resourceLocation;

    public Sound(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public void play() {
        File sound  = new File(resourceLocation);
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


