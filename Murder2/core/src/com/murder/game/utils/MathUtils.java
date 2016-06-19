package com.murder.game.utils;

import com.badlogic.gdx.math.Vector2;

public class MathUtils
{
    public static float getDistance(final Vector2 vector1, final Vector2 vector2)
    {
        return getDistance(vector1.x, vector1.y, vector2.x, vector2.y);
    }

    public static float getDistance(float x1, float y1, float x2, float y2)
    {
        float xDist = x1 - x2;
        float yDist = y1 - y2;
        float retVal = xDist * xDist + yDist * yDist;

        return (float) Math.sqrt(retVal);
    }
}