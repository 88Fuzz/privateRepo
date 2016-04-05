package com.murder.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.murder.game.MurderMain;
import com.murder.game.MurderMainMain;

public class DesktopLauncher
{
    public static void main(final String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Murder";
        config.useGL30 = false;
        config.width = 1600;
        config.height = 900;

        new LwjglApplication(new MurderMainMain(), config);
//         new LwjglApplication(new MurderMain(), config);
        // new LwjglApplication(new Application(), config);
    }
}
