package com.samsungproject.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.samsungproject.game.BecomeHero;
import com.samsungproject.game.scenes.HeadUpDisplay;

public class Brick extends InteractiveTileObject{
    public Brick(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
//        setCategoryFilter(BecomeHero.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collusion");
//        setCategoryFilter(BecomeHero.DESTROYED_BIT);
//        getCell().setTile(null);
        HeadUpDisplay.addScore(200);
    }
}
