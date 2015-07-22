package com.circleboy.util;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class MathUtils
{
    private static final Random rand = new Random();

    public static float getDistance(final Vector2 v1, final Vector2 v2)
    {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
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