package com.murder.game.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.murder.game.drawing.manager.TextureManager;

public class StateManager
{
    private static final Map<StateId, State> STATE_MAP = new HashMap<StateId, State>();

    public enum StateId
    {
        MENU_STATE,
        GAME_STATE,
        TEXT_STATE,
        NONE;
    }

    public enum StateAction
    {
        PUSH,
        POP,
        NONE;
    }

    public static class PendingAction
    {
        private StateAction action = StateAction.NONE;
        private StateId id = StateId.NONE;
        private String stateConfig;

        public PendingAction withAction(final StateAction action)
        {
            this.action = action;
            return this;
        }

        public PendingAction withStatId(final StateId id)
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

    private final List<PendingAction> pendingActions;

    public StateManager(final TextureManager textureManager)
    {
        pendingActions = new LinkedList<PendingAction>();

        STATE_MAP.put(StateId.GAME_STATE, new GameState(this));
        // STATE_MAP.put(StateId.TEXT_STATE, new TextState(this));
        // STATE_MAP.put(StateId.MENU_STATE, new MenuState(this));
    }

    // public void addAction(final StateAction action)
    // {
    // pendingActions.add(new PendingAction(action));
    // }
    //
    // public void addAction(final StateAction action, final StateId id)
    // {
    // pendingActions.add(new PendingAction(action, id));
    // }
    //
    // public void addAction(final StateAction action, final StateId id, final
    // String stateConfig)
    // {
    // pendingActions.add(new PendingAction(action, id, stateConfig));
    // }

    public void addAction(final PendingAction action)
    {
        pendingActions.add(action);
    }

    public List<PendingAction> getPendingActions()
    {
        return pendingActions;
    }

    public State getState(final StateId id)
    {
        return STATE_MAP.get(id);
    }
}