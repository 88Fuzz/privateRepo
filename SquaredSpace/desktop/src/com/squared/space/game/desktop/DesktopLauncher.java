package com.squared.space.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.squared.space.game.SquaredSpaceMain;

public class DesktopLauncher
{
    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = false;

    public static void main(final String[] arg)
    {
        if(rebuildAtlas)
        {
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;
            TexturePacker2.process(settings, "assets-raw/images", "../core/assets/images", "tiles.pack");
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SquaredSpace";
        config.useGL30 = false;
        config.width = 800;
        config.height = 600;
        new LwjglApplication(new SquaredSpaceMain(), config);
    }
}