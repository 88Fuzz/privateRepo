package com.libgdx.airplane.game.utils;

/**
 * 
 * Class used to hold information about the current map.
 * 
 */
public class MapDetails
{
    private final int mapWidth;

    public MapDetails(final int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public int getMapWidth()
    {
        return mapWidth;
    }
}