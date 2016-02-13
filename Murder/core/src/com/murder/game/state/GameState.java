package com.murder.game.state;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Actor.Direction;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.Level;
import com.murder.game.level.Tile.TileType;
import com.murder.game.level.room.Room;
import com.murder.game.level.Tile;
import com.murder.game.level.serial.LevelSerialize;
import com.murder.game.state.StateManager.StateAction;
import com.murder.game.utils.StringUtils;

public class GameState implements State
{
    private Actor player;
    private Level level;
    private StateManager stateManager;
    private String prevPlayerRoom;

    public GameState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
    }

    public void init(final WorldRenderer worldRenderer, final LevelSerialize levelSerialize,
            final TextureAtlas textureAtlas)
    {
        level = levelSerialize.getLevel();
        player = levelSerialize.getPlayer();
        player.init(textureAtlas, level);
        worldRenderer.init(player, level.getLevelBounds());
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
        level.update(dt);
        player.update(dt);
        final Vector2 playerPos = player.getTilePosition();
        final Tile tile = level.getTile((int) playerPos.x, (int) playerPos.y);
        final String currPlayerRoom = tile.getRoomId();

        if(!StringUtils.equals(prevPlayerRoom, currPlayerRoom))
        {
            List<Room> rooms = level.getRooms(prevPlayerRoom);
            if(rooms != null)
            {
                for(final Room room: rooms)
                {
                    room.turnOffLight();
                }
            }

            rooms = level.getRooms(currPlayerRoom);
            if(rooms != null)
            {
                for(final Room room: rooms)
                {
                    room.turnOnLight();
                }
            }

            prevPlayerRoom = currPlayerRoom;
        }

        if(TileType.EXIT == tile.getTileType())
        {
            stateManager.addAction(StateAction.POP);
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.adjustCamera();
        worldRenderer.render(level);
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
        switch(keyCode)
        {
        case Input.Keys.W:
            player.moveDirection(Direction.UP);
            return true;
        case Input.Keys.S:
            player.moveDirection(Direction.DOWN);
            return true;
        case Input.Keys.A:
            player.moveDirection(Direction.LEFT);
            return true;
        case Input.Keys.D:
            player.moveDirection(Direction.RIGHT);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keyCode)
    {
        switch(keyCode)
        {
        case Input.Keys.W:
            player.stopMoveDirection(Direction.UP);
            return true;
        case Input.Keys.S:
            player.stopMoveDirection(Direction.DOWN);
            return true;
        case Input.Keys.A:
            player.stopMoveDirection(Direction.LEFT);
            return true;
        case Input.Keys.D:
            player.stopMoveDirection(Direction.RIGHT);
            return true;
        }
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unicodeEntered(char character)
    {
        // TODO Auto-generated method stub
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
    public boolean mouseScrolled(int amount)
    {
        // TODO Auto-generated method stub
        return false;
    }
}