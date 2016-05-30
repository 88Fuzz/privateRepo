package com.murder.game.drawing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.NonBodyDrawable;
import com.murder.game.drawing.rendereffects.RenderEffect;
import com.murder.game.drawing.rendereffects.ScreenShake;
import com.murder.game.texture.loader.SinglePixelTextureLoader;

import box2dLight.RayHandler;

public class WorldRenderer
{
    private static final float LINEAR_INTERP = 1f;
    private static final float MAX_SCREEN_MOVEMENT = 40f;
    private static final float MIN_SCREEN_MOVEMENT = 1f;
    private static final int DEFAULT_SIZE = 1920 * 1080;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private PolygonSpriteBatch polyBatch;
    private Rectangle clearColor;
    private Actor targetActor;
    private BitmapFont font;
    private Map<Class, RenderEffect> renderEffects;
    private Sprite screenSprite;

    // Variables used to track a position in the world, instead of an Actor
    private boolean shouldTargetPosition;
    private Vector2 targetPosition;

    public WorldRenderer()
    {
        this.debugRenderer = new Box2DDebugRenderer();
        this.renderEffects = new HashMap<Class, RenderEffect>();

        batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        clearColor = new Rectangle();

        // TODO remove the ARIAL_15 font
        font = new BitmapFont(Gdx.files.internal(FontType.ARIAL_15), false);
        font.setColor(Color.CYAN);

        screenSprite = new Sprite(SinglePixelTextureLoader.getSinglePixelTextureLoader().getAtlasRegion());
        screenSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screenSprite.setColor(Color.CLEAR);
        shouldTargetPosition = false;
        targetPosition = new Vector2();

        init(null);
    }

    public void init(final Actor target)
    {
        renderEffects.clear();
        if(target != null)
        {
            final ScreenShake screenShake = new ScreenShake();
            screenShake.init(target, -5, 5, -5, 5);

            addRenderEffect(screenShake);
        }
        // TODO figure out why the camera is set to -100, -100 position.
        // camera.position.set(Gdx.graphics.getWidth() / 2,
        // Gdx.graphics.getHeight() / 2, 0);
        if(target != null)
            camera.position.set(target.getPosition().x, target.getPosition().y, 0);

        // If the screen is smaller than the default size, zoom out so that the
        // screen can make a bit more sense
        final float cameraZoom = DEFAULT_SIZE / (Gdx.graphics.getWidth() * Gdx.graphics.getHeight());
        if(cameraZoom > 1)
            camera.zoom = 1 + cameraZoom * .1f;

        camera.update();

        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cameraGUI.setToOrtho(false);
        cameraGUI.update();

        this.targetActor = target;
    }

    public void addRenderEffect(final RenderEffect renderEffect)
    {
        renderEffects.put(renderEffect.getClass(), renderEffect);
    }

    public void removeRenderEffect(final RenderEffect renderEffect)
    {
        renderEffects.remove(renderEffect.getClass());
    }

    public void update(final float dt)
    {
        clearScreen();
        adjustCamera();

        for(final Iterator<Entry<Class, RenderEffect>> iterator = renderEffects.entrySet().iterator(); iterator.hasNext();)
        {
            final Entry<Class, RenderEffect> entry = iterator.next();

            entry.getValue().update(this, dt);
            if(entry.getValue().isFinished(this))
            {
                iterator.remove();
            }
        }
    }

    public void setClearColor(final Rectangle clearColor)
    {
        this.clearColor = clearColor;
    }

    private void clearScreen()
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
        drawScreenSprite();
        drawFPSCounter();
        batch.end();
    }

    private void drawScreenSprite()
    {
        screenSprite.draw(batch);
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

    /**
     * Returns true if camera is centered on targetActor in the x-axis.
     * 
     * @return
     */
    public boolean isActorCenteredOnX()
    {
        if(targetActor != null)
        {
            // TODO the may need to be targetActor.getPosition()
            if(camera.position.x == targetActor.getPosition().x)
                return true;
        }

        return false;
    }

    /**
     * Returns true if camera is centered on targetActor in the y-axis.
     * 
     * @return
     */
    public boolean isActorCenteredOnY()
    {
        if(targetActor != null)
        {
            // TODO the may need to be targetActor.getPosition()
            if(camera.position.y == targetActor.getPosition().y)
                return true;
        }

        return false;
    }

    /**
     * Is the same as calling isActorCenteredOnX() && isActorCenteredOnY()
     * 
     * @return
     */
    public boolean isActorCentered()
    {
        return isActorCenteredOnX() && isActorCenteredOnY();
    }

    public void setTargetPosition(final float x, final float y)
    {
        targetPosition.x = x;
        targetPosition.y = y;

        shouldTargetPosition = true;
    }

    private void adjustCamera()
    {
        final float xTarget;
        final float yTarget;

        if(shouldTargetPosition)
        {
            xTarget = targetPosition.x;
            yTarget = targetPosition.y;
        }
        else if(targetActor != null)
        {
            xTarget = targetActor.getPosition().x;
            yTarget = targetActor.getPosition().y;
            // final Vector2 bodyPosition =
            // targetActor.getBodyPosition().scl(DisplayConstants.PIXELS_PER_METER);
            //
            // float xCamera = bodyPosition.x;
            // float yCamera = bodyPosition.y;

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

            // camera.position.x = xCamera;
            // camera.position.y = yCamera;
            // camera.update();
        }
        else
        {
            return;
        }

        final float xCameraMove = getMovementDistance((xTarget - camera.position.x) * LINEAR_INTERP);
        final float yCameraMove = getMovementDistance((yTarget - camera.position.y) * LINEAR_INTERP);
        
        if(xCameraMove == 0)
            camera.position.x = xTarget;
        else
            camera.position.x += xCameraMove;

        if(yCameraMove == 0)
            camera.position.y = yTarget;
        else
            camera.position.y += yCameraMove;

        if(camera.position.x == xTarget && camera.position.y == yTarget)
            shouldTargetPosition = false;

        camera.update();
    }

    private float getMovementDistance(final float movement)
    {
        final float absMovement = Math.abs(movement);
        if(absMovement > MAX_SCREEN_MOVEMENT)
        {
            if(movement < 0)
                return -1 * MAX_SCREEN_MOVEMENT;

            return MAX_SCREEN_MOVEMENT;
        }
        else if(absMovement < MIN_SCREEN_MOVEMENT)
        {
            // If the screen movement is below the threshold, just set the
            // screen to the target position.
            return 0;
        }

        return movement;
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
