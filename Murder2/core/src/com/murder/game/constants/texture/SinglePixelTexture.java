package com.murder.game.constants.texture;

public class SinglePixelTexture extends MiscTexture
{
    private static final SinglePixelTexture SINGLE_PIXEL_TEXTURE_LOADER = new SinglePixelTexture();

    private static final String[] TEXTURE_NAMES = { "SinglePixel" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private SinglePixelTexture()
    {
        super();
    }

    public static BaseTexture getCircleTextureLoader()
    {
        return SINGLE_PIXEL_TEXTURE_LOADER;
    }

    @Override
    protected String[] getAvailableRegions()
    {
        return TEXTURE_NAMES;
    }
}