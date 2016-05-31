package com.murder.game.drawing.rendereffects;

import com.badlogic.gdx.graphics.Color;
import com.murder.game.MurderMainMain;

/**
 * Changes screen from a color to completely clear.
 */
public class FadeIn extends Fade
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected float getIncrementalFade(final float fadeTime)
    {
        return -1 * MurderMainMain.TIMEPERFRAME / fadeTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkFadeBounds(final Color color)
    {
        if(color.a <= 0)
        {
            color.a = 0;
            return true;
        }

        return false;
    }
}