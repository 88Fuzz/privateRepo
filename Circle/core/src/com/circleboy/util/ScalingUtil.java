package com.circleboy.util;

import com.badlogic.gdx.math.Vector2;

public class ScalingUtil
{
    private static Vector2 scale = new Vector2(1, 1);
    private static Vector2 baseResolution = new Vector2(1920, 1080);

    public static void setScale(final float x, final float y)
    {
        scale.x = x / baseResolution.x;
        scale.y = y / baseResolution.y;
    }

    public static void setScale(final Vector2 resolution)
    {
        setScale(resolution.x, resolution.y);
    }

    public static Vector2 getScale()
    {
        return scale;
    }
}