package com.libgdx.airplane.game.drawable.airplanes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.drawable.weapons.Bomb;
import com.libgdx.airplane.game.utils.MapDetails;

public class Player extends Airplane
{
    private static final float MAX_ACCELERATION = 1.0f;
    private static final float MAX_PITCH_ACCELERATION = 0.75f;

    private final TextureAtlas atlas;
    private final List<Bomb> bombs;
    private final List<Bomb> newBombs;
    private final Vector2 initialPosition;

    private float bombDelay;
    private float bombTimer;
    private int bombsLeft;

    public Player(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final float bombDelay)
    {
        super();
        this.atlas = atlas;
        this.initialPosition = new Vector2();

        // TODO: if this ever becomes mobile, pre-allocate bombs
        bombs = new LinkedList<Bomb>();
        newBombs = new LinkedList<Bomb>();
        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity, numBombs, bombDelay);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final float bombDelay)
    {
        super.init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity);

        initialPosition.x = position.x;
        initialPosition.y = position.y;
        bombsLeft = numBombs;
        this.bombDelay = bombDelay;
        this.bombTimer = bombDelay;
    }

    public void update(final float dt)
    {
        super.update(dt);

        // Use iterator to safely remove objects in the list
        for(Iterator<Bomb> it = bombs.iterator(); it.hasNext();)
        {
            Bomb bomb = it.next();

            if(!bomb.isAlive())
            {
                it.remove();
                continue;
            }

            bomb.update(dt);
        }

        bombTimer = Math.max(bombTimer - dt, 0);

        if(!isAlive())
        {
            position.x = initialPosition.x;
            position.y = initialPosition.y;
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
    }

    /**
     * Method that should be when a bomb should be dropped.
     */
    public void dropBomb()
    {
        if(bombTimer <= 0 && bombsLeft > 0)
        {
            bombTimer = bombDelay;
            newBombs.add(getNewBomb());
            bombsLeft--;
        }
    }

    /**
     * Returns a new bomb. Or, once Bombs are pre-allocated, return a Bomb from
     * the pre-allocated bombs.
     * 
     * @return
     */
    private Bomb getNewBomb()
    {
        float gravity = -500.8f;
        float angle = (float) Math.toDegrees(Math.atan(gravity / velocity.x));
        float singleDimensionVelocity = (float) Math.sqrt(velocity.x * velocity.x + gravity * gravity);

        return new Bomb(atlas, mapDetails, new Vector2(position.x, position.y), new Vector2(0, 0),
                singleDimensionVelocity, Math.abs(angle), angle, singleDimensionVelocity);
    }

    /**
     * Get a list of all bombs
     * 
     * @return
     */
    public List<Bomb> getBombs()
    {
        return bombs;
    }

    /**
     * Get a list of all bombs that have been activated since the last update().
     * This will move the new bombs to the usual bombs list.
     * 
     * @return
     */
    public List<Bomb> getNewBombs()
    {
        List<Bomb> returnList = new LinkedList<Bomb>();

        // Use iterator to safely remove objects in the list
        for(Iterator<Bomb> it = newBombs.iterator(); it.hasNext();)
        {
            Bomb bomb = it.next();
            returnList.add(bomb);
            bombs.add(bomb);
            it.remove();
        }

        return returnList;
    }
}