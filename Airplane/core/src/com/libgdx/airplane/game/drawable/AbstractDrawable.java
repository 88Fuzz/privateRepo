package com.libgdx.airplane.game.drawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.drawable.weapons.Hittable;
import com.libgdx.airplane.game.utils.MapDetails;

/**
 * 
 * Interface for any object that is drawn on screen.
 * 
 */
public abstract class AbstractDrawable implements Hittable
{
    protected Vector2 position;
    protected Sprite sprite;
    protected boolean alive;
    protected MapDetails mapDetails;

    protected AbstractDrawable()
    {
        sprite = new Sprite();
    }

    protected void init(final MapDetails mapDetails, final boolean alive, final Vector2 position)
    {
        this.mapDetails = mapDetails;
        this.position = position;
        this.alive = alive;
    }

    /**
     * Method to update any time dependent properties.
     * 
     * @param dt
     *            Time since last time update was called.
     */
    public abstract void update(final float dt);

    /**
     * Method called to draw objects on screen. This should be used to draw
     * things that area in addition to the sprite.
     * 
     * @param batch
     *            SpriteBatch used to render objects on screen.
     */
    protected abstract void drawCurrent(final SpriteBatch batch);

    /**
     * Draws the sprite onto the screen. It also handles world wrapping
     * 
     * @param batch
     */
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);

        int cameraWidth = Gdx.graphics.getWidth();
        int mapWidth = mapDetails.getMapWidth();

        // In order to make a world wrapping, if objects are at the ends of the
        // playable map, draw the objects on both ends
        if(position.x < cameraWidth)
        {
            sprite.setPosition(mapWidth + position.x, position.y);
            sprite.draw(batch);
        }
        // Check the right side of the screen and draw on the left side of the
        // screen
        else if(position.x > (mapWidth - cameraWidth))
        {
            int tmpX = (int) (mapWidth - position.x);
            sprite.setPosition(-1 * tmpX, position.y);
            sprite.draw(batch);

        }

        sprite.setPosition(position.x, position.y);
    }

    /**
     * Get the position of Drawable object
     * 
     * @return
     */
    public Vector2 getPosition()
    {
        return position;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    /**
     * Dimensions of the sprite used for hit detection
     * 
     * @return
     */
    public Vector2 getDimension()
    {
        // TODO things should have a different hitbox than drawing dimensions
        return new Vector2(sprite.getWidth(), sprite.getHeight());
    }
}