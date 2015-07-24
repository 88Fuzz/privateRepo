package com.circleboy.event.implementations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.circleboy.event.abstracts.AbstractCircleDistanceEvent;
import com.circleboy.event.abstracts.AbstractLayerEvent;
import com.circleboy.moveable.Layer;
import com.circleboy.moveable.Square;
import com.circleboy.moveable.Layer.LayerType;
import com.circleboy.util.definitions.DrawableEventDefinitions;
import com.circleboy.util.definitions.DrawableEventDefinitions.DrawableEventType;

public class AddSquareEvent extends AbstractLayerEvent
{
    private int wrapNumberToActOn;
    private float offset;
    private Layer affectedLayer;
    private TextureAtlas atlas;
    private DrawableEventType eventType;
    private float minWidth;
    private float maxWidth;
    private float minHeight;
    private float maxHeight;

    public AddSquareEvent(final int wrapNumberToActOn, final float offset, final float minWidth, final float maxWidth,
            final float minHeight, final float maxHeight, final Layer affectedLayer, final TextureAtlas atlas,
            final DrawableEventType eventType)
    {
        this.wrapNumberToActOn = wrapNumberToActOn;
        this.offset = offset;
        this.affectedLayer = affectedLayer;
        this.atlas = atlas;
        this.eventType = eventType;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    protected boolean shouldProcess(int wrapNumber)
    {
        if(wrapNumber == wrapNumberToActOn)
            return true;

        return false;
    }

    @Override
    protected void process()
    {
        TextChangeEvent txe = new TextChangeEvent(900.0f, AbstractCircleDistanceEvent.Operator.LESS_THAN, "FUCK YOU");
        Square square = new Square(offset, 0, Square.generateSprite(atlas, (int) minWidth, (int) maxWidth,
                (int) minHeight, (int) maxHeight), LayerType.PEOPLE.getMovementSpeed(), 0, txe);

        if(eventType != null)
            square.addEventList(DrawableEventDefinitions.getDrawableEventList(eventType));

        affectedLayer.addMoveable(square);
    }

    @Override
    protected boolean shouldDelete()
    {
        return true;
    }
}