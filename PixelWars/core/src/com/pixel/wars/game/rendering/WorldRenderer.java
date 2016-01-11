package com.pixel.wars.game.rendering;

import java.util.Collections;
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
import com.pixel.wars.game.drawing.Drawable;
import com.pixel.wars.game.drawing.Pixel;

public class WorldRenderer implements Disposable
{
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private BitmapFont font;
    private SpriteBatch batch;

    public WorldRenderer()
    {
        font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraGUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        init();
    }

    public void init()
    {
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setColor(Color.BLACK);

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

    public <T extends Drawable> void render(final T drawable)
    {
        render(Collections.singletonList(drawable));
    }

    /**
     * Renders all game information to screen.
     */
    public <T extends Drawable> void render(final List<T> drawables)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Draw stuff
        for(final Drawable drawable: drawables)
        {
            drawable.draw(batch);
        }

        batch.end();
        // renderWorld(pixels);

        // final ShapeRenderer shapeRenderer = new ShapeRenderer();
        // shapeRenderer.setProjectionMatrix(camera.combined);
        //
        // shapeRenderer.begin(ShapeType.Filled);
        // for(final Pixel pixel: pixels)
        // {
        // shapeRenderer.setColor(pixel.getColor());
        // final Rectangle rectangle = pixel.getRectangle();
        // shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width,
        // rectangle.height);
        // }
        // shapeRenderer.end();

        // adjustCamera();
        // renderWorld();
        // renderGUI();
    }

    /**
     * Centers the camera on the target drawable object
     */
    private void adjustCamera()
    {
        // Vector2 targetPosition = target.getPosition();
        // float fixedHeight = Gdx.graphics.getHeight() / 2 + 12;
        // camera.position.x = targetPosition.x + Gdx.graphics.getWidth() / 4;
        // // Don't move the camera under the ground
        // camera.position.y = (targetPosition.y - fixedHeight < 0) ?
        // fixedHeight : targetPosition.y;
        // camera.update();
    }

    /**
     * Draws game objects on the screen
     */
    private void renderWorld(List<Pixel> pixels)
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Draw stuff
        for(final Pixel pixel: pixels)
        {
            pixel.draw(batch);
        }

        batch.end();
    }

    /**
     * Draws any user interface related components to the screen
     */
    public void renderFPSCounter()
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
        Vector2 tmpVector = new Vector2(camera.position.x / camera.zoom - Gdx.graphics.getWidth() / 2, camera.position.y / camera.zoom - Gdx.graphics.getHeight() / 2);
        return tmpVector;
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }
}