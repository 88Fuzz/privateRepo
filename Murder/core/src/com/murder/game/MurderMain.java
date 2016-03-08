package com.murder.game;

import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.serial.LevelGenerator;
import com.murder.game.level.serial.LevelSerialize;
import com.murder.game.state.GameState;
import com.murder.game.state.State;
import com.murder.game.state.StateManager;
import com.murder.game.state.StateManager.PendingAction;
import com.murder.game.state.StateManager.StateId;

public class MurderMain extends ApplicationAdapter implements InputProcessor
{
    // TODO there is probably a bunch of shit that needs to be disposed. Like
    // TextureAtlas, renderers??
    private static final float TIMEPERFRAME = 1.0f / 30.0f;

    private Stack<State> stateStack;
    private WorldRenderer worldRenderer;
    private StateManager stateManager;
    private float timeSinceLastUpdate;
    private TextureAtlas textureAtlas;
    private LevelGenerator levelGenerator;

    @Override
    public void create()
    {
        final AssetManager assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        stateStack = new Stack<State>();
        worldRenderer = new WorldRenderer();
        textureAtlas = assMan.get(TextureConstants.TILE_TEXTURES);
        stateManager = new StateManager(textureAtlas);
        levelGenerator = new LevelGenerator();
        timeSinceLastUpdate = 0;

        final GameState gameState = (GameState) stateManager.getState(StateId.GAME_STATE);
        final LevelSerialize levelSerialize = levelGenerator.getLevel("Level01");

        gameState.init(worldRenderer, levelSerialize, textureAtlas);
        stateStack.push(gameState);

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
    public boolean keyDown(final int keycode)
    {
        for(final State state: stateStack)
        {
            if(state.keyDown(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(final int keycode)
    {
        for(final State state: stateStack)
        {
            if(state.keyUp(keycode))
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(final char character)
    {
        for(final State state: stateStack)
        {
            if(state.unicodeEntered(character))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        final Vector2 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

        for(final State state: stateStack)
        {
            if(state.touchDown((int) touchPosition.x, (int) touchPosition.y, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        final Vector2 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

        for(final State state: stateStack)
        {
            if(state.touchUp((int) touchPosition.x, (int) touchPosition.y, pointer, button))
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        final Vector2 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

        for(final State state: stateStack)
        {
            if(state.touchDragged((int) touchPosition.x, (int) touchPosition.y, pointer))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        final Vector2 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

        for(final State state: stateStack)
        {
            if(state.mouseMoved((int) touchPosition.x, (int) touchPosition.y))
                return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(final int amount)
    {
        for(final State state: stateStack)
        {
            if(state.mouseScrolled(amount))
                return true;
        }
        return false;
    }

    private void updateAndRenderStates()
    {
        float dt = Gdx.graphics.getDeltaTime();

        // TODO this needs to be reworked. MASIVELY, Fix timestamp shouldn't be
        // used, if the framerate drops below 60, then anything that moves is
        // "jumped"
        timeSinceLastUpdate += dt;
        while(timeSinceLastUpdate > TIMEPERFRAME)
        {
            worldRenderer.clearScreen();

            timeSinceLastUpdate -= TIMEPERFRAME;
            for(final State state: stateStack)
            {
                state.update(dt);
                // state.render(worldRenderer);
            }
            // worldRenderer.renderGUI();
        }
        for(final State state: stateStack)
        {
            state.update(dt);
            state.render(worldRenderer);
        }
        worldRenderer.renderGUI();
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
                    stateStack.push(state);
                }
                break;
            case POP:
                if(!stateStack.isEmpty())
                    stateStack.pop();
                break;
            }
        }
        stateManager.getPendingActions().clear();
    }

    private void initState(final State state, final String stateConfig)
    {
        if(state instanceof GameState)
        {
            ((GameState) state).init(worldRenderer, levelGenerator.getLevel(stateConfig), textureAtlas);
        }
    }
}