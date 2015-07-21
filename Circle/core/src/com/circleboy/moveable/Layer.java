package com.circleboy.moveable;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.event.AbstractLayerEvent;
import com.circleboy.util.GraphicsUtils;
import com.circleboy.util.definitions.LevelWrapDetails;

public class Layer
{
    public enum LayerType
    {
        CIRCLE(0, 0, 0), BACKGROUND(-200f, 0, 0);

        private final float movementSpeed;
        private final float initX;
        private final float initY;

        LayerType(final float movementSpeed, final float initX, final float initY)
        {
            this.movementSpeed = movementSpeed;
            this.initX = initX;
            this.initY = initY;
        }

        public float getMovementSpeed()
        {
            return movementSpeed;
        }

        public float getInitX()
        {
            return initX;
        }

        public float getInitY()
        {
            return initY;
        }
    }

    private final LinkedList<Moveable> nodes;
    private final LinkedList<AbstractLayerEvent> events;
    private final TextureAtlas atlas;
    private final LayerType layerType;

    private int wrapNumber;
    private String layerSpriteKey;

    public Layer(final TextureAtlas atlas, final LayerType layerType, int numberOfNodes)
    {
        this.atlas = atlas;
        this.layerType = layerType;
        nodes = new LinkedList<Moveable>();
        events = new LinkedList<AbstractLayerEvent>();

        wrapNumber = 0;
        layerSpriteKey = LevelWrapDetails.getLayerStringKey(layerType, wrapNumber);

        float x = layerType.getInitX();
        float y = layerType.getInitY();
        for(int i = 0; i < numberOfNodes; i++)
        {
            Moveable tmpMoveable = new Moveable(x, y, new Sprite(atlas.findRegion(layerSpriteKey)),
                    layerType.getMovementSpeed(), 0);
            nodes.add(tmpMoveable);
            x += tmpMoveable.getWidth();
        }
    }

    public void update(final Moveable circle, final float dt, final float movementFactor)
    {
        for(Moveable node: nodes)
            node.update(circle, dt, movementFactor);

        Moveable first = nodes.getFirst();
        Vector2 position = first.getPosition();
        float width = first.getOriginalSpriteWidth();
        if(position.x + width < 0)
        {
            wrapNumber++;

            String newLayerKey = LevelWrapDetails.getLayerStringKey(layerType, wrapNumber);

            if(newLayerKey != null)
            {
                layerSpriteKey = newLayerKey;
            }
            Sprite firstSprite = first.getSprite();
            GraphicsUtils.applyTextureRegion(firstSprite, atlas.findRegion(layerSpriteKey));

            nodes.poll();
            Moveable last = nodes.getLast();
            position = last.getPosition();
            width = last.getWidth();

            first.setPosition(position.x + width, position.y);
            nodes.offerLast(first);
        }

        processEvents(dt);
    }

    private void processEvents(float dt)
    {
        if(events.size() == 0)
            return;

        AbstractLayerEvent event = events.getFirst();
        while(event != null)
        {
            if(event.checkEvent(wrapNumber))
            {
                events.poll();
                if(events.size() == 0)
                    return;

                event = events.getFirst();
            }else
            {
                event = null;
            }
        }
    }

    public void addEventList(List<AbstractLayerEvent> eventList)
    {
        events.addAll(eventList);
    }

    public Moveable getFirstInList()
    {
        return nodes.getFirst();
    }

    public void draw(SpriteBatch batch)
    {
        // if(LayerType.BACKGROUND.equals(layerType))
        // return;
        //
        // System.out.println("size ");

        for(Moveable node: nodes)
        {
            node.draw(batch);
        }
    }

    public void addMoveable(Moveable moveable)
    {
        nodes.addFirst(moveable);
    }
}