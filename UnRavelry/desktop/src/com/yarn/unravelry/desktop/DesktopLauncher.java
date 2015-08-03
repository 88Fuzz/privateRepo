package com.yarn.unravelry.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.yarn.unravelry.states.GameState;

public class DesktopLauncher
{
    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = false;

    public static void main(String[] arg)
    {
        if(rebuildAtlas)
        {
            Settings settings = new Settings();
            settings.maxWidth = 2048;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;
            TexturePacker2.process(settings, "assets/images", "../android/assets/images", "tiles.pack");
        }
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "UnRavel";
        config.useGL30 = false;
        config.width = 800;
        config.height = 600;

        new LwjglApplication(new GameState(), config);
    }
}
