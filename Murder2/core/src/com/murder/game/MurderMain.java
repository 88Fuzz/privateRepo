package com.murder.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.constants.drawing.DisplayConstants;
import com.murder.game.utils.BodyBuilder;
import com.murder.game.utils.LightBuilder;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class MurderMain extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 30.0f;
    private static final int PLAYER_SIZE = 32;
    private static final int WALL_SIZE = 200;

    private World physicsWorld;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch batch;
    private Body player;
    private Sprite playerSprite;
    private Sprite wallSprite;
    private Body body1;
    private Body body2;
    private Body body3;
    private Body body4;

    private RayHandler rayHandler;
    private ConeLight coneLight;
    private float timeSinceLastUpdate;
    private OrthographicCamera camera;

    @Override
    public void create()
    {
        timeSinceLastUpdate = 0;

        batch = new SpriteBatch();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float SCALE = 1f;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        this.physicsWorld = new World(new Vector2(0, 0), false);
        this.debugRenderer = new Box2DDebugRenderer();

        this.player = BodyBuilder.createBox(physicsWorld, 0, 0, PLAYER_SIZE, PLAYER_SIZE, false, false, (short) 1, (short) 0, (short) 1);
        this.player.setLinearDamping(20f);
        this.player.setAngularDamping(.2f);
        playerSprite = new Sprite(new Texture(Gdx.files.internal("img/SinglePixel.png")), PLAYER_SIZE, PLAYER_SIZE);
        playerSprite.setOrigin(PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        adjustPlayerSpriteAndRotation();

        wallSprite = new Sprite(new Texture(Gdx.files.internal("img/SinglePixel.png")), WALL_SIZE, WALL_SIZE);
        wallSprite.setOrigin(WALL_SIZE / 2, WALL_SIZE / 2);
        wallSprite.setColor(Color.ORANGE);

        body1 = BodyBuilder.createBox(physicsWorld, 400, 0, WALL_SIZE, WALL_SIZE, true, true, (short) 1, (short) 0, (short) 1);
        BodyBuilder.createBox(physicsWorld, 200, 0, WALL_SIZE, WALL_SIZE, true, true, (short) 0, (short) 0, (short) 0);
        body2 = BodyBuilder.createBox(physicsWorld, -200, 0, WALL_SIZE, WALL_SIZE, true, true, (short) 1, (short) 0, (short) 1);
        body3 = BodyBuilder.createBox(physicsWorld, 0, 200, WALL_SIZE, WALL_SIZE, true, true, (short) 1, (short) 0, (short) 1);
        body4 = BodyBuilder.createBox(physicsWorld, 0, -200, WALL_SIZE, WALL_SIZE, true, true, (short) 1, (short) 0, (short) 1);

        rayHandler = new RayHandler(physicsWorld);
//        rayHandler.setAmbientLight(.5f);

        coneLight = LightBuilder.createConeLight(rayHandler, player, Color.WHITE, 30, player.getAngle(), 30);
        coneLight.setColor(Color.WHITE);
        coneLight.setContactFilter((short) 1, (short) 0, (short) 1);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void pause()
    {}

    @Override
    public void resume()
    {}

    @Override
    public void resize(final int width, final int height)
    {}

    @Override
    public void render()
    {
        updateAndRenderStates();
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    @Override
    public boolean keyDown(final int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode)
    {
        if(Input.Keys.ESCAPE == keycode)
            Gdx.app.exit();

        return false;
    }

    @Override
    public boolean keyTyped(final char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(final int amount)
    {
        return false;
    }

    private void updateAndRenderStates()
    {
        float dt = Gdx.graphics.getDeltaTime();

        timeSinceLastUpdate += dt;
        while(timeSinceLastUpdate > TIMEPERFRAME)
        {
            physicsWorld.step(TIMEPERFRAME, 6, 2);
            rayHandler.update();

            inputUpdate(TIMEPERFRAME);
            cameraUpdate();
            adjustPlayerSpriteAndRotation();
            // batch.setProjectionMatrix(camera.combined);

            timeSinceLastUpdate -= TIMEPERFRAME;
        }
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(DisplayConstants.PIXELS_PER_METER));
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        playerSprite.draw(batch);
        drawWall(body1, wallSprite);
        wallSprite.draw(batch);
        drawWall(body2, wallSprite);
        wallSprite.draw(batch);
        drawWall(body3, wallSprite);
        wallSprite.draw(batch);
        drawWall(body4, wallSprite);
        wallSprite.draw(batch);
        batch.end();

        debugRenderer.render(physicsWorld, camera.combined.cpy().scl(DisplayConstants.PIXELS_PER_METER));
        rayHandler.render();
    }

    private void inputUpdate(final float delta)
    {
        float x = 0, y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            y -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            x += 1;
        }

        // Dampening check
        if(x != 0)
        {
            player.setLinearVelocity(x * 350 * delta, player.getLinearVelocity().y);
        }
        if(y != 0)
        {
            player.setLinearVelocity(player.getLinearVelocity().x, y * 350 * delta);
        }
    }

    private void cameraUpdate()
    {
        lockOnTarget(camera, player.getPosition().scl(DisplayConstants.PIXELS_PER_METER));
    }

    public static void lockOnTarget(final Camera camera, final Vector2 target)
    {
        Vector3 position = camera.position;
        position.x = target.x;
        position.y = target.y;
        camera.position.set(position);
        camera.update();
    }

    private void drawWall(final Body body, final Sprite sprite)
    {
        // TODO have a "adjust to pixels" method to do change x and y
        sprite.setPosition(body.getPosition().x * DisplayConstants.PIXELS_PER_METER - WALL_SIZE / 2,
                body.getPosition().y * DisplayConstants.PIXELS_PER_METER - WALL_SIZE / 2);
    }

    private void adjustPlayerSpriteAndRotation()
    {
        playerSprite.setPosition(player.getPosition().x * DisplayConstants.PIXELS_PER_METER - PLAYER_SIZE / 2,
                player.getPosition().y * DisplayConstants.PIXELS_PER_METER - PLAYER_SIZE / 2);
        playerSprite.setRotation(player.getAngle() * MathUtils.radiansToDegrees);
    }
}