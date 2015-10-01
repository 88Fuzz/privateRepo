package com.libgdx.airplane.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;
import com.libgdx.airplane.game.Game;

public class DesktopLauncher {
    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = false;

	public static void main (String[] arg) {
	    if (rebuildAtlas) {
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.debug = drawDebugOutline;
            TexturePacker2.process(settings, "assets-raw/images", "../android/assets/images", "tiles.pack");
        }

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Airplanes";
        config.useGL30 = false;
        config.width = 1920;
        config.height = 1080;
		new LwjglApplication(new Game(), config);
	}
}
