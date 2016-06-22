package com.murder.game.contact;

import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.level.Exit;

/**
 * Contact checker for Exit.
 */
public class ExitContactChecker implements ContactChecker
{

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(final Object objA, final Object objB)
    {
        if(!((objA instanceof Actor && objB instanceof Exit) || (objA instanceof Exit && objB instanceof Actor)))
            return false;

        final Actor actor;

        if(((Drawable) objA).getBodyType() == BodyType.PLAYER && ((Drawable) objB).getBodyType() == BodyType.EXIT)
            actor = (Actor) objA;
        else if(((Drawable) objB).getBodyType() == BodyType.PLAYER && ((Drawable) objA).getBodyType() == BodyType.EXIT)
            actor = (Actor) objB;
        else
            return false;

        actor.onExit();
        return true;
    }
}