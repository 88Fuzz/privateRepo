package com.murder.game.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.murder.game.drawing.TextureManager;

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
        POP;
    }

    // TODO change this to use .with<paramater> and not have multiple
    // constructors
    public class PendingAction
    {
        private final StateAction action;
        private final StateId id;
        private final String stateConfig;

        public PendingAction(final StateAction action)
        {
            this(action, StateId.NONE);
        }

        public PendingAction(final StateAction action, final StateId id)
        {
            this(action, id, "");
        }

        public PendingAction(final StateAction action, final StateId id, final String stateConfig)
        {
            this.action = action;
            this.id = id;
            this.stateConfig = stateConfig;
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
//        STATE_MAP.put(StateId.TEXT_STATE, new TextState(this));
        // STATE_MAP.put(StateId.MENU_STATE, new MenuState(this));
    }

    public void addAction(final StateAction action)
    {
        pendingActions.add(new PendingAction(action));
    }

    public void addAction(final StateAction action, final StateId id)
    {
        pendingActions.add(new PendingAction(action, id));
    }

    public void addAction(final StateAction action, final StateId id, final String stateConfig)
    {
        pendingActions.add(new PendingAction(action, id, stateConfig));
    }

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