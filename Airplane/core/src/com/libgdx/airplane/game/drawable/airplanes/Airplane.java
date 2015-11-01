package com.libgdx.airplane.game.drawable.airplanes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractMoveable;
import com.libgdx.airplane.game.drawable.weapons.Bomb;
import com.libgdx.airplane.game.drawable.weapons.Hittable;
import com.libgdx.airplane.game.drawable.weapons.Missile;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Airplane extends AbstractMoveable implements Hittable
{
    private float health;

    private final TextureAtlas atlas;

    private final List<Bomb> bombs;
    private final List<Bomb> newBombs;
    private final List<Missile> missiles;
    private final List<Missile> newMissiles;
    private final List<Missile> bullets;
    private final List<Missile> newBullets;

    protected float bombDelay;
    protected float bombTimer;
    private int bombsLeft;

    protected float missileDelay;
    protected float missileTimer;
    private int missilesLeft;

    protected float bulletDelay;
    protected float bulletTimer;
    private boolean fireBullets;

    // Constructor should only be used by classes that extend this class. Once
    // this is called by child class, the child class should call init()
    public Airplane(final TextureAtlas atlas)
    {
        super();
        this.atlas = atlas;

        // TODO: if this ever becomes mobile, pre-allocate bombs
        bombs = new LinkedList<Bomb>();
        newBombs = new LinkedList<Bomb>();
        missiles = new LinkedList<Missile>();
        newMissiles = new LinkedList<Missile>();
        bullets = new LinkedList<Missile>();
        newBullets = new LinkedList<Missile>();
    }

    public Airplane(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final int numMissiles, final float bombDelay,
            final float missileDelay, final float bulletDelay)
    {
        this(atlas);

        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity, numBombs, numMissiles, bombDelay, missileDelay, bulletDelay);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final int numBombs, final int numMissiles, final float bombDelay,
            final float missileDelay, final float bulletDelay)
    {
        super.init(mapDetails, true, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity);
        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.AIRPLANE));
        sprite.setOrigin(0, 0);

        bombsLeft = numBombs;
        this.bombDelay = bombDelay;
        this.bombTimer = bombDelay;

        missilesLeft = numMissiles;
        this.missileDelay = missileDelay;
        this.missileTimer = missileDelay;

        fireBullets = false;
        this.bulletDelay = bulletDelay;
        this.bulletTimer = bulletDelay;

        health = 50;
    }

    /**
     * {@inheritDoc}
     */
    public void update(final float dt)
    {
        super.update(dt);

        updateWeapons(dt);
        updateTimers(dt);
        fireBullet();
    }

    /**
     * sets flag for firing bullets
     * 
     * @param fireBullets
     */
    protected void setFireBullets(final boolean fireBullets)
    {
        this.fireBullets = fireBullets;
    }

    private void updateWeapons(final float dt)
    {
        updateBombs(dt);
        updateMissiles(dt);
        updateBullets(dt);
    }

    /**
     * Iterate over bombs, removes dead bombs or updates them.
     * 
     * @param dt
     */
    private void updateBombs(final float dt)
    {
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
    }

    /**
     * Iterate over bullets, removes dead bullets or updates them.
     * 
     * @param dt
     */
    private void updateBullets(final float dt)
    {
        for(Iterator<Missile> it = bullets.iterator(); it.hasNext();)
        {
            Missile missile = it.next();
            if(!missile.isAlive())
            {
                it.remove();
                continue;
            }
            missile.update(dt);
        }
    }

    /**
     * Iterate over missiles, removes dead missiles or updates them.
     * 
     * @param dt
     */
    private void updateMissiles(final float dt)
    {
        for(Iterator<Missile> it = missiles.iterator(); it.hasNext();)
        {
            Missile missile = it.next();
            if(!missile.isAlive())
            {
                it.remove();
                continue;
            }
            missile.update(dt);
        }
    }

    /**
     * Method to update all timers internal to the Airplane
     * 
     * @param dt
     */
    private void updateTimers(final float dt)
    {
        bombTimer = Math.max(bombTimer - dt, 0);
        missileTimer = Math.max(missileTimer - dt, 0);
        bulletTimer = Math.max(bulletTimer - dt, 0);
    }

    /**
     * Method called to fire a bullet.
     */
    private void fireBullet()
    {
        if(fireBullets && bulletTimer <= 0)
        {
            bulletTimer = bulletDelay;
            newBullets.add(getNewBullet());
        }
    }

    /**
     * Method called to fire a missile.
     */
    protected void fireMissile()
    {
        if(missileTimer <= 0 && missilesLeft > 0)
        {
            missileTimer = missileDelay;
            newMissiles.add(getNewMissile());
            missilesLeft--;
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
     * Returns a new missile
     * 
     * @return
     */
    private Missile getNewMissile()
    {
        final float singleDimensionVelocity = this.singleDimensionVelocity + 500;

        return new Missile(atlas, mapDetails, getFrontOfAirplane(), new Vector2(), Math.abs(singleDimensionVelocity),
                Math.abs(pitch), pitch, singleDimensionVelocity, 2000, true);
    }

    /**
     * returns a new bullet
     * 
     * @return
     */
    private Missile getNewBullet()
    {
        final float singleDimensionVelocity = this.singleDimensionVelocity + 300;

        return new Missile(atlas, mapDetails, getFrontOfAirplane(), new Vector2(), Math.abs(singleDimensionVelocity),
                Math.abs(pitch), pitch, singleDimensionVelocity, 1700, false);
    }

    /**
     * Returns a new bomb. Or, once Bombs are pre-allocated, return a Bomb from
     * the pre-allocated bombs.
     * 
     * @return
     */
    private Bomb getNewBomb()
    {
        final float gravity = mapDetails.getGravity();
        float angle = (float) Math.toDegrees(Math.atan(gravity / velocity.x));
        float singleDimensionVelocity = (float) Math.sqrt(velocity.x * velocity.x + gravity * gravity);

        return new Bomb(atlas, mapDetails, new Vector2(position.x, position.y), new Vector2(0, 0),
                Math.abs(singleDimensionVelocity), Math.abs(angle), angle, singleDimensionVelocity);
    }

    /**
     * Will return the front of the airplane after adjusting for the pitch
     * 
     * @return
     */
    protected Vector2 getFrontOfAirplane()
    {
        final Vector2 dimension = getDimension();
        final float x = (float) (position.x + Math.cos(Math.toRadians(pitch)) * dimension.x);
        final float y = (float) (position.y + Math.sin(Math.toRadians(pitch)) * dimension.x);

        return new Vector2(x, y);
    }

    /**
     * Get the list of all active bombs.
     * 
     * @return
     */
    public List<Bomb> getBombs()
    {
        return bombs;
    }

    /**
     * Get the list of all active bullets.
     * 
     * @return
     */
    public List<Missile> getBullets()
    {
        return bullets;
    }

    /**
     * Get the list of all active missiles
     * 
     * @return
     */
    public List<Missile> getMissiles()
    {
        return missiles;
    }

    /**
     * Get a list of all bombs that have been activated since the last update().
     * This will move the new bombs to the active bombs list.
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

    /**
     * Get a list of all bullets that have been activated since the last
     * update(). This will move the new bullets to the active bullet list.
     * 
     * @return
     */
    public List<Missile> getNewBullets()
    {
        List<Missile> returnList = new LinkedList<Missile>();

        // Use iterator to safely remove objects in the list
        for(Iterator<Missile> it = newBullets.iterator(); it.hasNext();)
        {
            Missile missile = it.next();
            returnList.add(missile);
            bullets.add(missile);
            it.remove();
        }

        return returnList;
    }

    /**
     * Get a list of all missiles that have been activated since the last
     * update(). This will move the new missiles to the active missiles list.
     * 
     * @return
     */
    public List<Missile> getNewMissiles()
    {
        List<Missile> returnList = new LinkedList<Missile>();

        // Use iterator to safely remove objects in the list
        for(Iterator<Missile> it = newMissiles.iterator(); it.hasNext();)
        {
            Missile missile = it.next();
            returnList.add(missile);
            missiles.add(missile);
            it.remove();
        }

        return returnList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float kill()
    {
        setAlive(false);
        final float damageDone = health;
        health = 0;

        return damageDone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void drawCurrent(final SpriteBatch batch)
    {
        // TODO implement later on, do nothing for now
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float hit(final int damageTaken)
    {
        health -= damageTaken;
        if(health <= 0)
            kill();

        return damageTaken;
    }

    @Override
    public int getAttackDamage()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void getAttackDamageType()
    {
        // TODO Auto-generated method stub

    }
}