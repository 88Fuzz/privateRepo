package com.murder.game.state;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.contact.WorldContactListener;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Actor.MoveDirection;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.Level;
import com.murder.game.level.generator.LevelGenerator;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.state.StateManager.PendingAction;
import com.murder.game.state.StateManager.StateAction;
import com.murder.game.state.StateManager.StateId;

import box2dLight.RayHandler;

public class GameState implements State
{
    // TODO what happens when this is true?
    private static final boolean ALLOW_SLEEP = true;

    private World physicsWorld;
    private RayHandler rayHandler;

    private Actor player;
    private Level level;
    private StateManager stateManager;
    // TODO when the app is suspended and brought back, buttonsPressed should be
    // set back to 0
    private int buttonsPressed;

    public GameState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
        buttonsPressed = 0;
    }

    // public void init(final WorldRenderer worldRenderer, final LevelSerialize
    // levelSerialize, final TextureAtlas textureAtlas)
    public void init(final WorldRenderer worldRenderer, final TextureManager textureManager, final FontManager fontManager, final String levelKey)
    {
        physicsWorld = new World(new Vector2(0, 0), ALLOW_SLEEP);
        physicsWorld.setContactListener(new WorldContactListener());
        rayHandler = new RayHandler(physicsWorld);
        rayHandler.setAmbientLight(.5f);

        final LevelSerialize levelSerialize = LevelGenerator.getLevel(levelKey);
        level = levelSerialize.getLevel();
        level.init(physicsWorld, textureManager, fontManager);
        player = levelSerialize.getPlayer();
        // player = new Actor(BodyType.PLAYER, new MyVector2(), 0, false);
        // player.init(textureAtlas, level);
        player.init(physicsWorld, rayHandler, textureManager);
        // worldRenderer.init(player, level.getLevelBounds());
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
        physicsWorld.step(dt, 6, 2);
        rayHandler.update();

        level.update(dt);
        player.update(dt);
        if(player.isOnExit())
        {
            stateManager.addAction(new PendingAction().withAction(StateAction.POP));
            stateManager.addAction(
                    new PendingAction().withAction(StateAction.PUSH).withStatId(StateId.GAME_STATE).withStateConfig(level.getNextLevelId()));
        }
        // final Vector2 playerPos = player.getTilePosition();
        // final Tile tile = level.getTile((int) playerPos.x, (int)
        // playerPos.y);
        //
        // if(tile != null && TileType.EXIT == tile.getTileType())
        // {
        // stateManager.addAction(StateAction.POP);
        // stateManager.addAction(StateAction.PUSH, level.getNextStateId(),
        // level.getNextLevelId());
        // }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.adjustCamera();
        worldRenderer.render(physicsWorld);
        worldRenderer.render(level);
        worldRenderer.render(player);
        worldRenderer.render(rayHandler);
        worldRenderer.renderGUI();
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
            player.moveDirection(MoveDirection.UP);
            return true;
        case Input.Keys.S:
            player.moveDirection(MoveDirection.DOWN);
            return true;
        case Input.Keys.A:
            player.moveDirection(MoveDirection.LEFT);
            return true;
        case Input.Keys.D:
            player.moveDirection(MoveDirection.RIGHT);
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
            player.stopMoveDirection(MoveDirection.UP);
            return true;
        case Input.Keys.S:
            player.stopMoveDirection(MoveDirection.DOWN);
            return true;
        case Input.Keys.A:
            player.stopMoveDirection(MoveDirection.LEFT);
            return true;
        case Input.Keys.D:
            player.stopMoveDirection(MoveDirection.RIGHT);
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
        final Vector2 position = player.getBodyPosition();
        final float deltaX = screenX - position.x;
        final float deltaY = screenY - position.y;

        // player.setRotation((float) (-1*Math.toDegrees(Math.atan2(deltaX,
        // deltaY))));
        player.setRotation((float) Math.atan2(deltaX, deltaY));
    }

    private void adjustPlayerMove()
    {
        if(buttonsPressed == 1)
            player.startMove(true);
        else
            player.startMove(false);
    }
}
