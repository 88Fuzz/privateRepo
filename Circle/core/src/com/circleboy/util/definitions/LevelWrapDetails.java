package com.circleboy.util.definitions;

import java.util.HashMap;
import java.util.Map;

import com.circleboy.moveable.Layer.LayerType;

public class LevelWrapDetails
{
    /*
     * Map that controls when a background should change. For example, once the
     * background layer has wrapped 3 times it should start adding the <new
     * texture> to the layer
     */
    private static final Map<LayerType, Map<Integer, String>> levelWrapMap = new HashMap<LayerType, Map<Integer, String>>()
    {
        private static final long serialVersionUID = 1L;
        {
            put(LayerType.CIRCLE, new HashMap<Integer, String>()
            {
                private static final long serialVersionUID = 1L;
                {
                    put(0, TextureConstants.CIRCLE_TEXTURE_KEY);
                }
            });
            put(LayerType.PEOPLE, new HashMap<Integer, String>()
            {
                private static final long serialVersionUID = 1L;
                {
                    put(0, TextureConstants.SQUARE_KEY);
                }
            });
            put(LayerType.BACKGROUND, new HashMap<Integer, String>()
            {
                private static final long serialVersionUID = 1L;
                {
                    put(0, TextureConstants.TREES_TEXTURE_KEY);
                    put(1, TextureConstants.TREES_ORANGE_TEXTURE_KEY);
                }
            });
        }
    };

    public static String getLayerStringKey(LayerType layerType, int wrapNumber)
    {
        Map<Integer, String> map = levelWrapMap.get(layerType);
        if(map != null)
        {
            return map.get(wrapNumber);
        }
        return null;
    }
}