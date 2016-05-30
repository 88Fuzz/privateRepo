package com.murder.game.drawing.rendereffects;

import com.badlogic.gdx.math.Vector2;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.utils.RandomUtils;

/**
 * Shakes the camera.
 */
public class ScreenShake implements RenderEffect
{
    private Vector2 minShake;
    private Vector2 maxShake;
    //TODO remove Actor, this is temporary
    private Actor actor;

    public ScreenShake()
    {
        minShake = new Vector2();
        maxShake = new Vector2();
    }

    public void init(final Actor actor, final float xMinShake, final float yMinShake, final float xMaxShake, final float yMaxShake)
    {
        this.actor = actor;
        minShake.x = xMinShake;
        minShake.y = yMinShake;

        maxShake.x = xMaxShake;
        maxShake.y = yMaxShake;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final WorldRenderer worldRenderer, final float dt)
    {
        if(worldRenderer.isActorCenteredOnX() && worldRenderer.isActorCenteredOnY())
        {
            final float x = actor.getPosition().x + RandomUtils.getRandomInt(minShake.x, maxShake.x);
            final float y = actor.getPosition().y + RandomUtils.getRandomInt(minShake.y, maxShake.y);

            worldRenderer.setTargetPosition(x,y);
        }
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