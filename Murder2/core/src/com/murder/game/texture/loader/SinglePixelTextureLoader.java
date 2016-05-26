package com.murder.game.texture.loader;

public class SinglePixelTextureLoader extends MiscTextureLoader
{
    private static final SinglePixelTextureLoader SINGLE_PIXEL_TEXTURE_LOADER = new SinglePixelTextureLoader();

    private static final String[] TEXTURE_NAMES = { "SinglePixel" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private SinglePixelTextureLoader()
    {
        super();
    }

    public static BaseTextureLoader getSinglePixelTextureLoader()
    {
        return SINGLE_PIXEL_TEXTURE_LOADER;
    }

    @Override
    protected String[] getAvailableRegions()
    {
        return TEXTURE_NAMES;
    }
}