package com.murder.game.utils;

import java.util.Random;

public class RandomUtils
{
    private static final Random random = new Random();

    /**
     * Returns a pseudo-random number between [min, max).
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(final int min, final int max)
    {
        return random.nextInt((max - 1 - min) + 1) + min;
    }

    /**
     * Returns a pseudo-random number between [min, max).
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static float getRandomFloat(final float min, final float max)
    {
        return random.nextFloat() * (max - min) + min;
    }
}