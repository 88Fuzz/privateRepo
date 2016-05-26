package com.murder.game.texture.loader;

public class ExitTextureLoader extends MiscTextureLoader
{
    private static final ExitTextureLoader EXIT_TEXTURE_LOADER = new ExitTextureLoader();

    private static final String[] TEXTURE_NAMES = { "ExitTexture" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private ExitTextureLoader()
    {
        super();
    }

    public static BaseTextureLoader getExitTextureLoader()
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