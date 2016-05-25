package com.murder.game.constants.texture;

public class ExitTexture extends MiscTexture
{
    private static final ExitTexture EXIT_TEXTURE_LOADER = new ExitTexture();

    private static final String[] TEXTURE_NAMES = { "ExitTexture" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private ExitTexture()
    {
        super();
    }

    public static BaseTexture getKeyTextureLoader()
    {
        return EXIT_TEXTURE_LOADER;
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