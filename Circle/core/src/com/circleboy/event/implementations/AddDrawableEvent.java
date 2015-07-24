package com.circleboy.event.implementations;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.circleboy.event.abstracts.AbstractLayerEvent;
import com.circleboy.moveable.Layer;
import com.circleboy.moveable.Moveable;
import com.circleboy.moveable.Layer.LayerType;
import com.circleboy.util.definitions.DrawableEventDefinitions;
import com.circleboy.util.definitions.DrawableEventDefinitions.DrawableEventType;

public class AddDrawableEvent extends AbstractLayerEvent
{
    private int wrapNumberToActOn;
    private float offset;
    private String textureKey;
    private Layer affectedLayer;
    private TextureAtlas atlas;
    private DrawableEventType eventType;

    public AddDrawableEvent(final int wrapNumberToActOn, final float offset, final String textureKey,
            final Layer affectedLayer, final TextureAtlas atlas, final DrawableEventType eventType)
    {
        this.wrapNumberToActOn = wrapNumberToActOn;
        this.offset = offset;
        this.textureKey = textureKey;
        this.affectedLayer = affectedLayer;
        this.atlas = atlas;
        this.eventType = eventType;
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
        Moveable moveable = new Moveable(offset, 0, new Sprite(atlas.findRegion(textureKey)),
                LayerType.BACKGROUND.getMovementSpeed(), 0);

        if(eventType != null)
            moveable.addEventList(DrawableEventDefinitions.getDrawableEventList(eventType));

        affectedLayer.addMoveable(moveable);
    }

    @Override
    protected boolean shouldDelete()
    {
        return true;
    }
}