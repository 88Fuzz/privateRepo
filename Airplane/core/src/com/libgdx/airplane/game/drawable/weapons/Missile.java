package com.libgdx.airplane.game.drawable.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractMoveable;
import com.libgdx.airplane.game.utils.CollisionDetection;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Missile extends AbstractMoveable implements Hittable
{
    float maxDistance;
    float distanceTraveled;

    public Missile()
    {
        super();
    }

    public Missile(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final float maxDistance, final boolean isMissile)
    {
        this();
        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity, maxDistance, isMissile);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity, final float maxDistance, final boolean isMissile)
    {
        super.init(mapDetails, true, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity);

        distanceTraveled = 0;
        this.maxDistance = maxDistance;

        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        if(isMissile)
        {
            sprite.setColor(Color.YELLOW);
            sprite.setBounds(0, 0, 15, 15);
        }
        else
        {
            sprite.setColor(Color.CYAN);
            sprite.setBounds(0, 0, 10, 10);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final float dt)
    {
        Vector2 oldPos = new Vector2(position.x, position.y);
        super.update(dt);
        distanceTraveled += CollisionDetection.getDistance(oldPos, position);
        if(distanceTraveled > maxDistance)
            distanceReached();
    }

    /**
     * Method called when the missile has traveled the maximum distance. By
     * default the missile disappears
     */
    public void distanceReached()
    {
        kill();
    }

    @Override
    public float hit()
    {
        return kill();
    }

    @Override
    public float kill()
    {
        setAlive(false);
        return 0;
    }

    @Override
    protected void drawCurrent(SpriteBatch batch)
    {
        // DO NOTHING for now
        // TODO Auto-generated method stub
    }
}