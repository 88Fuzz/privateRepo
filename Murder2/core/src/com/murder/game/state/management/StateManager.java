package com.murder.game.state.management;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.state.GameState;
import com.murder.game.state.State;
import com.murder.game.state.TextState;

public class StateManager
{
    private static final Map<StateId, State> STATE_MAP = new HashMap<StateId, State>();

    private final List<PendingAction> pendingActions;

    public StateManager(final TextureManager textureManager)
    {
        pendingActions = new LinkedList<PendingAction>();

        STATE_MAP.put(StateId.GAME_STATE, new GameState(this));
        STATE_MAP.put(StateId.TEXT_STATE, new TextState(this));
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

    public void addActions(final List<PendingAction> actions)
    {
        for(final PendingAction action: actions)
        {
            addAction(action);
        }
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