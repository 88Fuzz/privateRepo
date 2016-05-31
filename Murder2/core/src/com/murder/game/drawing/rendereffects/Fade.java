package com.murder.game.drawing.rendereffects;

import com.badlogic.gdx.graphics.Color;
import com.murder.game.drawing.WorldRenderer;

/**
 * Effect to change the alpha overlay on the screen.
 */
public abstract class Fade implements RenderEffect
{
    private Color color;
    private float incrementalFade;
    private boolean finished;

    public void init(final Color color, final float fadeTime)
    {
        this.color = color;
        incrementalFade = getIncrementalFade(fadeTime);
        finished = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final WorldRenderer worldRenderer, final float dt)
    {
        color.a += incrementalFade;
        finished = checkFadeBounds(color);

        worldRenderer.setScreenSpriteColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished(final WorldRenderer worldRenderer)
    {
        return finished;
    }

    /**
     * Set the incremental fade value for the fade effect.
     */
    protected abstract float getIncrementalFade(final float fadeTime);

    /**
     * Checks if the fade effect is finished. Returns true if finished, else
     * false.
     * 
     * @param color
     * @return
     */
    protected abstract boolean checkFadeBounds(final Color color);
}
