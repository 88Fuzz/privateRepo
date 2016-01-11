package com.pixel.wars.game.data;

import java.util.List;

import com.pixel.wars.game.drawing.Pixel;

public class Pixels
{
    private final List<Pixel> pixels;
    private final int height;
    private final int width;
    private final int size;

    public Pixels(final List<Pixel> pixels, final int height, final int width, final int size)
    {
        this.pixels = pixels;
        this.height = height;
        this.width = width;
        this.size = size;
    }

    public List<Pixel> getPixels()
    {
        return pixels;
    }

    public int getHeight()
    {
        return height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getSize()
    {
        return size;
    }
}