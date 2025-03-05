import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private Map<String, Clip> soundClips = new HashMap<>();
    private boolean soundEnabled = true;
    
    // Ljudeffekter att ladda
    private final String[] SOUND_FILES = {
        "eat.wav",         // Ätljud när Pacman äter pellets
        "power.wav",       // Ljud när Pacman äter en power pellet
        "ghost.wav",       // Ljud när Pacman äter ett spöke
        "death.wav",       // Ljud när Pacman dör
        "start.wav",       // Startljud
        "win.wav",         // Vinstljud
        "gameover.wav"     // Game over ljud
    };
    
    public SoundManager() {
        loadSounds();
    }
    
    private void loadSounds() {
        for (String fileName : SOUND_FILES) {
            try {
                // Försök att ladda ljudfil från resources-mappen
                URL url = getClass().getResource("/sounds/" + fileName);
                
                // Om ljudfilen inte hittas, fortsätt till nästa utan att krascha
                if (url == null) {
                    System.out.println("Kunde inte hitta ljudfil: " + fileName);
                    continue;
                }
                
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                // Spara clip med filnamn utan filändelsen som nyckel
                String soundName = fileName.substring(0, fileName.lastIndexOf('.'));
                soundClips.put(soundName, clip);
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println("Fel vid laddning av ljudfil: " + fileName);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Spela ett ljud en gång
     * @param soundName Ljudets namn (utan filändelse)
     */
    public void play(String soundName) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();  // Stoppa ljudet om det redan spelas
            }
            clip.setFramePosition(0);  // Återställ till början
            clip.start();  // Starta ljudet
        }
    }
    
    /**
     * Spela ett ljud i loop
     * @param soundName Ljudets namn (utan filändelse)
     */
    public void loop(String soundName) {
        if (!soundEnabled) return;
        
        Clip clip = soundClips.get(soundName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    /**
     * Stoppa ett ljud
     * @param soundName Ljudets namn (utan filändelse)
     */
    public void stop(String soundName) {
        Clip clip = soundClips.get(soundName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    
    /**
     * Stoppa alla ljudeffekter
     */
    public void stopAll() {
        for (Clip clip : soundClips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
    
    /**
     * Aktivera/avaktivera alla ljudeffekter
     * @param enabled true om ljud ska vara aktiverat, false annars
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopAll();
        }
    }
    
    /**
     * Kontrollera om ljud är aktiverat
     * @return true om ljud är aktiverat, false annars
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}