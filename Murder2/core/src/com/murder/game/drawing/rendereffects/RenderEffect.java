package com.murder.game.drawing.rendereffects;

import com.murder.game.drawing.WorldRenderer;

/**
 * Any effects that change how the camera operates should implement this
 * interface.
 */
public interface RenderEffect
{
    /**
     * Update the rendering effect.
     * 
     * @param worldRenderer
     * @param dt
     */
    void update(final WorldRenderer worldRenderer, final float dt);

    /**
     * return true if processing is done for the effect.
     * 
     * @param worldRenderer
     * @return
     */
    boolean isFinished(final WorldRenderer worldRenderer);
}
