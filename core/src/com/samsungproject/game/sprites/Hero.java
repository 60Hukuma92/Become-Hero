package com.samsungproject.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.samsungproject.game.BecomeHero;
import com.samsungproject.game.screens.PlayScreen;

public class Hero extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2dBody;
    private TextureRegion heroStand;
    private Animation<TextureRegion> heroRun;
    private Animation<TextureRegion> heroJump;
    private float stateTimer;

    private boolean runningRight;

    public Hero(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_hero"));
        //initialize default values
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
            frames.add(new TextureRegion(getTexture(), 1 * 16, 0, 16, 16));
            frames.add(new TextureRegion(getTexture(), 2 * 16, 0, 16, 16));
            frames.add(new TextureRegion(getTexture(), 3 * 16, 0, 16, 16));
            heroRun = new Animation<TextureRegion>(0.1f, frames);
            frames.clear();
            frames.add(new TextureRegion(getTexture(), 5 * 16, 0, 16, 16));
            heroJump = new Animation<TextureRegion>(0.1f, frames);

        //define hero in Box2d
        defineHero();
        heroStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16, 16);
        setRegion(heroStand);
    }

    public void update(float deltaTime) {
        setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));
    }

    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();

        //depending on the state, get corresponding animation keyFrame
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = heroJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = heroRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = heroStand;
                break;
        }

        //if hero is running left and the texture isn't facing left... flip it.
        if ((b2dBody.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }

        //if hero is running right and the texture isn't facing right... flip it.
        else if ((b2dBody.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    public State getState() {
        //Test to Box2D for velocity on the X and Y-Axis
        //if hero is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if (b2dBody.getLinearVelocity().y > 0 || (b2dBody.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis hero is falling
        else if (b2dBody.getLinearVelocity().y < 0) return State.FALLING;
            //if hero is positive or negative in the X axis he is running
        else if (b2dBody.getLinearVelocity().x != 0) return State.RUNNING;
        //if none of these return then he must be standing
        else return State.STANDING;
    }

    private void defineHero() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32, 32 + 240);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7);
//        fixtureDef.filter.categoryBits = BecomeHero.HERO_BIT;
//        fixtureDef.filter.maskBits = BecomeHero.DEFAULT_BIT | BecomeHero.COIN_BIT | BecomeHero.HERO_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2, 6), new Vector2(2, 6));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.createFixture(fixtureDef).setUserData("head");
    }

    public boolean isRunningRight() {
        return runningRight;
    }
}
