package com.circleboy.util.definitions;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.circleboy.event.AddDrawableEvent;
import com.circleboy.event.AbstractLayerEvent;
import com.circleboy.moveable.Layer;
import com.circleboy.moveable.Layer.LayerType;

public class LayerEventDefinitions
{
    public static void configureLayerEvent(LinkedHashMap<LayerType, Layer> scene, TextureAtlas atlas)
    {
        createCircleLayerEvents(scene, atlas);
        createBackgroundLayerEvents(scene, atlas);
    }

    private static void createCircleLayerEvents(LinkedHashMap<LayerType, Layer> scene, TextureAtlas atlas)
    {
        LinkedList<AbstractLayerEvent> tmpList = new LinkedList<AbstractLayerEvent>();
        Layer layer = scene.get(LayerType.CIRCLE);
        layer.addEventList(tmpList);
    }

    private static void createBackgroundLayerEvents(LinkedHashMap<LayerType, Layer> scene, TextureAtlas atlas)
    {
        LinkedList<AbstractLayerEvent> tmpList = new LinkedList<AbstractLayerEvent>();
        //TODO make the offset a ratio of screen resolution
        AddDrawableEvent event = new AddDrawableEvent(2, 2500, TextureConstants.FOX_TEXTURE_KEY,
                scene.get(LayerType.CIRCLE), atlas);

        tmpList.add(event);

        Layer layer = scene.get(LayerType.BACKGROUND);
        layer.addEventList(tmpList);
    }
}