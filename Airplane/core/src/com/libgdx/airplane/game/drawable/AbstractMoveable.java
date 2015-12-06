package com.libgdx.airplane.game.drawable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.libgdx.airplane.game.utils.MapDetails;

public abstract class AbstractMoveable extends AbstractDrawable
{
    protected float pitch;
    protected Vector2 velocity;
    protected float acceleration;
    protected float pitchAcceleration;
    protected float maxSingleDimensionVelocity;
    protected float maxPitchAcceleration;

    public AbstractMoveable()
    {
        super();
    }

    public void init(final Body physicsBody, final Vector2 bodySize, final MapDetails mapDetails, final boolean alive, final Vector2 velocity,
            final float maxSingleDimensionVelocity, final float maxPitchAcceleration, final float pitch,
            final float initialVelocity)
    {
        super.init(physicsBody, bodySize, mapDetails, alive);

        this.pitch = pitch;
        this.velocity = velocity;
        this.acceleration = 0;
        this.pitchAcceleration = 0;
        this.maxSingleDimensionVelocity = maxSingleDimensionVelocity;
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
        Vector2 linearVelocity = physicsBody.getLinearVelocity();
        float length = linearVelocity.len();
            System.out.println("velocity " + physicsBody.getLinearVelocity());

        physicsBody.setLinearVelocity(0, 0);
            physicsBody.applyAngularImpulse(pitchAcceleration, true);
            float angle = physicsBody.getAngle();
            final Vector2 force = new Vector2();

//            force.x = (float) (force1d* Math.cos(angle));
//            force.y = (float) (force1d* Math.sin(angle));
            force.x = (float) (acceleration* Math.cos(angle));
            force.y = (float) (acceleration* Math.sin(angle));
            System.out.println("FORCE " + force);
            
//            TODO remove the oldFore vector, it is not needed
            Vector2 oldForce = new Vector2();
            oldForce.x = (float) (length * Math.cos(angle));
            oldForce.y = (float) (length * Math.sin(angle));
            
            oldForce.scl(physicsBody.getMass() * 60);

//            System.out.println(force + " " + angle);
//            System.out.println(oldForce + " " + angle);
            
            force.add(oldForce);
            System.out.println("FORCE2 " + force);
            physicsBody.applyForceToCenter(force, true);

        // singleDimensionVelocity += acceleration;
        // float deltaPitch = pitchAcceleration;
        // pitch += deltaPitch;
        //
        // sprite.rotate(deltaPitch);
        //
        // velocity.x = (float) (singleDimensionVelocity *
        // Math.cos(Math.toRadians(pitch)));
        // velocity.y = (float) (singleDimensionVelocity *
        // Math.sin(Math.toRadians(pitch)));
        //
        // position.x += velocity.x * dt;
        // position.y += velocity.y * dt;
        //
        // sprite.setPosition(position.x, position.y);

        final Vector2 position = physicsBody.getPosition();
        position.x = position.x - sprite.getWidth() / 2;
        position.y = position.y - sprite.getHeight() / 2;

        sprite.setPosition(position.x, position.y);
        sprite.setRotation((float) Math.toDegrees(physicsBody.getAngle()));

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
        float singleDimensionVelocity = physicsBody.getLinearVelocity().len();
        if(singleDimensionVelocity < -1 * maxSingleDimensionVelocity)
        {
            singleDimensionVelocity = -1 * maxSingleDimensionVelocity;
            setAcceleration(0);
        }
        else if(singleDimensionVelocity > maxSingleDimensionVelocity)
        {
            singleDimensionVelocity = maxSingleDimensionVelocity;
            setAcceleration(0);
        }

        if(pitch >= 360)
            pitch -= 360;
        else if(pitch <= -360)
            pitch += 360;

        // if(pitch < -1 * maxPitchAcceleration)
        // {
        // pitch = -1 * maxPitchAcceleration;
        // setPitchAcceleration(0);
        // }
        // else if(pitch > maxPitchAcceleration)
        // {
        // pitch = maxPitchAcceleration;
        // setPitchAcceleration(0);
        // }
    }

    private void checkPositions()
    {
        Vector2 position = physicsBody.getPosition();
        if(position.x < 0)
        {
            position.x = mapDetails.getMapWidth();
        }
        else if(position.x > mapDetails.getMapWidth())
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
        this.pitchAcceleration += pitchAcceleration;
    }

    public void setPitchAcceleration(final float pitchAcceleration)
    {
        this.pitchAcceleration = pitchAcceleration;
    }

    public float getPitch()
    {
        return pitch;
    }

    public float getPitchAcceleration()
    {
        return pitchAcceleration;
    }
}