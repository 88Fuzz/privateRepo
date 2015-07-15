package com.circleboy.event;

import com.circleboy.moveable.Moveable;

public abstract class AbstractDrawableEvent
{
    abstract protected boolean shouldProcess(final Moveable circle, final Moveable moveable);

    abstract protected void process(final Moveable circle, final Moveable moveable);

    abstract protected boolean shouldDelete();

    public boolean checkEvent(final Moveable circle, final Moveable moveable)
    {
        if(shouldProcess(circle, moveable))
        {
            process(circle, moveable);
            return shouldDelete();
        }

        return false;
    }

}
