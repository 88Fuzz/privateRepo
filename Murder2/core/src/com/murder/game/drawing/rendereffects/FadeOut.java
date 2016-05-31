package com.murder.game.drawing.rendereffects;

import com.badlogic.gdx.graphics.Color;
import com.murder.game.MurderMainMain;

/**
 * Changes the screen from clear to a specific color.
 */
public class FadeOut extends Fade
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected float getIncrementalFade(final float fadeTime)
    {
        return MurderMainMain.TIMEPERFRAME / fadeTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkFadeBounds(final Color color)
    {
        if(color.a >= 1)
        {
            color.a = 1;
            return true;
        }

        return false;
    }
}