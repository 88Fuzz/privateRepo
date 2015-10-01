package com.libgdx.airplane.game.drawable.airplanes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractMoveable;
import com.libgdx.airplane.game.drawable.weapons.Hittable;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Airplane extends AbstractMoveable implements Hittable
{

    // Constructor should only be used by classes that extend this class. Once
    // this is called by child class, the child class should call init()
    protected Airplane()
    {
        super();
    }

    public Airplane(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position,
            final Vector2 velocity, final float maxAcceleration, final float maxPitchAcceleration, final float pitch,
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
        super.init(mapDetails, true, position, velocity, maxAcceleration, maxPitchAcceleration, pitch,
                singleDimensionVelocity);

        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        sprite.setColor(Color.GREEN);
        sprite.setBounds(0, 0, 100, 20);
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
        // TODO implement later on, do nothing for now
    }
}