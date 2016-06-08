package com.murder.game.contact;

import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.level.Item;

/**
 * Contact checker for the Item objects.
 */
public class ItemContactChecker implements ContactChecker
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(final Object objA, final Object objB)
    {
        if(!(objA instanceof Drawable && objB instanceof Drawable))
            return false;
        if(!(objA instanceof Item || objB instanceof Item))
            return false;

        final Drawable drawable;
        final Item item;

        if(objA instanceof Item)
        {
            item = (Item) objA;
            drawable = (Drawable) objB;
        }
        else
        {
            item = (Item) objB;
            drawable = (Drawable) objA;
        }

        if(drawable.getBodyType() == BodyType.PLAYER)
        {
            ((Actor) drawable).addItem(item.pickUpItem());
            return true;
        }

        return false;
    }
}