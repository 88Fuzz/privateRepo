package com.libgdx.airplane.game.rendering;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
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
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private BitmapFont font;
    private SpriteBatch batch;
    private Player target;
    // TODO add layers of drawing
    private List<AbstractDrawable> drawables;

    public WorldRenderer(final Player target)
    {
        font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawables = new LinkedList<AbstractDrawable>();

        init(target);
    }

    public void init(final Player target)
    {
        this.target = target;

        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setColor(Color.BLACK);

        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cameraGUI.setToOrtho(false);
        cameraGUI.update();
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
    }

    /**
     * Centers the camera on the target drawable object
     */
    private void adjustCamera()
    {
        Vector2 targetPosition = target.getPosition();
        float fixedHeight = Gdx.graphics.getHeight() / 2 + 12;
        camera.position.x = targetPosition.x + Gdx.graphics.getWidth() / 4;
        // Don't move the camera under the ground
        camera.position.y = (targetPosition.y - fixedHeight < 0) ? fixedHeight : targetPosition.y;
        camera.update();
    }

    /**
     * Draws game objects on the screen
     */
    private void renderWorld()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(AbstractDrawable drawable: drawables)
        {
            // TODO remove buildings in the collision stage if they are killed.
            if(drawable.isAlive())
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

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }
}
