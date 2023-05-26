package com.samsungproject.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.samsungproject.game.BecomeHero;
import com.samsungproject.game.screens.PlayScreen;
import com.samsungproject.game.screens.scenes.HeadUpDisplay;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
//        setCategoryFilter(BecomeHero.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick", "Collusion");
        //setCategoryFilter(BecomeHero.DESTROYED_BIT);
        //getCell().setTile(null);
//        HeadUpDisplay.addScore(200);
    }
}
