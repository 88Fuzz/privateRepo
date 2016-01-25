package com.squared.space.game.state;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.game.event.StateEvent;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.context.SceneContext;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;

public class TestState implements State
{
    private static final int MOVE_RIGHT_BUTTON = Input.Keys.D;
    private static final int MOVE_LEFT_BUTTON = Input.Keys.A;
    private static final int ACTION_BUTTON = Input.Keys.SPACE;

    private final StateManager stateManager;
    private Sprite superBackground;
    private Actor player;
    private List<Actor> actors;
    private NavigableMap<Integer, StateEvent> activateEvents;

    public TestState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
        activateEvents = new TreeMap<Integer, StateEvent>();
        actors = new ArrayList<Actor>();
    }

    public void init(final SceneContext context)
    {
        this.activateEvents.clear();
        this.activateEvents.putAll(context.getActivateEvents());
        this.actors.clear();
        this.actors.addAll(context.getActors());

        player = new Actor();
        // TODO this should be some kind of "Context" object where it I would do
        // context.getSuperBackground() and context.getPlayer() and everything
        // is scaled correctly.
        final AtlasRegion pixelRegion = context.getAtlas().findRegion(TextureConstants.SINGLE_PIXEL);
        superBackground = new Sprite(pixelRegion);
        superBackground.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        superBackground.setColor(1, 1, 0, 1);

        final Sprite playerSprite = new Sprite(pixelRegion);
        final int size = Gdx.graphics.getHeight() / 10;
        playerSprite.setBounds(0, 0, size, size);
        playerSprite.setColor(0, 0, 1, 1);
        player.init(new Vector2(0, 0), playerSprite);
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
        worldRenderer.render(superBackground);
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
