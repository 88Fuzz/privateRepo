package com.murder.game.constants.texture;

/**
 * Holds the collection of one off textures, like the single pixel and key.
 */
public abstract class MiscTexture extends BaseTexture
{
    public static final String TEXTURE_SOURCE_FILES = "assets-raw/images/miscTiles";
    public static final String TEXTURE_PACK_NAME = "miscTiles.pack";
    public static final String TEXTURE_PACK_LOCATION = "images/miscTiles/";

    private static final String FULL_TEXTURE_PACK_NAME = TEXTURE_PACK_LOCATION + TEXTURE_PACK_NAME;

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
    protected abstract String[] getAvailableRegions();
}