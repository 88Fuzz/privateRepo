package com.murder.game.level.serial;

import com.murder.game.drawing.Actor;
import com.murder.game.level.Level;

public class LevelSerialize
{
    private final Level level;
    private final Actor player;

    public LevelSerialize(final Level level, final Actor player)
    {
        this.level = level;
        this.player = player;
    }

    public Level getLevel()
    {
        return level;
    }

    public Actor getPlayer()
    {
        return player;
    }
}