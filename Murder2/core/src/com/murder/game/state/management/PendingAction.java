package com.murder.game.state.management;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PendingAction
{
    private static final String STATE_ACTION = "stateACtion";
    private static final String STATE_ID = "stateId";
    private static final String STATE_CONFIG = "stateConfig";

    private StateAction action;
    private StateId id;
    private String stateConfig;

    public PendingAction()
    {
        this(StateAction.NONE, StateId.NONE, "");
    }

    @JsonCreator
    public PendingAction(@JsonProperty(STATE_ACTION) final StateAction action, @JsonProperty(STATE_ID) final StateId id,
            @JsonProperty(STATE_CONFIG) final String stateConfig)
    {
        this.action = action;
        this.id = id;
        this.stateConfig = stateConfig;
    }

    public PendingAction withAction(final StateAction action)
    {
        this.action = action;
        return this;
    }

    public PendingAction withStateId(final StateId id)
    {
        this.id = id;
        return this;
    }

    public PendingAction withStateConfig(final String stateConfig)
    {
        this.stateConfig = stateConfig;
        return this;
    }

    public StateAction getAction()
    {
        return action;
    }

    public StateId getId()
    {
        return id;
    }

    public String getStateConfig()
    {
        return stateConfig;
    }
}