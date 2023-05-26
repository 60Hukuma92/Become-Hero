package com.samsungproject.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.samsungproject.game.screens.PlayScreen;

public class BecomeHero extends Game {
	//Virtual Screen size
	public static final int VIRTUAL_WIDTH = 400;
	public static final int VIRTUAL_HEIGHT = 220;
	public SpriteBatch batch;

	//Box2D Collision Bits
//	public static final short GROUND_BIT = 1;
//	public static final short HERO_BIT = 2;
//	public static final short BRICK_BIT = 4;
//	public static final short COIN_BIT = 8;
//	public static final short DESTROYED_BIT = 16;
//	public static final short OBJECT_BIT = 32;
//	public static final short ENEMY_BIT = 64;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
