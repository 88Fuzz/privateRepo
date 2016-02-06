package com.squared.space.game;

import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.drawing.WorldRenderer;
import com.squared.space.game.state.SceneState;
import com.squared.space.game.state.State;
import com.squared.space.game.state.StateManager;
import com.squared.space.game.state.StateManager.PendingAction;
import com.squared.space.game.state.StateManager.StateId;
import com.squared.space.game.state.TestState;
import com.squared.space.game.state.TextState;
import com.squared.space.game.state.config.SceneStateConfig;
import com.squared.space.game.state.config.TextStateConfig;

public class SquaredSpaceMain extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    private Stack<State> stateStack;
    private WorldRenderer worldRenderer;
    private StateManager stateManager;
    private SceneStateConfig sceneStateConfig;
    private float timeSinceLastUpdate;

    @Override
    public void create()
    {
        final AssetManager assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        stateStack = new Stack<State>();
        worldRenderer = new WorldRenderer();
        sceneStateConfig = new SceneStateConfig(worldRenderer, assMan);
        stateManager = new StateManager((TextureAtlas) assMan.get(TextureConstants.TILE_TEXTURES));
        timeSinceLastUpdate = 0;

        //IntroScene
//        TestState testState = (TestState) stateManager.getState(StateId.TEST_STATE);
//        testState.init(sceneStateConfig.getContext(SceneStateConfig.TEST_SCENE));
//        stateStack.add(testState);

        //IntroScene
        SceneState sceneState = (SceneState) stateManager.getState(StateId.SCENE_STATE);
        sceneState.init(worldRenderer, sceneStateConfig.getContext(SceneStateConfig.INTRO_SCENE));
        stateStack.add(sceneState);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void pause()
    {
        for(final State state: stateStack)
        {
            state.pause();
        }
    }

    @Override
    public void resume()
    {
        for(final State state: stateStack)
        {
            state.resume();
        }
    }

    @Override
    public void resize(final int width, final int height)
    {
        for(final State state: stateStack)
        {
            state.resize(width, height);
        }
    }

    @Override
    public void render()
    {
        updateAndRenderStates();
        processStateActions();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        for(final State state: stateStack)
        {
            state.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        for(final State state: stateStack)
        {
            if(state.keyDown(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        for(final State state: stateStack)
        {
            if(state.keyUp(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        for(final State state: stateStack)
        {
            if(state.unicodeEntered(character))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        // TODO Auto-generated method stub
        return false;
    }

    private void updateAndRenderStates()
    {
        float dt = Gdx.graphics.getDeltaTime();

        timeSinceLastUpdate += dt;
        while(timeSinceLastUpdate > TIMEPERFRAME)
        {
            worldRenderer.clearScreen();

            timeSinceLastUpdate -= TIMEPERFRAME;
            for(final State state: stateStack)
            {
                state.update(dt);
                state.render(worldRenderer);
            }
        }
    }

    private void processStateActions()
    {
        for(final PendingAction pendingAction: stateManager.getPendingActions())
        {
            switch(pendingAction.getAction())
            {
            case PUSH:
                final State state;
                if((state = stateManager.getState(pendingAction.getId())) != null)
                {
                    initState(state, pendingAction.getStateConfig());
                    // TODO initState(state);
                    stateStack.push(state);
                }
                break;
            case POP:
                stateStack.pop();
                break;
            }
        }
        stateManager.getPendingActions().clear();
    }

    private void initState(final State state, final String stateConfig)
    {
        if(state instanceof TextState)
        {
            ((TextState) state).init(TextStateConfig.getTexts(stateConfig));
        }
    }
}