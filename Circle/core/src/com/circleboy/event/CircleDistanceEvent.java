package com.circleboy.event;

import com.badlogic.gdx.math.Vector2;
import com.circleboy.moveable.Moveable;
import com.circleboy.util.MathUtils;

public class CircleDistanceEvent extends AbstractDrawableEvent
{
    public enum Operator
    {
        LESS_THAN, GREATER_THAN;
    }

    private float triggerDistance;
    private float speedBoost;
    private float baseSpeed;
    private Operator operator;

    public CircleDistanceEvent(final float triggerDistance, final float speedBoost, final float baseSpeed, final Operator operator)
    {
        this.triggerDistance = triggerDistance;
        this.speedBoost = speedBoost;
        this.operator = operator;
        this.baseSpeed = baseSpeed;
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
    protected void process(final Moveable circle, final Moveable moveable)
    {
        moveable.setBaseScreenMovement(speedBoost);
        moveable.setBaseMovement(baseSpeed);
    }

    @Override
    protected boolean shouldDelete()
    {
        return true;
    }
}