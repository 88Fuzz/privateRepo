package com.libgdx.airplane.game.drawable.airplanes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.airplane.game.drawable.airplanes.Maneuvers.RotationManeuver;
import com.libgdx.airplane.game.drawable.airplanes.Maneuvers.UTurnManeuver;
import com.libgdx.airplane.game.utils.MapDetails;

public class Player extends Airplane
{
    private static final float MAX_ACCELERATION = 500000000.0f;
    private static final float MAX_PITCH_ACCELERATION = 0.75f;

    private final Vector2 initialPosition;

    public Player(final TextureAtlas atlas, final World physicsWorld, final MapDetails mapDetails,
            final Vector2 position, final Vector2 velocity, final float maxSingleDimensionVelocity,
            final float maxPitchAcceleration, final float pitch, final float singleDimensionVelocity,
            final int numBombs, final int numMissiles, final float bombDelay, final float missileDelay,
            final float bulletDelay)
    {
        super(atlas, physicsWorld);
        this.initialPosition = new Vector2();

        init(atlas, physicsWorld, mapDetails, position, velocity, maxSingleDimensionVelocity, maxPitchAcceleration,
                pitch, singleDimensionVelocity, numBombs, numMissiles, bombDelay, missileDelay, bulletDelay);
    }

    public void init(final TextureAtlas atlas, final World physicsWorld, final MapDetails mapDetails,
            final Vector2 position, final Vector2 velocity, final float maxSingleDimensionVelocity,
            final float maxPitchAcceleration, final float pitch, final float singleDimensionVelocity,
            final int numBombs, final int numMissiles, final float bombDelay, final float missileDelay,
            final float bulletDelay)
    {
        super.init(atlas, physicsWorld, mapDetails, position, velocity, maxSingleDimensionVelocity,
                maxPitchAcceleration, pitch, singleDimensionVelocity, numBombs, numMissiles, bombDelay, missileDelay,
                bulletDelay);

        initialPosition.x = position.x;
        initialPosition.y = position.y;
    }

    /**
     * {@inheritDoc}
     */
    public void update(final float dt)
    {
        super.update(dt);

        if(!isAlive())
        {
            physicsBody.setTransform(initialPosition, 0);

            bombTimer = bombDelay;
            missileTimer = missileDelay;
            bulletTimer = bulletDelay;
            setFireBullets(false);

            setAlive(true);
        }
    }

    public void processKeyDown(final int keyCode)
    {
        // TODO create a map from key to action with anonymous inner classes
        if(Input.Keys.D == keyCode)
        {
            addAcceleration(MAX_ACCELERATION);
        }
        else if(Input.Keys.A == keyCode)
        {
            addAcceleration(-1 * MAX_ACCELERATION);
        }
        else if(Input.Keys.W == keyCode)
        {
            addPitchAcceleration(MAX_PITCH_ACCELERATION);
        }
        else if(Input.Keys.S == keyCode)
        {
            addPitchAcceleration(-1 * MAX_PITCH_ACCELERATION);
        }
        else if(Input.Keys.Q == keyCode)
        {
            setManeuver(new RotationManeuver(2.0f, 360f));
        }
        else if(Input.Keys.E == keyCode)
        {
            setManeuver(new UTurnManeuver(2.0f));
        }
        else if(Input.Keys.SPACE == keyCode)
        {
            dropBomb();
        }
        else if(Input.Keys.CONTROL_LEFT == keyCode)
        {
            setFireBullets(true);
        }
        else if(Input.Keys.SHIFT_LEFT == keyCode)
        {
            fireMissile();
        }
    }

    public void processKeyUp(final int keyCode)
    {
        if(Input.Keys.D == keyCode || Input.Keys.A == keyCode)
        {
            setAcceleration(0);
        }
        else if(Input.Keys.W == keyCode || Input.Keys.S == keyCode)
        {
            setPitchAcceleration(0);
        }
        else if(Input.Keys.CONTROL_LEFT == keyCode)
        {
            setFireBullets(false);
        }
    }
}