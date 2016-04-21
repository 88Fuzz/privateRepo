package com.murder.game.serialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Mob;
import com.murder.game.level.Level;

public class LevelSerialize
{
    private static final String LEVEL = "level";
    private static final String PLAYER = "player";
    private static final String MOBS = "mobs";

    private final Level level;
    private final Actor player;
    private final List<Mob> mobs;

    @JsonCreator
    public LevelSerialize(@JsonProperty(LEVEL) final Level level, @JsonProperty(PLAYER) final Actor player, @JsonProperty(MOBS) final List<Mob> mobs)
    {
        this.level = level;
        this.player = player;
        this.mobs = mobs;
    }

    public Level getLevel()
    {
        return level;
    }

    public Actor getPlayer()
    {
        return player;
    }

    public List<Mob> getMobs()
    {
        return mobs;
    }
}