package model;

public class Settings {
    private String cueType;
    private boolean soundEnabled;

    public Settings() {
        this.cueType = "Classic Wooden Cue";
        this.soundEnabled = true;
    }

    public String getCueType() {
        return cueType;
    }

    public void setCueType(String cueType) {
        this.cueType = cueType;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }
}