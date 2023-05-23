package com.samsungproject.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.samsungproject.game.BecomeHero;
import com.samsungproject.game.screens.PlayScreen;

public class Hero extends Sprite {
    public World world;
    public Body b2dBody;
    private TextureRegion heroStand;

    public Hero(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_hero"));
//        initialize default values
        this.world = world;
        //define hero in Box2d
        defineHero();
        heroStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16, 16);
        setRegion(heroStand);
    }

    private void defineHero() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32, 32 + 240);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6);

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);
    }
}
