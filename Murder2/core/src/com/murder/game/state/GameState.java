package com.murder.game.state;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.contact.WorldContactListener;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Actor.MoveDirection;
import com.murder.game.drawing.Mob;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.level.Level;
import com.murder.game.level.generator.LevelGenerator;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.state.FadeEffect.FadeDirection;
import com.murder.game.state.management.PendingAction;
import com.murder.game.state.management.StateManager;

import box2dLight.RayHandler;

public class GameState extends State
{
    private static final float FADE_IN_TIME = 1.41f;
    private static final float FADE_OUT_TIME = 1.51f;

    private static final boolean ALLOW_SLEEP = true;

    private World physicsWorld;
    private RayHandler rayHandler;

    private Actor player;
    private List<Mob> mobs;
    private Level level;
    // TODO when the app is suspended and brought back, buttonsPressed should be
    // set back to 0
    private LinkedList<Integer> touches;
    private List<PendingAction> stateActions;
    private FadeEffect fadeIn;
    private FadeEffect fadeOut;

    public GameState(final StateManager stateManager)
    {
        super(stateManager);
        this.touches = new LinkedList<Integer>();
    }

    // public void init(final WorldRenderer worldRenderer, final LevelSerialize
    // levelSerialize, final TextureAtlas textureAtlas)
    public void init(final WorldRenderer worldRenderer, final FontManager fontManager, final String levelKey)
    {
        physicsWorld = new World(new Vector2(0, 0), ALLOW_SLEEP);
        physicsWorld.setContactListener(new WorldContactListener());
        rayHandler = new RayHandler(physicsWorld);
        // rayHandler.setAmbientLight(.5f);

        final LevelSerialize levelSerialize = LevelGenerator.getLevel(levelKey);
        stateActions = levelSerialize.getStateActions();
        mobs = levelSerialize.getMobs();
        level = levelSerialize.getLevel();
        player = levelSerialize.getPlayer();

        level.init(physicsWorld, fontManager, mobs, rayHandler);
        // player = new Actor(BodyType.PLAYER, new MyVector2(), 0, false);
        // player.init(textureAtlas, level);
        player.init(physicsWorld, rayHandler);
        for(final Mob mob: mobs)
        {
            mob.init(physicsWorld, rayHandler, level, player);
        }
        // worldRenderer.init(player, level.getLevelBounds());
        worldRenderer.init(player, level.getLevelBounds());

        touches.clear();

        fadeIn = new FadeEffect();
        fadeIn.init(level.getLevelBounds(), FADE_IN_TIME, FadeDirection.FADE_IN);
        fadeOut = new FadeEffect();
        fadeOut.init(level.getLevelBounds(), FADE_OUT_TIME, FadeDirection.FADE_OUT);
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

        updateLevel(dt);
        updatePlayer(dt);
        updateMobs(dt);
        fadeIn.update(dt);

        if(player.isOnExit())
        {
            fadeOut.update(dt);
            if(fadeOut.isFinished())
                stateManager.addActions(stateActions);
            // stateManager.addAction(new
            // PendingAction().withAction(StateAction.POP));
            // stateManager.addAction(
            // new
            // PendingAction().withAction(StateAction.PUSH).withStateId(level.getNextStateId()).withStateConfig(level.getNextLevelId()));
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

    private void updateLevel(final float dt)
    {
        level.update(dt);
    }

    private void updatePlayer(final float dt)
    {
        player.update(dt);
    }

    private void updateMobs(final float dt)
    {
        for(final Mob mob: mobs)
        {
            mob.update(dt);
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.adjustCamera();
        worldRenderer.render(physicsWorld);
        worldRenderer.render(level);
        worldRenderer.render(player);
        renderMobs(worldRenderer);
        worldRenderer.render(rayHandler);
        worldRenderer.render(fadeIn);
        if(player.isOnExit())
            worldRenderer.render(fadeOut);
        worldRenderer.renderGUI();
    }

    private void renderMobs(final WorldRenderer worldRenderer)
    {
        for(final Mob mob: mobs)
        {
            worldRenderer.render(mob);
        }
    }

    @Override
    public void dispose()
    {
        level.dispose();
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        // TODO remove these for release
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
    public boolean keyUp(final int keyCode)
    {
        // TODO remove these for release
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
        return false;
    }

    @Override
    public boolean unicodeEntered(final char character)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        touches.addLast(new Integer(pointer));

        // TODO Should this be controlled by the player?
        adjustPlayerRotation(screenX, screenY);
        adjustPlayerMove();
        return true;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        if(!touches.isEmpty())
            touches.remove(new Integer(pointer));

        adjustPlayerMove();
        return true;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        // Only follow the last button/finger pressed when rotating
        if(!touches.isEmpty() && pointer == touches.getLast().intValue())
            adjustPlayerRotation(screenX, screenY);

        return true;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseScrolled(final int amount)
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
        if(touches.size() == 1)
            player.startMove(true);
        else
            player.startMove(false);
    }
}