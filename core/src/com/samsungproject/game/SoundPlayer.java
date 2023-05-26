package com.samsungproject.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundPlayer {
    public static Music theme = Gdx.audio.newMusic(Gdx.files.internal("music/35 Hunt Or Be Hunted.mp3"));
    public static Music nextTheme = Gdx.audio.newMusic(Gdx.files.internal("music/33 Ladies of the Woods.mp3"));

    public static void play() {
        theme.setVolume(0.2f);
        nextTheme.setVolume(0.2f);
        theme.play();
        theme.setLooping(true);
//        if (!theme.isPlaying()) nextTheme.play();
    }
}
