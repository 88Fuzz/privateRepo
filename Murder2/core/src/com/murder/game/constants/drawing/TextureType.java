package com.murder.game.constants.drawing;

public enum TextureType
{
    SINGLE_PIXEL_TEXTURE("images/SinglePixel.png"),
    CIRCLE_TEXTURE("images/CircleTexture.png");

    private final String fileName;

    private TextureType(final String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}