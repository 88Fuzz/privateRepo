package com.murder.game.contact;

import java.util.HashMap;
import java.util.Map;

import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.drawables.Drawable;

/**
 * Contact checker for the Drawable objects
 */
public class DrawableContactChecker implements ContactChecker
{
    private Map<BodyType, ContactChecker> bodyTypeContactMap;

    public DrawableContactChecker()
    {
        bodyTypeContactMap = new HashMap<BodyType, ContactChecker>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(final Object objA, final Object objB)
    {
        if(!(objA instanceof Drawable && objB instanceof Drawable))
            return false;

        final ContactChecker contactChecker = bodyTypeContactMap.get(((Drawable) objA).getBodyType());
        if(contactChecker == null)
            return false;

        return contactChecker.check(objA, objB);
    }
}