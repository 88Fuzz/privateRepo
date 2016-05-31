package com.murder.game.drawing.rendereffects;

import com.badlogic.gdx.math.Vector2;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.utils.RandomUtils;

/**
 * Shakes the camera.
 */
// TODO get rid of this, in favor of screen blurring.
public class ScreenShake implements RenderEffect
{
    private Vector2 minShake;
    private Vector2 maxShake;
    private Vector2 cameraPosition;
    private float maxWaitTime;
    private float waitTime;

    public ScreenShake()
    {
        minShake = new Vector2();
        maxShake = new Vector2();
        cameraPosition = new Vector2();
    }

    /**
     * Sets the min and max screen shake range. The min and max range should
     * both be negative or positive such that minShake && maxShake =<0 ||
     * minShake && maxShake >= 0. When the screen shakes, the values will be
     * decided to be less than 0 or greater than 0 randomly.
     *
     * 
     * @param xMinShake
     * @param yMinShake
     * @param xMaxShake
     * @param yMaxShake
     */
    public void init(final float xMinShake, final float yMinShake, final float xMaxShake, final float yMaxShake, final float maxWaitTime)
    {
        minShake.x = xMinShake;
        minShake.y = yMinShake;

        maxShake.x = xMaxShake;
        maxShake.y = yMaxShake;

        waitTime = 0;
        this.maxWaitTime = maxWaitTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final WorldRenderer worldRenderer, final float dt)
    {
        final int xSign = getSign();
        final int ySign = getSign();

        if(worldRenderer.isActorCenteredOnX() && worldRenderer.isActorCenteredOnY())
        {
            waitTime -= dt;
            if(waitTime <= 0)
            {
                waitTime = maxWaitTime;
                cameraPosition = worldRenderer.getCameraPosition(cameraPosition);
                final float x = cameraPosition.x + xSign * RandomUtils.getRandomFloat(minShake.x, maxShake.x);
                final float y = cameraPosition.y + ySign * RandomUtils.getRandomFloat(minShake.y, maxShake.y);

                worldRenderer.setTargetPosition(x, y);
            }
        }
    }

    private int getSign()
    {
        if(RandomUtils.getRandomInt(0, 2) == 1)
            return 1;
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished(final WorldRenderer worldRenderer)
    {
        return false;
    }
}