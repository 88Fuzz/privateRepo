package com.murder.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.murder.game.MurderMain;
import com.murder.game.MurderMainMain;
import com.murder.game.texture.loader.CircleTextureLoader;
import com.murder.game.texture.loader.FloorTextureLoader;
import com.murder.game.texture.loader.MiscTextureLoader;

public class DesktopLauncher
{
    private static final String BASE_DIRECTORY = "../android/assets/";
    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = false;

    public static void main(final String[] arg)
    {
        if(rebuildAtlas)
        {
            final Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;

            TexturePacker2.process(settings, FloorTextureLoader.TEXTURE_SOURCE_FILES, BASE_DIRECTORY + FloorTextureLoader.TEXTURE_PACK_LOCATION,
                    FloorTextureLoader.TEXTURE_PACK_NAME);

            TexturePacker2.process(settings, CircleTextureLoader.TEXTURE_SOURCE_FILES, BASE_DIRECTORY + CircleTextureLoader.TEXTURE_PACK_LOCATION,
                    CircleTextureLoader.TEXTURE_PACK_NAME);

            TexturePacker2.process(settings, MiscTextureLoader.TEXTURE_SOURCE_FILES, BASE_DIRECTORY + MiscTextureLoader.TEXTURE_PACK_LOCATION,
                    MiscTextureLoader.TEXTURE_PACK_NAME);
        }

        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Murder";
        config.useGL30 = false;
        // config.width = 2560;
        // config.height = 1440;

        // config.width = 1920;
        // config.height = 1080;

        // TODO text is still fucked up by showing up off screen
        config.width = 1600;
        config.height = 900;

        // config.width = 1280;
        // config.height = 800;

        // config.width = 800;
        // config.height = 480;

        // config.width = 600;
        // config.height = 400;

        // config.width = 480;
        // config.height = 320;

        new LwjglApplication(new MurderMainMain(), config);
        // new LwjglApplication(new MurderMain(), config);
        // new LwjglApplication(new Application(), config);
    }
}