package com.murder.game.serialize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Actor;
import com.murder.game.level.Level;

public class LevelSerialize
{
    private static final String LEVEL = "level";
    private static final String PLAYER = "player";

    private final Level level;
    private final Actor player;

    @JsonCreator
    public LevelSerialize(@JsonProperty(LEVEL) final Level level, @JsonProperty(PLAYER) final Actor player)
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