package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.Mob;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.BodyBuilder;

public class Door extends Tile
{
    private static final int BODY_SHRINKING_SIZE = 10;
    private static final float MAX_SHRINKING_TIME = 0.02f;

    private static final String ITEM_UNLOCK = "itemUnlock";
    private static final BodyType UNLOCKED_BODY_TYPE = BodyType.FLOOR;
    private static final Filter FLOOR_FILTER = new Filter();

    static
    {
        FLOOR_FILTER.categoryBits = UNLOCKED_BODY_TYPE.getCategoryBits();
        FLOOR_FILTER.maskBits = UNLOCKED_BODY_TYPE.getMaskBits();
        FLOOR_FILTER.groupIndex = UNLOCKED_BODY_TYPE.getGroupIndex();
    }

    private static class DoorInfo
    {
        public Body body;
        public float width;
        public float height;
    }

    private ItemType itemUnlock;
    @JsonIgnore
    private World physicsWorld;
    @JsonIgnore
    private Body floorBody;
    @JsonIgnore
    private Sprite floorSprite;
    @JsonIgnore
    private boolean locked;
    @JsonIgnore
    private boolean unlocking;
    @JsonIgnore
    private int shrinkingBodiesPos;
    @JsonIgnore
    private float shrinkingTime;
    @JsonIgnore
    private DoorInfo[] shrinkingBodies;

    @JsonCreator
    public Door(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(ITEM_UNLOCK) ItemType itemUnlock,
            @JsonProperty(POSITION) final MyVector2 position, @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        this.itemUnlock = itemUnlock;

        shrinkingBodies = new DoorInfo[BODY_SHRINKING_SIZE];
    }

    public void init(final World physicsWorld, final List<Mob> mobs)
    {
        super.init(physicsWorld, mobs);
        this.physicsWorld = physicsWorld;

        floorSprite = getSprite(UNLOCKED_BODY_TYPE);
        floorBody = generateBody(physicsWorld, UNLOCKED_BODY_TYPE, floorSprite);
        generateShrinkingBodies(physicsWorld);

        locked = true;
        unlocking = false;
        shrinkingBodiesPos = -1;
        shrinkingTime = 0;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        floorSprite.draw(batch);
        sprite.draw(batch);
    }

    @Override
    public void updateCurrent(final float dt)
    {
        if(unlocking)
        {
            if(shrinkingTime <= 0)
            {
                physicsWorld.destroyBody(body);
                shrinkingBodiesPos++;
                if(shrinkingBodiesPos >= BODY_SHRINKING_SIZE)
                {
                    finishUnlocking();
                    return;
                }

                body = shrinkingBodies[shrinkingBodiesPos].body;
                bodyWidth = shrinkingBodies[shrinkingBodiesPos].width;
                bodyHeight = shrinkingBodies[shrinkingBodiesPos].height;
                shrinkingTime = MAX_SHRINKING_TIME;
            }
            shrinkingTime -= dt;
        }
    }

    public boolean unlockDoor(final ItemType key)
    {
        if(key == itemUnlock)
        {
            unlock();
            return true;
        }

        return false;
    }

    private void finishUnlocking()
    {
        unlocking = false;
        bodyWidth = 0;
        bodyHeight = 0;
        for(final Fixture fixture: body.getFixtureList())
        {
            fixture.setFilterData(FLOOR_FILTER);
        }
    }

    private void unlock()
    {
        unlocking = true;
        locked = false;
        updateMobs();
    }

    private void generateShrinkingBodies(final World physicsWorld)
    {
        final float widthShrinkRate = bodyType.getWidth() / (BODY_SHRINKING_SIZE + 1);
        final float heightShrinkRate = bodyType.getHeight() / (BODY_SHRINKING_SIZE + 1);

        for(int i = 0; i < BODY_SHRINKING_SIZE; i++)
        {
            final float width = bodyType.getWidth() - widthShrinkRate * (i + 1);
            final float height = bodyType.getHeight() - heightShrinkRate * (i + 1);
            shrinkingBodies[i] = new DoorInfo();
            shrinkingBodies[i].body = BodyBuilder.createBody(physicsWorld, bodyType, position, 0, width, height);
            shrinkingBodies[i].width = width;
            shrinkingBodies[i].height = height;
        }
    }

    public ItemType getItemUnlock()
    {
        return itemUnlock;
    }

    @Override
    public boolean isTraversable()
    {
        return !locked;
    }
}