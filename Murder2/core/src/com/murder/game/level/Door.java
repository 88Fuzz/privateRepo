package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
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
import com.murder.game.drawing.drawables.DrawPosition;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.BodyBuilder;

public class Door extends Tile
{
    private static final String ITEM_UNLOCK = "itemUnlock";
    private static final String DOOR_MAT = "doorMat";

    private static final int BODY_SHRINKING_SIZE = 10;
    private static final float MAX_SHRINKING_TIME = 0.02f;

    private static final BodyType UNLOCKED_BODY_TYPE = BodyType.FLOOR;
    private static final Filter FLOOR_FILTER = new Filter();
    private static final float DOOR_MAT_OFFSET = .1f;

    static
    {
        FLOOR_FILTER.categoryBits = UNLOCKED_BODY_TYPE.getCategoryBits();
        FLOOR_FILTER.maskBits = UNLOCKED_BODY_TYPE.getMaskBits();
        FLOOR_FILTER.groupIndex = UNLOCKED_BODY_TYPE.getGroupIndex();
    }

    /**
     * Signifies what direction the door mat should extend.
     */
    public static enum DoorMat
    {
        UP(0, 1, 1.0f, DOOR_MAT_OFFSET, 1, 0),
        DOWN(0, -.1f, 1.0f, DOOR_MAT_OFFSET, 1, 0),
        LEFT(1, 0, DOOR_MAT_OFFSET, 1.0f, 0, 1),
        RIGHT(-1, 0, DOOR_MAT_OFFSET, 1.0f, 0, 1);

        private final float xOffset;
        private final float yOffset;
        private final float xScale;
        private final float yScale;
        private final int widthScale;
        private final int heightScale;

        private DoorMat(final float xDirection, final float yDirection, final float xScale, final float yScale, final int widthScale,
                final int heightScale)
        {
            xOffset = xDirection;
            yOffset = yDirection;

            this.xScale = xScale;
            this.yScale = yScale;

            this.widthScale = widthScale;
            this.heightScale = heightScale;
        }

        public float getxOffset()
        {
            return xOffset;
        }

        public float getyOffset()
        {
            return yOffset;
        }

        public float getXScale()
        {
            return xScale;
        }

        public float getYScale()
        {
            return yScale;
        }

        public int getWidthScale()
        {
            return widthScale;
        }

        public int getHeightScale()
        {
            return heightScale;
        }
    }

    private static class DoorInfo
    {
        public Body body;
        public float width;
        public float height;
    }

    private ItemType itemUnlock;
    private DoorMat doorMat;
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
    @JsonIgnore
    private Sprite doorMatSprite;

    @JsonCreator
    public Door(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(ITEM_UNLOCK) ItemType itemUnlock,
            @JsonProperty(POSITION) final MyVector2 position, @JsonProperty(ROTATION) final float rotation,
            @JsonProperty(DOOR_MAT) final DoorMat doorMat)
    {
        super(bodyType, position, rotation, DrawPosition.WALLS);
        this.itemUnlock = itemUnlock;
        this.doorMat = doorMat;

        shrinkingBodies = new DoorInfo[BODY_SHRINKING_SIZE];
    }

    public void init(final World physicsWorld, final List<Mob> mobs)
    {
        doorMatSprite = new Sprite(bodyType.getTextureLoader().getAtlasRegion());
        this.physicsWorld = physicsWorld;
        super.init(physicsWorld, mobs);

        doorMatSprite.setBounds(0, 0, doorMat.getXScale() * bodyType.getWidth(), doorMat.getYScale() * bodyType.getHeight());
        if(bodyType.getColor() != Color.CLEAR)
        {
            final Color color = bodyType.getColor().cpy();
            color.a = .5f;
            doorMatSprite.setColor(color);
        }
        doorMatSprite.setOriginCenter();

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
        doorMatSprite.draw(batch);
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
        doorMatSprite.setSize(0, 0);
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

    @Override
    protected void adjustSprite(final Body body, final Sprite sprite)
    {
        super.adjustSprite(body, sprite);
        adjustDoorMat();
    }

    private void adjustDoorMat()
    {
        final float x = sprite.getX() + doorMat.getxOffset() * sprite.getWidth();
        final float y = sprite.getY() + doorMat.getyOffset() * sprite.getHeight();
        final float width = getScaleSize(doorMat.getWidthScale(), sprite.getWidth(), doorMatSprite.getWidth());
        final float height = getScaleSize(doorMat.getHeightScale(), sprite.getHeight(), doorMatSprite.getHeight());

        doorMatSprite.setBounds(x, y, width, height);
    }

    private float getScaleSize(final int scale, final float size, final float defaultSize)
    {
        return (scale != 0) ? scale * size : defaultSize;
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