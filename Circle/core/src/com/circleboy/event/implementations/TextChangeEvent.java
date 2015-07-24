package com.circleboy.event.implementations;

import com.circleboy.event.abstracts.AbstractCircleDistanceEvent;
import com.circleboy.moveable.Moveable;

public class TextChangeEvent extends AbstractCircleDistanceEvent
{
    private final String text;

    public TextChangeEvent(final float triggerDistance, final Operator operator, final String text)
    {
        super(triggerDistance, operator);
        this.text = text;
    }

    @Override
    protected void process(Moveable circle, Moveable moveable)
    {
        moveable.setText(text);
    }
}
