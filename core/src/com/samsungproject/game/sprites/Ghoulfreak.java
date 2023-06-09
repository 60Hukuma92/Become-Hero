package com.samsungproject.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.samsungproject.game.screens.PlayScreen;

public class Ghoulfreak extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private Hero hero;

    public Ghoulfreak(Hero hero, PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("ghoulfreak"), 0 * 16, 0, 16, 16));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("ghoulfreak"), 1 * 16, 0, 16, 16));
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16, 16);
        setToDestroy = false;
        destroyed = false;
        this.hero = hero;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2dBody);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("ghoulfreak"), 2 * 16, 0, 16, 16));
        }
        else if (!destroyed) {
            setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 240);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7);

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);
//        fixtureDef.filter.categoryBits = BecomeHero.ENEMY_BIT;
//        fixtureDef.filter.maskBits = BecomeHero.HERO_BIT | BecomeHero.GROUND_BIT | BecomeHero.COIN_BIT | BecomeHero.HERO_BIT | BecomeHero.ENEMY_BIT | BecomeHero.OBJECT_BIT;
    // creating the head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 9);
        vertice[1] = new Vector2(5, 9);
        vertice[2] = new Vector2(-3, 3);
        vertice[3] = new Vector2(3, 3);
        head.set(vertice);

        fixtureDef.shape = head;

        fixtureDef.restitution = 0.2f;
        b2dBody.createFixture(fixtureDef).setUserData("freak's_head");
    }

    @Override
    public void hitOnHead() {
      //  if (b2dBody.getPosition().y + 16)
        setToDestroy = true;
    }
}
