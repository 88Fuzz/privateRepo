package com.murder.game.drawing;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.level.pathfinder.PathFinder;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.MathUtils;

import box2dLight.RayHandler;

public class Mob extends Actor
{
    private static final float MAX_MOB_VELOCITY = 1060;
    private String previousPathKey;
    private PathFinder pathFinder;
    private Actor player;
    private boolean playerFound;
    private Level level;
    private float distanceToTravel;

    @JsonCreator
    public Mob(@JsonProperty(BODY_TYPE) BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        velocity = MAX_MOB_VELOCITY;
    }

    public void init(final World physicsWorld, final RayHandler rayHandler, final TextureManager textureManager, final Level level,
            final Actor player)
    {
        super.init(physicsWorld, rayHandler, textureManager);
        this.player = player;
        this.level = level;
        this.distanceToTravel = 0;

        this.previousPathKey = getPathKey();

        pathFinder = new PathFinder();
        pathFinder.init(level);
        playerFound = pathFinder.findPath(previousPathKey, getTilePositionX(), getTilePositionY(), player.getTilePositionX(),
                player.getTilePositionY());
    }

    @Override
    public void updateCurrent(final float dt)
    {
        final String newPathKey = getPathKey();

        if(!newPathKey.equals(previousPathKey))
        {
            findPath(newPathKey);
        }

        if(playerFound)
        {
            modifyDistanceToTravel(dt);
            if(distanceToTravel <= 0)
                calculateVelocity();

            super.updateCurrent(dt);
        }
    }

    private void modifyDistanceToTravel(final float dt)
    {
        final float mag = unitVelocityVector.len();
        if(mag != 0)
        {
            final float velocityX = velocity * unitVelocityVector.x * dt / mag;
            final float velocityY = velocity * unitVelocityVector.y * dt / mag;
            final float distance = velocityX * velocityX + velocityY * velocityY;

            distanceToTravel -= distance;
        }
    }

    private void calculateVelocity()
    {
        final Tile currentTile = level.getTile(getTilePositionX(), getTilePositionY());
        if(currentTile == null)
        {
            unitVelocityVector.x = 0;
            unitVelocityVector.y = 0;
            return;
        }

        Tile nextTile = currentTile.getChildTile(previousPathKey);
        if(nextTile == null)
        {
            // TODO this may possibly need to be reworked.
            nextTile = level.getTile(player.getTilePositionX(), player.getTilePositionY());

            // This is an error state, if there is no nextTile along the path,
            // the mob should be in the same tile as the player. yo
            if(nextTile != currentTile)
            {
                unitVelocityVector.x = 0;
                unitVelocityVector.y = 0;
                return;
            }
        }

        setUnitVelocity(currentTile, nextTile);
    }

    private void setUnitVelocity(final Tile currentTile, final Tile nextTile)
    {
        final Vector2 currentTilePos = currentTile.getPosition();
        final Vector2 nextTilePos = nextTile.getPosition();

        float diff = nextTilePos.x - currentTilePos.x;
        if(diff < 0)
            unitVelocityVector.x = -1;
        else if(diff > 0)
            unitVelocityVector.x = 1;
        else
            unitVelocityVector.x = 0;

        diff = nextTilePos.y - currentTilePos.y;
        if(diff < 0)
            unitVelocityVector.y = -1;
        else if(diff > 0)
            unitVelocityVector.y = 1;
        else
            unitVelocityVector.y = 0;

        calculateDistanceToTravel(currentTilePos, nextTilePos);
    }

    private void calculateDistanceToTravel(final Vector2 currentTilePos, final Vector2 nextTilePos)
    {
        distanceToTravel = MathUtils.getDistance(currentTilePos.x, currentTilePos.y, nextTilePos.x, nextTilePos.y);
    }

    public void findPath()
    {
        final String pathKey = getPathKey();
        findPath(pathKey);
    }

    private void findPath(final String pathKey)
    {
        playerFound = pathFinder.findPath(pathKey, getTilePositionX(), getTilePositionY(), player.getTilePositionX(), player.getTilePositionY());
        previousPathKey = pathKey;
    }

    private String getPathKey()
    {
        return "" + getTilePositionX() + ":" + getTilePositionY() + "," + player.getTilePositionX() + ":" + player.getTilePositionY();
    }
}