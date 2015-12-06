package com.libgdx.airplane.game.drawable.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractMoveable;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Bomb extends AbstractMoveable implements Hittable
{
    //TODO figure out a better way to handle damage dealt with the health
    private int damage;

    public Bomb()
    {
        super();
    }

    public Bomb(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position, final Vector2 velocity,
            final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity)
    {
        this();
        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity)
    {
        //TODO fix this
//        super.init(mapDetails, true, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
//                singleDimensionVelocity);

        //TODO Damage should be calculated by a look up table with the bomb type
        this.damage = 50;

        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        sprite.setColor(Color.BLACK);
        sprite.setBounds(0, 0, 20, 20);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(float dt)
    {
        super.update(dt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float kill()
    {
        setAlive(false);
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float hit(final int damageTaken)
    {
        return kill();
    }

    @Override
    protected void drawCurrent(SpriteBatch batch)
    {
        // DO nothing for now, will be used in the future
        // TODO Auto-generated method stub
    }

    @Override
    public int getAttackDamage()
    {
        return damage;
    }

    @Override
    public void getAttackDamageType()
    {
        // TODO Auto-generated method stub
    }
}