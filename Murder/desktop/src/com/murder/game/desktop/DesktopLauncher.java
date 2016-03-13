package com.murder.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.murder.game.MurderMain;

public class DesktopLauncher
{
    private static boolean rebuildAtlas = false;
    private static boolean drawDebugOutline = false;

    public static void main(final String[] arg)
    {
        if(rebuildAtlas)
        {
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;
            TexturePacker2.process(settings, "assets-raw/images", "../android/assets/images", "tiles.pack");
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Murder";
        config.useGL30 = false;
        config.width = 1600;
        config.height = 900;

        new LwjglApplication(new MurderMain(), config);
    }
}
