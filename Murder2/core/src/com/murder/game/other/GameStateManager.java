package com.murder.game.other;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Application app;

    private Stack<GameState> states;

    public enum State {
        SPLASH,
        PLAY,
        DUNGEON,
        FILTER,
        LIGHTS,
        JOINTS,
        CONTACT,
        LASER
    }

    public GameStateManager(final Application app) {
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.CONTACT);
    }

    public Application application() {
        return app;
    }

    public void update(float delta) {
        states.peek().update(delta);
    }

    public void render() {
        states.peek().render();
    }

    public void dispose() {
        for(GameState gs : states) {
//            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h) {
//        if(!states.isEmpty())
//            states.peek().resize(w, h);
    }

    public void setState(State state) {
//        if(states.size() >= 1) {
//            states.pop().dispose();
//        }
        states.push(getState(state));
    }

    private GameState getState(State state) {
        switch(state) {
            case LIGHTS: return new TutLightsState(this);
        }
        return null;
    }
}