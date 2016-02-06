package com.squared.space.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.event.StateEvent;
import com.squared.space.game.context.SceneContext;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;

public class SceneState implements State
{
    private static final int MOVE_RIGHT_BUTTON = Input.Keys.D;
    private static final int MOVE_LEFT_BUTTON = Input.Keys.A;
    private static final int ACTION_BUTTON = Input.Keys.SPACE;

    private final StateManager stateManager;
    private Actor player;
    private List<Actor> actors;
    private NavigableMap<Integer, StateEvent> activateEvents;

    public SceneState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
        activateEvents = new TreeMap<Integer, StateEvent>();
        actors = new ArrayList<Actor>();
    }

    public void init(final WorldRenderer worldRenderer, final SceneContext context)
    {
        this.activateEvents.clear();
        this.activateEvents.putAll(context.getActivateEvents());
        this.actors.clear();
        this.actors.addAll(context.getActors());

        // TODO this should be some kind of "Context" object where it I would do
        // context.getSuperBackground() and context.getPlayer() and everything
        // is scaled correctly.
        player = context.getPlayer();
        worldRenderer.setClearColor(context.getClearColor());
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
    public void resize(final int width, final int height)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(final float dt)
    {
        player.update(dt);
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.render(actors);
        worldRenderer.render(player);
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        if(MOVE_RIGHT_BUTTON == keyCode)
        {
            player.startMoveRight();
            return true;
        }else if(MOVE_LEFT_BUTTON == keyCode)
        {
            player.startMoveLeft();
            return true;
        }else if(ACTION_BUTTON == keyCode)
        {
            final Vector2 playerPosition = player.getPosition();
            final Integer floorKey = activateEvents.floorKey((int) playerPosition.x);
            if(floorKey != null)
            {
                final StateEvent stateEvent = activateEvents.get(floorKey);
                if(stateEvent != null)
                {
                    if(stateEvent.shouldProcess(playerPosition.x))
                    {
                        stateEvent.process(stateManager);
                        if(stateEvent.shouldDelete())
                        {
                            activateEvents.remove(floorKey);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(final int keyCode)
    {
        if(MOVE_RIGHT_BUTTON == keyCode)
        {
            player.stopMoveRight();
            return true;
        }else if(MOVE_LEFT_BUTTON == keyCode)
        {
            player.stopMoveLeft();
            return true;
        }
        return false;
    }

    @Override
    public boolean unicodeEntered(final char character)
    {
        // TODO Auto-generated method stub
        return false;
    }
}