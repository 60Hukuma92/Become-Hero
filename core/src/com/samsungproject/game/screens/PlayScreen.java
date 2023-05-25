package com.samsungproject.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samsungproject.game.BecomeHero;
import com.samsungproject.game.Tools.B2WorldCreator;
import com.samsungproject.game.Tools.WorldContactListener;
import com.samsungproject.game.scenes.HeadUpDisplay;
import com.samsungproject.game.sprites.Hero;

public class PlayScreen implements Screen {
    //Reference to our game, used to set screens
    private BecomeHero game;
    private TextureAtlas atlas;

    //basic PlayScreen variables
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private HeadUpDisplay hud;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer renderer;

    //sprites
    private Hero player;

    //scaling
    private float scaleX, scaleY;

    public PlayScreen(BecomeHero game) {
        scaleX = (float) BecomeHero.VIRTUAL_WIDTH / Gdx.graphics.getWidth();
        scaleY = (float) BecomeHero.VIRTUAL_HEIGHT / Gdx.graphics.getHeight();
        atlas = new TextureAtlas("Sprites.pack");
        this.game = game;
        //create cam used to follow mario through cam world
        this.gameCamera = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        this.gameViewport = new FitViewport(BecomeHero.VIRTUAL_WIDTH, BecomeHero.VIRTUAL_HEIGHT, gameCamera);

        //create our game HUD for scores/timers/level info
        this.hud = new HeadUpDisplay(game.batch);

        //Load our map and setup our map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("world_1-1.tmx");
        renderer = new OrthoCachedTiledMapRenderer(map, 1);

        //initially set our gameCamera to be centered correctly at the start of map
        gameCamera.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2 + 240, 0);
        // +240 because the world is actually 480px and half of it is a secret room down the center

        //create our Box2D world, setting no gravity in X, -100 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -110), true);

        //allows for debug lines of our box2d world
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        //create hero in our game world
        player = new Hero(world, this);

        world.setContactListener(new WorldContactListener());

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(float deltaTime) {
        //handle user input
        handleInput(deltaTime);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(deltaTime);

        //update our gameCamera with correct coordinates after changes
        gameCamera.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float deltaTime) {
        //separate our update logic from render
        update(deltaTime);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();
        //gameCamera.position.x = player.b2dBody.getPosition().x;
        //this is in progress
        if (player.b2dBody.getPosition().x >= BecomeHero.VIRTUAL_WIDTH / 2)
            gameCamera.position.x = player.b2dBody.getPosition().x;

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCamera.combined);

        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.draw(new Texture("Controller.png"), gameCamera.position.x - 200, 240);
        game.batch.draw(new Texture("JumpControl.png"), gameCamera.position.x + 150, 240 + 24);
        game.batch.end();
    }

    private void handleInput(float deltaTime) {
        //pc
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2dBody.getLinearVelocity().y == 0) {
            player.b2dBody.setLinearVelocity(0, 1000);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2dBody.getLinearVelocity().x <= 50 && player.b2dBody.getLinearVelocity().y == 0) {
            player.b2dBody.setLinearVelocity(45, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2dBody.getLinearVelocity().x >= -50 && player.b2dBody.getLinearVelocity().y == 0) {
            player.b2dBody.setLinearVelocity(-45, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2dBody.setLinearVelocity(400, 1700);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2dBody.setLinearVelocity(-400, 1700);
        }
        //android
        if (Gdx.input.isTouched()) {
            //96 - controller's picture width and height
            // 48 - half of it
            //40 - one button's width
            // 20 - half of it
            //17 experimental number used instead of 20
            //24 - one button's height
            //going right
            if ((double) Gdx.input.getX() * scaleX >= 96 - 24 && (double) Gdx.input.getX() * scaleX <= 96
                    && (double) Gdx.input.getY() * scaleY >= 155 - 17 && (double) Gdx.input.getY() * scaleY <= 155 + 17
                    && player.b2dBody.getLinearVelocity().y == 0) {
                player.b2dBody.setLinearVelocity(45, 0);
            }
            //going left
            if ((double) Gdx.input.getX() * scaleX <= 24
                    && (double) Gdx.input.getY() * scaleY >= 155 - 17 && (double) Gdx.input.getY() * scaleY <= 155 + 17
                    && player.b2dBody.getLinearVelocity().y == 0) {
                player.b2dBody.setLinearVelocity(-45, 0);
            }
            //jumping
            if (((double) Gdx.input.getX() * scaleX > 24 && (double) Gdx.input.getX() * scaleX < 48 + 20
                    && (double) Gdx.input.getY() * scaleY < 120 + 17 && (double) Gdx.input.getY() * scaleY >= 120
                    || ((double) Gdx.input.getX() * scaleX > 350 && (double) Gdx.input.getY() * scaleY >= 150 - 14 && (double) Gdx.input.getY() * scaleY < 150 + 24))
                    && player.b2dBody.getLinearVelocity().y == 0) {
                player.b2dBody.setLinearVelocity(0, 1000);
            }
            //side jumping
            if (player.isRunningRight() && (double) Gdx.input.getX() * scaleX >= 48 - 12 && (double) Gdx.input.getX() * scaleX <= 48 + 12
                    && (double) Gdx.input.getY() * scaleY >= 155 - 12 && (double) Gdx.input.getY() * scaleY <= 155 + 12
                    && player.b2dBody.getLinearVelocity().y == 0) {
                player.b2dBody.setLinearVelocity(400, 1700);
            } else if (!player.isRunningRight() && (double) Gdx.input.getX() * scaleX >= 48 - 12 && (double) Gdx.input.getX() * scaleX <= 48 + 12
                    && (double) Gdx.input.getY() * scaleY >= 155 - 14 && (double) Gdx.input.getY() * scaleY <= 155 + 12
                    && player.b2dBody.getLinearVelocity().y == 0) {
                player.b2dBody.setLinearVelocity(-400, 1700);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gameViewport.update(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
