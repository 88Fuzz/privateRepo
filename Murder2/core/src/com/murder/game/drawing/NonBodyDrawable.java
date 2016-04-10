package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murder.game.serialize.MyVector2;

public abstract class NonBodyDrawable
{
    private static final float SPIN = 45;
    @JsonIgnore
    protected Sprite sprite;
    protected MyVector2 position;
    protected float rotation;

    public NonBodyDrawable(final MyVector2 position, final float rotation)
    {
        this.position = position;
        this.rotation = rotation;
    }

    public MyVector2 getPosition()
    {
        return position;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void rotate(final float direction)
    {
        setRotation(rotation + direction * SPIN);
    }

    public void setRotation(final float rotation)
    {
        this.rotation = rotation * MathUtils.radiansToDegrees;
    }

    /**
     * Method called when the object should be drawn on the screen.
     * 
     * @param batch
     */
    public abstract void draw(final SpriteBatch batch);

    /**
     * Method called with the time since the last call to update.
     * 
     * @param dt
     */
    protected abstract void updateCurrent(final float dt);

    public void update(final float dt)
    {
        updateCurrent(dt);
    }
}