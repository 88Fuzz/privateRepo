package com.libgdx.airplane.game.rendering;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.airplane.game.drawable.AbstractDrawable;
import com.libgdx.airplane.game.drawable.airplanes.Player;
import com.libgdx.airplane.game.drawable.weapons.Bomb;
import com.libgdx.airplane.game.drawable.weapons.Missile;

/**
 * 
 * Camera class
 * 
 */
public class WorldRenderer implements Disposable
{
    private static final float SCENE_WIDTH = 800; // 800 meters wide
    private static final float SCENE_HEIGHT = 600; // 600 meters high

    //TODO look into other viewports to use https://github.com/libgdx/libgdx/wiki/Viewports
    private Viewport viewport;
    private Viewport viewportGUI;
    private BitmapFont font;
    private SpriteBatch batch;
    private Player target;
    // TODO add layers of drawing
    private List<AbstractDrawable> drawables;
    private boolean showPhysicsBox;
    private Box2DDebugRenderer debugRenderer;
    private World physicsWorld;

    public WorldRenderer(final World physicsWorld, final Player target, final boolean showPhysicsBox)
    {
        font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
        batch = new SpriteBatch();
        drawables = new LinkedList<AbstractDrawable>();

        init(physicsWorld, target, showPhysicsBox);
    }

    public void init(final World physicsWorld, final Player target, final boolean showPhysicsBox)
    {
        this.physicsWorld = physicsWorld;
        this.target = target;
        this.showPhysicsBox = showPhysicsBox;

        debugRenderer = new Box2DDebugRenderer(true, false, true, true, false, true);

        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setColor(Color.BLACK);

//        final OrthographicCamera camera;
        final OrthographicCamera cameraGUI;

//        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//        camera.update();
//
//        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//        cameraGUI.setToOrtho(false);
//        cameraGUI.update();
        
//        camera = new OrthographicCamera(SCENE_WIDTH, SCENE_HEIGHT);
//        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//
//        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//        camera.update();
//
//        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//        cameraGUI.setToOrtho(false);
//        cameraGUI.update();
        
        viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT);
        // Center camera to get (0,0) as the origin of the Box2D world
//        viewport.getCamera().position.set(viewport.getCamera().position.x + SCENE_WIDTH*0.5f, 
//                viewport.getCamera().position.y + SCENE_HEIGHT*0.5f
//                , 0);
        viewport.getCamera().update();
        viewportGUI = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT);
        viewportGUI.getCamera().update();
    }

    /**
     * Renders all game information to screen.
     */
    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        adjustCamera();
        renderWorld();
        renderGUI();

        if(showPhysicsBox)
        {
            debugRenderer.render(physicsWorld, viewport.getCamera().combined);
        }
    }

    /**
     * Centers the camera on the target drawable object
     */
    private void adjustCamera()
    {
        Vector2 targetPosition = target.getPosition();
        float fixedHeight = Gdx.graphics.getHeight() / 2 + 12;
        viewport.getCamera().position.x = targetPosition.x + Gdx.graphics.getWidth() / 4;
        // Don't move the camera under the ground
        viewport.getCamera().position.y = (targetPosition.y - fixedHeight < 0) ? fixedHeight : targetPosition.y;
        viewport.getCamera().update();
    }

    /**
     * Draws game objects on the screen
     */
    private void renderWorld()
    {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        // Use iterator to safely remove objects in the list
        for(Iterator<AbstractDrawable> it = drawables.iterator(); it.hasNext();)
        {
            AbstractDrawable drawable = it.next();
            // remove drawables from local list if they are killed.
            if(!drawable.isAlive())
            {
                it.remove();
                continue;
            }

            drawable.draw(batch);
        }
        target.draw(batch);
        renderTargetWeapons(batch);

        batch.end();
    }

    private void renderTargetWeapons(final SpriteBatch batch)
    {
        for(Bomb bomb: target.getBombs())
        {
            // TODO remove bombs in the collision stage/airplane if they are
            // killed.
            if(bomb.isAlive())
                bomb.draw(batch);
        }

        for(Missile missile: target.getMissiles())
        {
            // TODO remove bombs in the collision stage/airplane if they are
            // killed.
            if(missile.isAlive())
                missile.draw(batch);
        }
        for(Missile bullet: target.getBullets())
        {
            // TODO remove bombs in the collision stage/airplane if they are
            // killed.
            if(bullet.isAlive())
                bullet.draw(batch);
        }
    }

    /**
     * Draws any user interface related components to the screen
     */
    private void renderGUI()
    {
        batch.setProjectionMatrix(viewportGUI.getCamera().combined);
        batch.begin();
        drawFPSCounter();
        batch.end();
    }

    /**
     * Draws the FPS counter.
     */
    private void drawFPSCounter()
    {
        float x = viewportGUI.getCamera().viewportWidth - 75;
        float y = viewportGUI.getCamera().viewportHeight - 25;
        int fps = Gdx.graphics.getFramesPerSecond();

        font.draw(batch, "FPS: " + fps, x, y);
    }

    /**
     * Adds a single Drawable object to the list of Drawable objects.
     * 
     * @param drawable
     */
    public void addDrawable(AbstractDrawable drawable)
    {
        drawables.add(drawable);
    }

    /**
     * Adds a list of Drawable objects to the list of Drawable objects.
     * 
     * @param drawables
     */
    public void addDrawables(List<AbstractDrawable> drawables)
    {
        this.drawables.addAll(drawables);
    }

    /**
     * Updates the width and height of the window.
     * 
     * @param width
     * @param height
     */
    public void update(final int width, final int height)
    {
        viewport.update(width, height);
        viewportGUI.update(width, height);
    }

    @Override
    public void dispose()
    {
        debugRenderer.dispose();
        // TODO Auto-generated method stub
    }
}
