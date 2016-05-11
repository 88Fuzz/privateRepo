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
//        config.width = 2560;
//        config.height = 1440;

//        config.width = 1920;
//        config.height = 1080;

//        config.width = 1600;
//        config.height = 900;

//        config.width = 1280;
//        config.height = 800;
        
//        config.width = 800;
//        config.height = 480;

//        config.width = 600;
//        config.height = 400;
        
        config.width = 480;
        config.height = 320;
        
        new LwjglApplication(new MurderMainMain(), config);
//         new LwjglApplication(new MurderMain(), config);
        // new LwjglApplication(new Application(), config);
    }
}