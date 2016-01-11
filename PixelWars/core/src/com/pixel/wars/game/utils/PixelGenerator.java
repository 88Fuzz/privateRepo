package com.pixel.wars.game.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pixel.wars.game.config.PixelConfig;
import com.pixel.wars.game.data.Pixels;
import com.pixel.wars.game.drawing.Pixel;
import com.pixel.wars.game.drawing.Pixel.Team;

public class PixelGenerator
{
    public static Pixels generatePixels(final PixelConfig pixelConfig, final TextureAtlas atlas, final int height, final float percentPlayer)
    {
        Team team = Team.PLAYER;

        // TODO this truncation will lead to some amount of pixels not fitting
        // to the end of the screen
        final int tileSize = Gdx.graphics.getHeight() / height;
        final int width = (int) (Gdx.graphics.getWidth() / tileSize);
        final float teamSwitch = (width * height) * percentPlayer;
        final List<Pixel> pixels = new ArrayList<Pixel>(width * height);

        int count = 0;
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {

                if(count++ > teamSwitch)
                {
                    team = Team.OTHER;
                }

                // TODO configure health based on the team
                pixels.add(new Pixel(atlas, pixelConfig, i, j, tileSize, team));
            }
        }

        return new Pixels(pixels, height, width, tileSize);
    }
}