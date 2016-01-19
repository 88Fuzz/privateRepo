package com.squared.space.game.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StateManager
{
    private static final Map<StateId, State> STATE_MAP = new HashMap<StateId, State>();

    public enum StateId
    {
        TEXT_STATE,
        MENU_STATE,
        TEST_STATE,
        NONE;
    }

    public enum StateAction
    {
        PUSH,
        POP;
    }

    public class PendingAction
    {
        private final StateAction action;
        private final StateId id;

        public PendingAction(final StateAction action)
        {
            this(action, StateId.NONE);
        }

        public PendingAction(final StateAction action, final StateId id)
        {
            this.action = action;
            this.id = id;
        }

        public StateAction getAction()
        {
            return action;
        }

        public StateId getId()
        {
            return id;
        }
    }

    private final List<PendingAction> pendingActions;

    public StateManager()
    {
        pendingActions = new LinkedList<PendingAction>();
        STATE_MAP.put(StateId.TEXT_STATE, new TextState(this));
        STATE_MAP.put(StateId.MENU_STATE, new MenuState(this));
        STATE_MAP.put(StateId.TEST_STATE, new TestState(this));
    }

    public void addAction(final StateAction action)
    {
        pendingActions.add(new PendingAction(action));
    }

    public void addAction(final StateAction action, final StateId id)
    {
        pendingActions.add(new PendingAction(action, id));
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