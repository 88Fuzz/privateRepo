package com.circleboy.util.definitions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.circleboy.event.AbstractDrawableEvent;
import com.circleboy.event.CircleDistanceEvent;
import com.circleboy.moveable.Layer.LayerType;

public class DrawableEventDefinitions
{
    public enum DrawableEventType
    {
        FOX;
    }

    private static final Map<DrawableEventType, LinkedList<AbstractDrawableEvent>> DRAWABLE_EVENTS = new HashMap<DrawableEventType, LinkedList<AbstractDrawableEvent>>();

    public static final void initializeDrawableEventDefinitions()
    {
        LinkedList<AbstractDrawableEvent> tmpList = new LinkedList<AbstractDrawableEvent>();
        //TODO these values need to be a ratio of the screen resolution!
        CircleDistanceEvent event = new CircleDistanceEvent(900.0f, 0.0f, 100.0f, CircleDistanceEvent.Operator.LESS_THAN);
        tmpList.add(event);
        event = new CircleDistanceEvent(1220.0f, LayerType.BACKGROUND.getMovementSpeed(), 0.0f, CircleDistanceEvent.Operator.GREATER_THAN);
        tmpList.add(event);
        event = new CircleDistanceEvent(600.0f, 0f, 200.0f, CircleDistanceEvent.Operator.LESS_THAN);
        tmpList.add(event);
        event = new CircleDistanceEvent(900.0f, 0f, 50.0f, CircleDistanceEvent.Operator.GREATER_THAN);
        tmpList.add(event);
        event = new CircleDistanceEvent(1600.0f, LayerType.BACKGROUND.getMovementSpeed(), 0.0f, CircleDistanceEvent.Operator.GREATER_THAN);
        tmpList.add(event);

        DRAWABLE_EVENTS.put(DrawableEventType.FOX, tmpList);
    }

    public static LinkedList<AbstractDrawableEvent> getDrawableEventList(final DrawableEventType type)
    {
        return DRAWABLE_EVENTS.get(type);
    }
}