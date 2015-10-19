package com.libgdx.airplane.game.drawable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.utils.MapDetails;

public abstract class AbstractMoveable extends AbstractDrawable
{
    protected float singleDimensionVelocity;
    protected float pitch;
    protected Vector2 velocity;
    protected float acceleration;
    protected float pitchAcceleration;
    protected float maxAcceleration;
    protected float maxPitchAcceleration;

    public AbstractMoveable()
    {
        super();
    }

    public void init(final MapDetails mapDetails, final boolean alive, final Vector2 position, final Vector2 velocity,
            final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity)
    {
        super.init(mapDetails, alive, position);

        this.singleDimensionVelocity = singleDimensionVelocity;
        this.pitch = pitch;
        this.velocity = velocity;
        this.acceleration = 0;
        this.pitchAcceleration = 0;
        this.maxAcceleration = maxAcceleration;
        this.maxPitchAcceleration = maxPitchAcceleration;
    }

    @Override
    public void update(final float dt)
    {
        checkBounds();
        updatePosition(dt);
    }

    /**
     * Updates the position of the sprite based on pitch and velocity.
     */
    protected void updatePosition(final float dt)
    {
        singleDimensionVelocity += acceleration;
        pitch += pitchAcceleration;

        sprite.rotate(pitchAcceleration);

        velocity.x = (float) (singleDimensionVelocity * Math.cos(Math.toRadians(pitch)));
        velocity.y = (float) (singleDimensionVelocity * Math.sin(Math.toRadians(pitch)));

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        sprite.setPosition(position.x, position.y);

        if(position.y < 0)
        {
            kill();
        }
    }

    /**
     * Check and bounding properties like velocity, acceleration, position, etc.
     */
    protected void checkBounds()
    {
        checkAccelerations();
        checkPositions();
    }

    private void checkAccelerations()
    {
        if(singleDimensionVelocity < -1 * maxAcceleration)
        {
            singleDimensionVelocity = -1 * maxAcceleration;
            setAcceleration(0);
        }
        else if(singleDimensionVelocity > maxAcceleration)
        {
            singleDimensionVelocity = maxAcceleration;
            setAcceleration(0);
        }

        if(pitch < -1 * maxPitchAcceleration)
        {
            pitch = -1 * maxPitchAcceleration;
            setPitchAcceleration(0);
        }
        else if(pitch > maxPitchAcceleration)
        {
            pitch = maxPitchAcceleration;
            setPitchAcceleration(0);
        }
    }

    private void checkPositions()
    {
        if(position.x < 0)
        {
            position.x = (float) mapDetails.getMapWidth();
        }
        else if(position.x > (float) mapDetails.getMapWidth())
        {
            position.x = 0;
        }

        // TODO kill if y < 0
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(SpriteBatch batch)
    {
        if(isAlive())
        {
            super.draw(batch);
        }
    }

    public void addSingleDimensionVelocity(final float velocity)
    {
        singleDimensionVelocity += velocity;
    }

    public void addAcceleration(final float acceleration)
    {
        this.acceleration += acceleration;
    }

    public void setAcceleration(final float acceleration)
    {
        this.acceleration = acceleration;
    }

    public void addPitchAcceleration(final float pitchAcceleration)
    {
        this.pitchAcceleration = pitchAcceleration;
    }

    public void setPitchAcceleration(final float pitchAcceleration)
    {
        this.pitchAcceleration = pitchAcceleration;
    }
}