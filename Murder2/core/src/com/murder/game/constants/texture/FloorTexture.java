package com.murder.game.constants.texture;

/**
 * Floor texture implementation of BaseTexture. Used to load in a floor tile.
 */
public class FloorTexture extends BaseTexture
{
    private static final String TEXTURE_PACK_NAME = "images/floorTiles/floorTiles.pack";
    private static final String[] TEXTURE_NAMES = { 
        "FloorTexture1",
        "FloorTexture2",
        "FloorTexture3",
        "FloorTexture4",
        "FloorTexture5"
//        "FloorTexture6"
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTexturePackName()
    {
        return TEXTURE_PACK_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] getAvailableRegions()
    {
        return TEXTURE_NAMES;
    }

}
