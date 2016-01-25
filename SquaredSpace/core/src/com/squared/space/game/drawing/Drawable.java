package com.squared.space.game.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Drawable
{
    /**
     * Method called when the object should be drawn on the screen.
     * 
     * @param batch
     */
    public void draw(final SpriteBatch batch);

    /**
     * Method called with the time since the last call to update.
     * 
     * @param dt
     */
    public void update(final float dt);
}