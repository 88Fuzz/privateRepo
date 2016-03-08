package com.murder.game.state;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Actor.Direction;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.Level;
import com.murder.game.level.Tile.TileType;
import com.murder.game.level.Tile;
import com.murder.game.level.serial.LevelSerialize;
import com.murder.game.level.serial.MyVector2;
import com.murder.game.state.StateManager.StateAction;
import com.murder.game.state.StateManager.StateId;

public class GameState implements State
{
    private Actor player;
    private Level level;
    private StateManager stateManager;
    private int buttonsPressed;

    public GameState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
        buttonsPressed = 0;
    }

    public void init(final WorldRenderer worldRenderer, final LevelSerialize levelSerialize,
            final TextureAtlas textureAtlas)
    {
        level = levelSerialize.getLevel();
        level.init(textureAtlas);
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

        if(tile != null && TileType.EXIT == tile.getTileType())
        {
            stateManager.addAction(StateAction.POP);
            stateManager.addAction(StateAction.PUSH, StateId.GAME_STATE, level.getNextLevelId());
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.adjustCamera();
        worldRenderer.render(level);
        worldRenderer.render(player);
        worldRenderer.render(player.getFlashlight());
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
        case Input.Keys.LEFT:
            player.rotate(-1);
            return true;
        case Input.Keys.RIGHT:
            player.rotate(1);
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
        buttonsPressed++;

        // TODO Should this be controlled by the player?
        adjustPlayerRotation(screenX, screenY);
        adjustPlayerMove();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        buttonsPressed--;
        adjustPlayerMove();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        adjustPlayerRotation(screenX, screenY);
        return true;
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

    private void adjustPlayerRotation(final int screenX, final int screenY)
    {
        final MyVector2 position = player.getCenterPosition();
        final float deltaX = screenX - position.x;
        final float deltaY = screenY - position.y;

        player.setRotation((float) Math.toDegrees(Math.atan2(deltaX, deltaY)));
    }
    
    private void adjustPlayerMove()
    {
        if(buttonsPressed == 1)
            player.startMove(true);
        else
            player.startMove(false);
    }
}