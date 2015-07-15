package com.circleboy.util.definitions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.circleboy.event.AbstractDrawableEvent;
import com.circleboy.event.CircleDistanceEvent;

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
        CircleDistanceEvent event = new CircleDistanceEvent(300.0f, 100.0f);
        tmpList.add(event);

        DRAWABLE_EVENTS.put(DrawableEventType.FOX, tmpList);
    }

    public static LinkedList<AbstractDrawableEvent> getDrawableEventList(final DrawableEventType type)
    {
        return DRAWABLE_EVENTS.get(type);
    }
}