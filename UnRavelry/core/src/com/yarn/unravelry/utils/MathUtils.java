package com.yarn.unravelry.utils;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class MathUtils
{
    private static final Random rand = new Random();

    public static float getDistance(final Vector2 v1, final Vector2 v2)
    {
        return MathUtils.getDistance(v1.x, v1.y, v2.x, v2.y);
    }

    public static float getDistance(final float x1, final float y1, final float x2, final float y2)
    {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static int randInt(final int min, final int max)
    {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static int randInt(final int max)
    {
        return rand.nextInt(max);
    }
}