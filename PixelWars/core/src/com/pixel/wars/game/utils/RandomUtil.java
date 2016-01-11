package com.pixel.wars.game.utils;

import java.util.Random;

public class RandomUtil
{
    private final Random randomGenerator;

    public RandomUtil()
    {
        randomGenerator = new Random();
    }

    public RandomUtil(final long seed)
    {
        randomGenerator = new Random(seed);
    }

    public int getInt(final int min, final int max)
    {
        return randomGenerator.nextInt(max - min + 1) + min;
    }

    public float getFloat(final float min, final float max)
    {
        return randomGenerator.nextFloat() * (max - min) + min;
    }
}