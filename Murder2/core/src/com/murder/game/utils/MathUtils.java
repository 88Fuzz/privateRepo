package com.murder.game.utils;

public class MathUtils
{
    /*
     * TODO it may be more performant for path finding to not take the sqrt all
     * the time and just return 10 if tiles are vertical or horizontal and 14 if
     * diagonal.
     */
    public static float getDistance(float x1, float y1, float x2, float y2)
    {
        float xDist = x1 - x2;
        float yDist = y1 - y2;
        float retVal = xDist * xDist + yDist * yDist;

        return (float) Math.sqrt(retVal);
    }
}