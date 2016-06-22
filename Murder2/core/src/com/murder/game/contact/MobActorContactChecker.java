package com.murder.game.contact;

import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Mob;

/**
 * Contact checker for Actor.
 */
public class MobActorContactChecker implements ContactChecker
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(final Object objA, final Object objB)
    {
        if(!((objA instanceof Mob && objB instanceof Actor && !(objB instanceof Mob))
                || (objA instanceof Actor && objB instanceof Mob && !(objA instanceof Mob))))
            return false;

        final Actor actor;

        if(objA instanceof Mob)
        {
            actor = (Actor) objB;
        }
        else
        {
            actor = (Actor) objA;
        }

        actor.setMobTouched();
        return false;
    }
}