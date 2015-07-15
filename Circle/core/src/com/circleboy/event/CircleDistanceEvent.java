package com.circleboy.event;

import com.badlogic.gdx.math.Vector2;
import com.circleboy.moveable.Moveable;
import com.circleboy.util.MathUtils;

public class CircleDistanceEvent extends AbstractDrawableEvent
{
    private float triggerDistance;
    private float speedBoost;

    public CircleDistanceEvent(final float triggerDistance, final float speedBoost)
    {
        this.triggerDistance = triggerDistance;
        this.speedBoost = speedBoost;
    }

    @Override
    protected boolean shouldProcess(final Moveable circle, final Moveable moveable)
    {
        Vector2 p1 = circle.getPosition();
        Vector2 p2 = moveable.getPosition();
        float distance = MathUtils.getDistance(p1, p2);
        if(distance <= triggerDistance)
            return true;

        return false;
    }

    @Override
    protected void process(final Moveable circle, final Moveable moveable)
    {
        moveable.setBaseMovement(speedBoost);
    }

    @Override
    protected boolean shouldDelete()
    {
        return true;
    }
}