package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.Drawable;
import com.murder.game.serialize.MyVector2;

public class Item extends Drawable
{
    private static final String ITEM_TYPE = "itemType";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";

    private ItemType itemType;
    @JsonIgnore
    private boolean pickedUp;

    @JsonCreator
    public Item(@JsonProperty(ITEM_TYPE) final ItemType itemType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(itemType.getBodyType(), position, rotation);
        this.itemType = itemType;
        this.pickedUp = false;
    }

    public void init(final World physicsWorld)
    {
        super.init(physicsWorld);
        this.pickedUp = false;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    protected void updateCurrent(final float dt)
    {
        // Do nothing
    }

    public ItemType pickUpItem()
    {
        pickedUp = true;
        return itemType;
    }

    public boolean isPickedUp()
    {
        return pickedUp;
    }

    public ItemType getItemType()
    {
        return itemType;
    }
}