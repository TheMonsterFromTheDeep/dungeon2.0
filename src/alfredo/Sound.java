/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alfredo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Sound {
    
    public static Sound load(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            InputStream audioSrc = Sound.class.getClass().getResourceAsStream(path);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip.open(audioStream);
            return new Sound(clip);        
        } 
        catch (Exception e) { //Catch all exceptions because multicatch *will* fail sometimes :/
            System.err.println("Failed to load audio resource (" + path + "): " + e.getLocalizedMessage());
            return new Sound(null);
        }
    }
    
    Clip clip;
    
    public Sound(Clip c) {
        this.clip = c;
    }

    public void play() {
        if(clip == null) { return; }
        
        clip.setFramePosition(0);
        clip.start();
    }

    public boolean playing() {
        if(clip == null) { return false; }
        
        return clip.isRunning();
    }

    public void stop() {
        if(clip == null) { return; }
        
        clip.stop();
    }
    
    /**
     * loops forever
     */
    public void loop() {
        if(clip == null) { return; }
        
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
}