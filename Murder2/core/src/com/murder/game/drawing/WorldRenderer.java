package com.murder.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.constants.drawing.DisplayConstants;
import com.murder.game.constants.drawing.FontType;

import box2dLight.RayHandler;

public class WorldRenderer
{
    private static final int DEFAULT_SIZE = 1920 * 1080;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private PolygonSpriteBatch polyBatch;
    private Rectangle clearColor;
    private Rectangle bounds;
    private Actor target;
    private BitmapFont font;
    private Vector2 middlePosition;

    public WorldRenderer()
    {
        this.debugRenderer = new Box2DDebugRenderer();
        this.middlePosition = new Vector2();

        batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        clearColor = new Rectangle();

        // TODO remove the ARIAL_15 font
        font = new BitmapFont(Gdx.files.internal(FontType.ARIAL_15), false);
        font.setColor(Color.CYAN);

        init(null, new Rectangle());
    }

    public void init(final Actor target, final Rectangle bounds)
    {
        // camera.position.set(Gdx.graphics.getWidth() / 2,
        // Gdx.graphics.getHeight() / 2, 0);
        camera.position.set(-100, -100, 0);
        // If the screen is smaller than the default size, zoom out so that the
        // screen can make a bit more sense
        final float cameraZoom = DEFAULT_SIZE / (Gdx.graphics.getWidth() * Gdx.graphics.getHeight());
        if(cameraZoom > 1)
            camera.zoom = 1 + cameraZoom * .1f;

        camera.update();

        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cameraGUI.setToOrtho(false);
        cameraGUI.update();

        this.target = target;
        this.bounds = bounds;
    }

    public void setClearColor(final Rectangle clearColor)
    {
        this.clearColor = clearColor;
    }

    public void clearScreen()
    {
        Gdx.gl.glClearColor(clearColor.x, clearColor.y, clearColor.width, clearColor.height);
        // Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void render(final World physicsWorld)
    {
        // TODO do something better here and not call cpy everytime;
        debugRenderer.render(physicsWorld, camera.combined.cpy().scl(DisplayConstants.PIXELS_PER_METER));
    }

    public void render(final RayHandler rayHandler)
    {
        // TODO do something better here and not call cpy everytime;
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(DisplayConstants.PIXELS_PER_METER));
        rayHandler.render();
    }

    /**
     * Renders a straight up sprite.
     * 
     * @param sprite
     */
    public void render(final Sprite sprite)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    /**
     * Renders a drawable object on the screen.
     */
    public <T extends NonBodyDrawable> void render(final T drawable)
    {
        // TODO setting the projectionMatrix probably isn't necessary for every
        // loop?
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawable.draw(batch);
        batch.end();
    }

    /**
     * Draws any user interface related components to the screen
     */
    public void renderGUI()
    {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        drawFPSCounter();
        batch.end();
    }

    /**
     * Draws the FPS counter.
     */
    private void drawFPSCounter()
    {
        float x = camera.viewportWidth - 75;
        float y = camera.viewportHeight - 25;
        int fps = Gdx.graphics.getFramesPerSecond();

        font.draw(batch, "FPS: " + fps, x, y);
    }

    public float getCameraZoom()
    {
        return camera.zoom;
    }

    public Vector2 getCameraPosition()
    {
        final Vector2 tmpVector = new Vector2(camera.position.x / camera.zoom - Gdx.graphics.getWidth() / 2,
                camera.position.y / camera.zoom - Gdx.graphics.getHeight() / 2);
        return tmpVector;
    }

    public void adjustCamera()
    {
        if(target != null)
        {
            final Vector2 bodyPosition = target.getBodyPosition().scl(DisplayConstants.PIXELS_PER_METER);
            float xCamera = bodyPosition.x;
            float yCamera = bodyPosition.y;
            // final Vector2 targetPosition = target.getCenterPosition();
            // final Vector2 middlePosition = new
            // Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() /
            // 2);
            // middlePosition.x = Gdx.graphics.getWidth() / 2;
            // middlePosition.y = Gdx.graphics.getHeight() / 2;
            // if(xCamera - middlePosition.x < bounds.x)
            // {
            // xCamera = bounds.x + middlePosition.x;
            // }
            // else if(xCamera + middlePosition.x > bounds.width)
            // {
            // xCamera = bounds.width - middlePosition.x;
            // }
            //
            // if(yCamera - middlePosition.y < bounds.y)
            // {
            // yCamera = bounds.y + middlePosition.y;
            // }
            // else if(yCamera + middlePosition.y > bounds.height)
            // {
            // yCamera = bounds.height - middlePosition.y;
            // }

            camera.position.x = xCamera;
            camera.position.y = yCamera;
            camera.update();
        }
    }
    // private void cameraUpdate()
    // {
    // lockOnTarget(camera,
    // player.getPosition().scl(DisplayConstants.PIXELS_PER_METER));
    // }
    //
    // public static void lockOnTarget(final Camera camera, final Vector2
    // target)
    // {
    // Vector3 position = camera.position;
    // position.x = target.x;
    // position.y = target.y;
    // camera.position.set(position);
    // camera.update();
    // }

    public Vector3 getWorldCoordinates(final float screenX, final float screenY)
    {
        final Vector3 unprojected = new Vector3(screenX, screenY, 0);
        camera.unproject(unprojected);
        return unprojected.scl(1 / DisplayConstants.PIXELS_PER_METER);
    }

    public void dispose()
    {
        batch.dispose();
        polyBatch.dispose();
        font.dispose();
    }
}