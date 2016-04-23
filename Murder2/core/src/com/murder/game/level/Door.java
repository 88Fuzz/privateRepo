package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;

public class Door extends Tile
{
    private static final String BODY_TYPE = "bodyType";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";
    private static final String ITEM_UNLOCK = "itemUnlock";
    private static final BodyType UNLOCKED_BODY_TYPE = BodyType.FLOOR;
    private static final Filter FLOOR_FILTER = new Filter();

    static
    {
        FLOOR_FILTER.categoryBits = UNLOCKED_BODY_TYPE.getCategoryBits();
        FLOOR_FILTER.maskBits = UNLOCKED_BODY_TYPE.getMaskBits();
        FLOOR_FILTER.groupIndex = UNLOCKED_BODY_TYPE.getGroupIndex();
    }

    private ItemType itemUnlock;
    @JsonIgnore
    private Body floorBody;
    @JsonIgnore
    private Sprite floorSprite;
    @JsonIgnore
    private boolean locked;

    @JsonCreator
    public Door(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(ITEM_UNLOCK) ItemType itemUnlock,
            @JsonProperty(POSITION) final MyVector2 position, @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        this.itemUnlock = itemUnlock;
    }

    public void init(final World physicsWorld, final TextureManager textureManager, final List<Mob> mobs)
    {
        super.init(physicsWorld, textureManager, mobs);
        floorSprite = getSprite(textureManager, UNLOCKED_BODY_TYPE);
        floorBody = getBody(physicsWorld, UNLOCKED_BODY_TYPE, floorSprite);
        locked = true;
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

    private void unlock()
    {
        for(final Fixture fixture: body.getFixtureList())
        {
            fixture.setFilterData(FLOOR_FILTER);
        }

        body = floorBody;
        sprite = floorSprite;
        locked = false;
        updateMobs();
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