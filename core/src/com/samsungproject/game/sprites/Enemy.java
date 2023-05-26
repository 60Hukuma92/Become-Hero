package com.samsungproject.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.samsungproject.game.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;

    public Body b2dBody;

    public Enemy(PlayScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
    }
    public abstract void update(float deltaTime);
    protected abstract void defineEnemy();
    public abstract void hitOnHead();
}
