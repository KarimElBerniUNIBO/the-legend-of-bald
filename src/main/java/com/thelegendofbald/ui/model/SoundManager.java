package com.thelegendofbald.ui.model;

import java.util.ArrayList;
import java.util.List;

public class SoundManager {

    private static final List<SoundPlayer> soundPlayers = new ArrayList<>();

    public static void addSoundPlayer(SoundPlayer soundPlayer) {
        soundPlayers.add(soundPlayer);
    }

    public static void closeAll() {
        soundPlayers.forEach(SoundPlayer::close);
        soundPlayers.clear();
    }

}
