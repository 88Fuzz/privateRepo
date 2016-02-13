package com.murder.game.drawing;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WorldRenderer
{
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;
    private Rectangle clearColor;
    private Rectangle bounds;
    private Actor target;

    public WorldRenderer()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        clearColor = new Rectangle();

        init(null, new Rectangle());
    }

    public void init(final Actor target, final Rectangle bounds)
    {
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

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
    public <T extends Drawable> void render(final T drawable)
    {
        render(Collections.singletonList(drawable));
    }

    /**
     * Renders a list of drawable objects on the screen.
     */
    public <T extends Drawable> void render(final List<T> drawables)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(final Drawable drawable: drawables)
        {
            drawable.draw(batch);
        }

        batch.end();
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
            final Vector2 targetPosition = target.getPosition();
            final Vector2 middlePosition = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            if(targetPosition.x - middlePosition.x < bounds.x)
            {
                camera.position.x = bounds.x + middlePosition.x;
            }
            else if(targetPosition.x + middlePosition.x > bounds.width)
            {
                camera.position.x = bounds.width - middlePosition.x;
            }
            else
            {
                camera.position.x = targetPosition.x;
            }

            if(targetPosition.y - middlePosition.y < bounds.y)
            {
                camera.position.y = bounds.y + middlePosition.y;
            }
            else if(targetPosition.y + middlePosition.y > bounds.height)
            {
                camera.position.y = bounds.height - middlePosition.y;
            }
            else
            {
                camera.position.y = targetPosition.y;
            }
            camera.update();
        }
    }
}