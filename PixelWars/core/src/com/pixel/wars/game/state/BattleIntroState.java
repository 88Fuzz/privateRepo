package com.pixel.wars.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.pixel.wars.game.data.Pixels;
import com.pixel.wars.game.drawing.Text;
import com.pixel.wars.game.rendering.WorldRenderer;
import com.pixel.wars.game.state.StateManager.StateAction;
import com.pixel.wars.game.state.StateManager.StateId;

public class BattleIntroState implements State
{
    private static final int DISPLAY_TIME = 1;
    private static final String LEVEL_STRING = "LEVEL: ";
    private static final Vector2 TEXT_POSITION = new Vector2(Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 2);

    private final StateManager stateManager;
    private Pixels pixels;
    private Text text;
    private float displayTime;

    public BattleIntroState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
    }

    public void init(final Pixels pixels, final int level)
    {
        this.displayTime = DISPLAY_TIME;
        this.text = new Text(LEVEL_STRING + level, Color.BLACK, (int) TEXT_POSITION.x, (int) TEXT_POSITION.y, 3);
        this.pixels = pixels;
    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(float dt)
    {
        displayTime -= dt;
        if(displayTime < 0)
        {
            stateManager.addAction(StateAction.POP);
            stateManager.addAction(StateAction.PUSH, StateId.BATTLE_STATE);
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.render(pixels.getPixels());
        worldRenderer.render(text);
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }
}