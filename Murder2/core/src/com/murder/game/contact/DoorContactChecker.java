package com.murder.game.contact;

import java.util.Iterator;
import java.util.Set;

import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.level.Door;

/**
 * Contact checker for the Door objects.
 */
public class DoorContactChecker implements ContactChecker
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(final Object objA, final Object objB)
    {
        if(!(objA instanceof Drawable && objB instanceof Drawable))
            return false;
        if(!(objA instanceof Door || objB instanceof Door))
            return false;

        final Drawable drawable;
        final Door door;

        if(objA instanceof Door)
        {
            door = (Door) objA;
            drawable = (Drawable) objB;
        }
        else
        {
            door = (Door) objB;
            drawable = (Drawable) objA;
        }

        // Player is trying to unlock a door
        if(drawable.getBodyType() == BodyType.PLAYER)
        {
            final Actor player = (Actor) drawable;
            final Set<ItemType> items = player.getItems();

            for(final Iterator<ItemType> itr = items.iterator(); itr.hasNext();)
            {
                final ItemType item = itr.next();

                if(door.unlockDoor(item))
                {
                    itr.remove();
                    return true;
                }
            }
        }

        return false;
    }
}