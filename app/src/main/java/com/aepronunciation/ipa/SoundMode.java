package com.aepronunciation.ipa;

public enum SoundMode {

    // Do not change the hardcoded string values unless you take into consideration
    // that they are used in persistent memory.
    Single("single"),
    Double("double");

    private final String persistentMemoryString;

    SoundMode(String s) {
        persistentMemoryString = s;
    }

    public String getPersistentMemoryString() {
        return persistentMemoryString;
    }

    public static SoundMode fromString(String text) {
        if (text != null) {
            for (SoundMode mode : SoundMode.values()) {
                if (text.equalsIgnoreCase(mode.persistentMemoryString)) {
                    return mode;
                }
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
