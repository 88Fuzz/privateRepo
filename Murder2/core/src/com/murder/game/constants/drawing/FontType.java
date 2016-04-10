package com.murder.game.constants.drawing;

public enum FontType
{
    BLUEBIRD_12("font/Bluebird.otf", 12),
    BLUEBIRD_48("font/Bluebird.otf", 48);

    public static final String ARIAL_15 = "images/arial-15.fnt";

    private final String fileName;
    private final int size;

    private FontType(final String fileName, final int size)
    {
        this.fileName = fileName;
        this.size = size;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getSize()
    {
        return size;
    }
}