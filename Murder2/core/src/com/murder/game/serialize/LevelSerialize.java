package com.murder.game.serialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.level.Level;
import com.murder.game.state.management.PendingAction;

public class LevelSerialize
{
    private static final String LEVEL = "level";
    private static final String PLAYER = "player";
    private static final String MOBS = "mobs";
    private static final String STATE_ACTIONS = "stateActions";

    private final Level level;
    private final Actor player;
    private final List<Mob> mobs;
    private List<PendingAction> stateActions;

    @JsonCreator
    public LevelSerialize(@JsonProperty(LEVEL) final Level level, @JsonProperty(PLAYER) final Actor player, @JsonProperty(MOBS) final List<Mob> mobs,
            @JsonProperty(STATE_ACTIONS) List<PendingAction> stateActions)
    {
        this.level = level;
        this.player = player;
        this.mobs = mobs;
        this.stateActions = stateActions;
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

    public List<PendingAction> getStateActions()
    {
        return stateActions;
    }
}