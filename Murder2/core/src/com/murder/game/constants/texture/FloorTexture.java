package com.murder.game.constants.texture;

/**
 * Floor texture implementation of BaseTexture. Used to load in a floor tile.
 */
public class FloorTexture extends BaseTexture
{
    private static final FloorTexture FLOOR_TEXTURE_LOADER = new FloorTexture();

    public static final String TEXTURE_SOURCE_FILES = "assets-raw/images/floorTiles";
    public static final String TEXTURE_PACK_NAME = "floorTiles.pack";
    public static final String TEXTURE_PACK_LOCATION = "images/floorTiles/";

    private static final String FULL_TEXTURE_PACK_NAME = TEXTURE_PACK_LOCATION + TEXTURE_PACK_NAME;
    private static final String[] TEXTURE_NAMES = { 
        "FloorTexture1",
        "FloorTexture2",
        "FloorTexture3",
        "FloorTexture4",
        "FloorTexture5"
    };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private FloorTexture()
    {
        super();
    }

    public static BaseTexture getFloorTextureLoader()
    {
        return FLOOR_TEXTURE_LOADER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getTexturePackName()
    {
        return FULL_TEXTURE_PACK_NAME;
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
