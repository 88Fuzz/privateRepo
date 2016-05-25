package com.murder.game.constants.texture;

/**
 * Class to load and select circle textures
 */
public class CircleTexture extends BaseTexture
{
    private static final CircleTexture CIRCLE_TEXTURE_LOADER = new CircleTexture();

    public static final String TEXTURE_SOURCE_FILES = "assets-raw/images/circleTiles";
    public static final String TEXTURE_PACK_NAME = "circleTiles.pack";
    public static final String TEXTURE_PACK_LOCATION = "images/circleTiles/";

    private static final String FULL_TEXTURE_PACK_NAME = TEXTURE_PACK_LOCATION + TEXTURE_PACK_NAME;
    private static final String[] TEXTURE_NAMES = { "CircleTexture1", "CircleTexture2", "CircleTexture3" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private CircleTexture()
    {
        super();
    }

    public static BaseTexture getCircleTextureLoader()
    {
        return CIRCLE_TEXTURE_LOADER;
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