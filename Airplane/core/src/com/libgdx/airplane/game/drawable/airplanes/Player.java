package com.libgdx.airplane.game.drawable.airplanes;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.utils.MapDetails;

public class Player extends Airplane
{
    private static final float MAX_ACCELERATION = 1.0f;
    private static final float MAX_PITCH_ACCELERATION = 0.75f;

    private final Vector2 initialPosition;

    public Player(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final int numMissiles, final float bombDelay,
            final float missileDelay, final float bulletDelay)
    {
        super(atlas);
        this.initialPosition = new Vector2();

        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity, numBombs, numMissiles, bombDelay, missileDelay, bulletDelay);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final int numMissiles, final float bombDelay,
            final float missileDelay, final float bulletDelay)
    {
        super.init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity, numBombs, numMissiles, bombDelay, missileDelay, bulletDelay);

        sprite.setColor(Color.GREEN);
        // sprite.setBounds(0, 0, 100, 20);

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
            position.x = initialPosition.x;
            position.y = initialPosition.y;

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