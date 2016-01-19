package com.squared.space.game.state;

import com.squared.space.game.drawing.WorldRenderer;

public class TestState implements State
{
    private final StateManager stateManager;

    public TestState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
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
        // TODO Auto-generated method stub
    }

    @Override
    public void render(WorldRenderer worldRenderer)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean keyDown(int keycode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keyCode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unicodeEntered(char character)
    {
        // TODO Auto-generated method stub
        return false;
    }
}