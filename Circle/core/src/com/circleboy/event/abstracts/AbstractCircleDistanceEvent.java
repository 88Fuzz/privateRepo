package com.circleboy.event.abstracts;

import com.badlogic.gdx.math.Vector2;
import com.circleboy.moveable.Moveable;
import com.circleboy.util.MathUtils;

public abstract class AbstractCircleDistanceEvent extends AbstractDrawableEvent
{
    public enum Operator
    {
        LESS_THAN, GREATER_THAN;
    }

    private final float triggerDistance;
    private final Operator operator;

    public AbstractCircleDistanceEvent(final float triggerDistance, final Operator operator)
    {
        this.triggerDistance = triggerDistance;
        this.operator = operator;
    }

    @Override
    protected boolean shouldProcess(final Moveable circle, final Moveable moveable)
    {
        Vector2 p1 = circle.getPosition();
        Vector2 p2 = moveable.getPosition();
        float distance = MathUtils.getDistance(p1, p2);
        if(Operator.LESS_THAN.equals(operator))
            return Float.compare(distance, triggerDistance) < 0;

        return Float.compare(distance, triggerDistance) > 0;
    }

    @Override
    abstract protected void process(final Moveable circle, final Moveable moveable);

    @Override
    protected boolean shouldDelete()
    {
        return true;
    }
}