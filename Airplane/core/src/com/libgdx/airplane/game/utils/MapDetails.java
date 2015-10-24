package com.libgdx.airplane.game.utils;

/**
 * Class used to hold information about the current map.
 */
public class MapDetails
{
    private final int mapWidth;
    private final float gravity;

    public MapDetails(final int mapWidth, final float gravity)
    {
        this.mapWidth = mapWidth;
        this.gravity = gravity;
    }

    public int getMapWidth()
    {
        return mapWidth;
    }

    public float getGravity()
    {
        return gravity;
    }
}