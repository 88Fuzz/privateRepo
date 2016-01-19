package com.squared.space.game.drawing;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class WorldRenderer
{
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private SpriteBatch batch;

    public WorldRenderer()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        init();
    }

    public void init()
    {
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        camera.update();

        cameraGUI.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        cameraGUI.setToOrtho(false);
        cameraGUI.update();
    }

    public void clearScreen()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    // TODO there should be a camera adjust method that has a target to move with.

    public float getCameraZoom()
    {
        return camera.zoom;
    }

    public Vector2 getCameraPosition()
    {
        Vector2 tmpVector = new Vector2(camera.position.x / camera.zoom - Gdx.graphics.getWidth() / 2, camera.position.y / camera.zoom - Gdx.graphics.getHeight() / 2);
        return tmpVector;
    }
}