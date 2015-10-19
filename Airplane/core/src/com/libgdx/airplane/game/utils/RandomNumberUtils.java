package com.libgdx.airplane.game.utils;

import java.util.Random;

public class RandomNumberUtils
{
    static private final Random RNG = new Random(1);

    public static int getRandomInt(final int min, final int max)
    {
        return RNG.nextInt(max - min + 1) + min;
    }

    public static float getRandomFloat(final int min, final int max)
    {
        // TODO verify this does what you think it does
        return RNG.nextFloat() * (max - min + 1) + min;
    }
}
