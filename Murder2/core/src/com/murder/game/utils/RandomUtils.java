package com.murder.game.utils;

import java.util.Random;

public class RandomUtils
{
    /**
     * Returns a pseudo-random number between [min, max).
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static int getRandomInt(final Random random, final int min, final int max)
    {
        return random.nextInt((max - 1 - min) + 1) + min;
    }
}