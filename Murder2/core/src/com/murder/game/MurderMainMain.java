package com.murder.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.Text;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;
import com.murder.game.state.GameState;
import com.murder.game.state.State;
import com.murder.game.state.StateManager;
import com.murder.game.state.StateManager.PendingAction;
import com.murder.game.state.StateManager.StateId;
import com.murder.game.state.TextState;

public class MurderMainMain extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 30.0f;

    private Stack<State> stateStack;
    private TextureManager textureManager;
    private FontManager fontManager;
    private WorldRenderer worldRenderer;
    private StateManager stateManager;
    private float timeSinceLastUpdate;
    // private TextureAtlas textureAtlas;
    // private FontGenerator fontGenerator;

    @Override
    public void create()
    {
        // final AssetManager assMan = new AssetManager();
        textureManager = new TextureManager();
        fontManager = new FontManager();
        // assMan.load(TextureType.TILE_TEXTURES, TextureAtlas.class);
        // assMan.finishLoading();
        stateStack = new Stack<State>();
        worldRenderer = new WorldRenderer();
        // textureAtlas = assMan.get(TextureType.TILE_TEXTURES);
        stateManager = new StateManager(textureManager);
        // fontGenerator = new FontGenerator();
        timeSinceLastUpdate = 0;

        // final GameState gameState = (GameState)
        // stateManager.getState(StateId.GAME_STATE);
        //
        // gameState.init(worldRenderer, textureManager, fontManager,
        // "Level01");
        // stateStack.push(gameState);

        final TextState textState = (TextState) stateManager.getState(StateId.TEXT_STATE);
        textState.init(fontManager, "Text1");
        stateStack.push(textState);

        // final List<Text> drawableTexts = new LinkedList<Text>();
        // drawableTexts.add(new Text(new MyVector2(100,100),
        // FontType.BLOODSUCKERS_48, "BLOODSUCKERS", 0));
        // drawableTexts.add(new Text(new MyVector2(100,50),
        // FontType.COLDNIGHT_48, "COLDNIGHT", 0));
        // drawableTexts.add(new Text(new MyVector2(100,10), FontType.EDOSZ_48,
        // "EDOSZ", 0));
        // drawableTexts.add(new Text(new MyVector2(100,-10),
        // FontType.GHASTLYPANIC_48, "GHASTLYPANIC", 0));
        // drawableTexts.add(new Text(new MyVector2(100,-70),
        // FontType.GYPSYCURSE_48, "GYPSYCURSE", 0));
        // drawableTexts.add(new Text(new MyVector2(100,-130),
        // FontType.SUBTLE_48, "SUBTLE", 0));
        // drawableTexts.add(new Text(new MyVector2(100,-180),
        // FontType.SUNSET_48, "SUNSET", 0));
        // drawableTexts.add(new Text(new MyVector2(100,-280),
        // FontType.TEQUILA_48, "TEQUILA", 0));
        // final TextState textState = (TextState)
        // stateManager.getState(StateId.TEXT_STATE);
        // textState.init(fontManager, drawableTexts);
        // stateStack.push(textState);

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
        fontManager.dispose();
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
        final Vector3 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

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
        final Vector3 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

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
        final Vector3 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

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
        final Vector3 touchPosition = worldRenderer.getWorldCoordinates(screenX, screenY);

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

            for(final State state: stateStack)
            {
                state.update(dt);
                // state.render(worldRenderer);
            }
            // worldRenderer.renderGUI();
            timeSinceLastUpdate -= TIMEPERFRAME;
        }

        worldRenderer.clearScreen();
        for(final State state: stateStack)
        {
            state.render(worldRenderer);
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
            ((GameState) state).init(worldRenderer, textureManager, fontManager, stateConfig);
        }
        // else if(state instanceof TextState)
        // {
        // ((TextState) state).init(fontGenerator,
        // levelGenerator.getTexts(stateConfig));
        // }
    }
}