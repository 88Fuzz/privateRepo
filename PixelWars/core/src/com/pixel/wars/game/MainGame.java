package com.pixel.wars.game;

import java.util.Stack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.pixel.wars.game.config.PixelConfig;
import com.pixel.wars.game.data.Pixels;
import com.pixel.wars.game.drawing.Pixel.Team;
import com.pixel.wars.game.rendering.TextureConstants;
import com.pixel.wars.game.rendering.WorldRenderer;
import com.pixel.wars.game.state.BattleIntroState;
import com.pixel.wars.game.state.BattleState;
import com.pixel.wars.game.state.State;
import com.pixel.wars.game.state.StateManager;
import com.pixel.wars.game.state.StateManager.PendingAction;
import com.pixel.wars.game.state.StateManager.StateAction;
import com.pixel.wars.game.state.StateManager.StateId;
import com.pixel.wars.game.utils.PixelGenerator;

public class MainGame extends ApplicationAdapter
{
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    // TODO make these values configurable
    private static final int HEIGHT = 50;

    private TextureAtlas atlas;
    private StateManager stateManager;
    private WorldRenderer worldRenderer;
    private Stack<State> stateStack;
    private float timeSinceLastUpdate;
    private int level;

    @Override
    public void create()
    {
        level = 1;
        worldRenderer = new WorldRenderer();
        stateManager = new StateManager();

        final AssetManager assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        stateStack = new Stack<State>();
        final BattleState battleState = (BattleState) stateManager.getState(StateId.BATTLE_STATE);
        initBattleState(battleState);
        final BattleIntroState battleIntroState = (BattleIntroState) stateManager.getState(StateId.BATTLE_INTRO);
        initBattleIntroState(battleIntroState);
        stateStack.add(battleIntroState);
        // stateStack.add(battleState);
        // TODO insert default menu state
        timeSinceLastUpdate = 0;
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
                // special logic for the battle state, this is dumb and should
                // be fixed at some point.
                if(state instanceof BattleState)
                {
                    final BattleState battleState = (BattleState) state;
                    if(battleState.isFinished())
                    {
                        if(battleState.getWinner() == Team.PLAYER)
                        {
                            level++;
                        }
                        stateManager.addAction(StateAction.POP);
                        stateManager.addAction(StateAction.PUSH, StateId.BATTLE_INTRO);
                        // initBattleState(battleState);
                    }
                }

                state.render(worldRenderer);
                worldRenderer.renderFPSCounter();
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
                    initState(state);
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

    private PixelConfig getPixelConfig()
    {
        final PixelConfig pixelConfig = new PixelConfig();
        
        pixelConfig.setTeamConfigValues(Team.PLAYER, level);
        pixelConfig.setTeamConfigValues(Team.OTHER, level * 0.9f);

        return pixelConfig;
    }

    private float getPercentPlayer()
    {
        // TODO adjust to use level
        return .75f;
    }

    private void initState(final State state)
    {
        if(state instanceof BattleState)
        {
            initBattleState((BattleState) state);
        }
        else if(state instanceof BattleIntroState)
        {
            initBattleIntroState((BattleIntroState) state);
        }
    }

    private void initBattleState(final BattleState battleState)
    {
        battleState.init(worldRenderer, getPixels(), getPixelConfig(), atlas);
    }

    private void initBattleIntroState(final BattleIntroState battleIntoState)
    {
        battleIntoState.init(getPixels(), level);
    }

    private Pixels getPixels()
    {
        return PixelGenerator.generatePixels(getPixelConfig(), atlas, HEIGHT, getPercentPlayer());
    }
}