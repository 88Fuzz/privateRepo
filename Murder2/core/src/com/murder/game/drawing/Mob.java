package com.murder.game.drawing;

import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.level.Level;
import com.murder.game.level.pathfinder.PathFinder;
import com.murder.game.serialize.MyVector2;

import box2dLight.RayHandler;

public class Mob extends Actor
{
    private String previousPathKey;
    private PathFinder pathFinder;
    private Actor player;
    private boolean playerFound;

    @JsonCreator
    public Mob(@JsonProperty(BODY_TYPE) BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        velocity = 0;
    }

    public void init(final World physicsWorld, final RayHandler rayHandler, final TextureManager textureManager, final Level level,
            final Actor player)
    {
        super.init(physicsWorld, rayHandler, textureManager);
        this.player = player;

        this.previousPathKey = getPathKey(player);

        pathFinder = new PathFinder();
        pathFinder.init(level);
        playerFound = pathFinder.findPath(previousPathKey, getTilePositionX(), getTilePositionY(), player.getTilePositionX(),
                player.getTilePositionY());
    }

    @Override
    public void updateCurrent(final float dt)
    {
        final String newPathKey = getPathKey(player);

        if(!newPathKey.equals(previousPathKey))
        {
            playerFound = pathFinder.findPath(newPathKey, getTilePositionX(), getTilePositionY(), player.getTilePositionX(),
                    player.getTilePositionY());
            previousPathKey = newPathKey;
        }

        super.updateCurrent(dt);
    }

    private String getPathKey(final Actor actor)
    {
        return "" + getTilePositionX() + ":" + getTilePositionY() + "," + actor.getTilePositionX() + ":" + actor.getTilePositionY();
    }
}