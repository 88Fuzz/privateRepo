package com.murder.game;

import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.drawing.TextureManager;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.generator.LevelGenerator;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.state.GameState;
import com.murder.game.state.State;
import com.murder.game.state.StateManager;
import com.murder.game.state.StateManager.PendingAction;
import com.murder.game.state.StateManager.StateId;

public class MurderMainMain extends ApplicationAdapter implements InputProcessor
{
    private static final boolean ALLOW_SLEEP = true;
    private static final float TIMEPERFRAME = 1.0f / 30.0f;

    private Stack<State> stateStack;
    private TextureManager textureManager;
    private WorldRenderer worldRenderer;
    // TODO the physicsWorld needs to be reset after each level change.
    private World physicsWorld;
    private StateManager stateManager;
    private float timeSinceLastUpdate;
    // private TextureAtlas textureAtlas;
    private LevelGenerator levelGenerator;
    // private FontGenerator fontGenerator;

    @Override
    public void create()
    {
        // final AssetManager assMan = new AssetManager();
        textureManager = new TextureManager();
        // assMan.load(TextureType.TILE_TEXTURES, TextureAtlas.class);
        // assMan.finishLoading();
        stateStack = new Stack<State>();
        physicsWorld = new World(new Vector2(0, 0), ALLOW_SLEEP);
        worldRenderer = new WorldRenderer(physicsWorld);
        // textureAtlas = assMan.get(TextureType.TILE_TEXTURES);
        stateManager = new StateManager(textureManager);
        levelGenerator = new LevelGenerator();
        // fontGenerator = new FontGenerator();
        timeSinceLastUpdate = 0;

        final GameState gameState = (GameState) stateManager.getState(StateId.GAME_STATE);
        final LevelSerialize levelSerialize = levelGenerator.getLevel("Level01");

        // gameState.init(worldRenderer, levelSerialize, textureAtlas);
        gameState.init(physicsWorld, worldRenderer, textureManager, levelSerialize);
        stateStack.push(gameState);

        // assMan.dispose();
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
        worldRenderer.dispose();
        textureManager.dispose();
        // fontGenerator.dispose();
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
        if(Input.Keys.ESCAPE == keycode)
            Gdx.app.exit();

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
            physicsWorld.step(TIMEPERFRAME, 6, 2);

            timeSinceLastUpdate -= TIMEPERFRAME;
            for(final State state: stateStack)
            {
                state.update(dt);
                // state.render(worldRenderer);
            }
            // worldRenderer.renderGUI();
        }

        worldRenderer.render();
        for(final State state: stateStack)
        {
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
            // ((GameState) state).init(worldRenderer,
            // levelGenerator.getLevel(stateConfig), textureAtlas);
            ((GameState) state).init(physicsWorld, worldRenderer, textureManager, levelGenerator.getLevel(stateConfig));
        }
        // else if(state instanceof TextState)
        // {
        // ((TextState) state).init(fontGenerator,
        // levelGenerator.getTexts(stateConfig));
        // }
    }
}