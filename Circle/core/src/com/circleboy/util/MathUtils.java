package com.circleboy.util;

import com.badlogic.gdx.math.Vector2;

public class MathUtils
{
    public static float getDistance(final Vector2 v1, final Vector2 v2)
    {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
