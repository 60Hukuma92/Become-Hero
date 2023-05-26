package com.samsungproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samsungproject.game.BecomeHero;

public class StartScreen implements Screen {
    private BecomeHero game;
    private Viewport gameViewport;
    private OrthographicCamera gameCamera;
    private Texture startScreen;

    public StartScreen(BecomeHero game) {
        this.game = game;
        this.gameCamera = new OrthographicCamera();
        this.gameViewport = new FitViewport(BecomeHero.VIRTUAL_WIDTH, BecomeHero.VIRTUAL_HEIGHT, gameCamera);
        this.startScreen = new Texture("StartScreen.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(startScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        if (Gdx.input.isTouched()) game.setScreen(new PlayScreen(game));
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
