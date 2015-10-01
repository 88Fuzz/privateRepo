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
    public Bomb()
    {
        super();
    }

    public Bomb(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position, final Vector2 velocity, final float maxAcceleration,
            final float maxPitchAcceleration, final float pitch, final float singleDimensionVelocity)
    {
        this();
        init(atlas, mapDetails, position, velocity, maxAcceleration, maxPitchAcceleration, pitch, singleDimensionVelocity);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position, final Vector2 velocity,
            final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
            final float singleDimensionVelocity)
    {
        super.init(mapDetails, true, position, velocity, maxAcceleration, maxPitchAcceleration, pitch, singleDimensionVelocity);
        // super.init(position);

        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        sprite.setColor(Color.BLACK);
        sprite.setBounds(0, 0, 20, 20);
    }

    @Override
    public void update(float dt)
    {
        super.update(dt);
    }

    @Override
    public float kill()
    {
        setAlive(false);
        return 0;
    }

    @Override
    public float hit()
    {
        return kill();
    }

    @Override
    protected void drawCurrent(SpriteBatch batch)
    {
        //DO nothing for now, will be used in the future
        // TODO Auto-generated method stub
    }
}