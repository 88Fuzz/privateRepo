package com.squared.space.game.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class StateManager
{
    private static final Map<StateId, State> STATE_MAP = new HashMap<StateId, State>();

    public enum StateId
    {
        MENU_STATE, SCENE_STATE, TEST_STATE, TEXT_STATE, NONE;
    }

    public enum StateAction
    {
        PUSH, POP;
    }

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

    public StateManager(final TextureAtlas atlas)
    {
        pendingActions = new LinkedList<PendingAction>();

        final BitmapFont font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
        font.setColor(Color.BLACK);
        STATE_MAP.put(StateId.TEXT_STATE, new TextState(this, font,
                new Vector2(Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 10),
                atlas));
        STATE_MAP.put(StateId.MENU_STATE, new MenuState(this));
        STATE_MAP.put(StateId.TEST_STATE, new TestState(this));
        STATE_MAP.put(StateId.SCENE_STATE, new SceneState(this));
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