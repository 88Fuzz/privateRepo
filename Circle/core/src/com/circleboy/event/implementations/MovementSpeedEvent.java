package com.circleboy.event.implementations;

import com.circleboy.event.abstracts.AbstractCircleDistanceEvent;
import com.circleboy.moveable.Moveable;

public class MovementSpeedEvent extends AbstractCircleDistanceEvent
{
    private final float speedBoost;
    private final float baseSpeed;

    public MovementSpeedEvent(float triggerDistance, float speedBoost, float baseSpeed, Operator operator)
    {
        super(triggerDistance, operator);
        this.speedBoost = speedBoost;
        this.baseSpeed = baseSpeed;
    }

    @Override
    protected void process(final Moveable circle, final Moveable moveable)
    {
        moveable.setBaseScreenMovement(speedBoost);
        moveable.setBaseMovement(baseSpeed);
    }
}
