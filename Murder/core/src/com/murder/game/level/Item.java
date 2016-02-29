package com.murder.game.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Drawable;

public class Item extends Drawable
{
    private static final String INVENTORY_ITEM = "inventoryItem";
    private static final String POSITION = "position";

    public enum InventoryItem
    {
        GREEN_KEY(TextureConstants.KEY_TEXTURE, Color.GREEN);

        private final String textureName;
        private final Color color;

        InventoryItem(final String textureName, final Color color)
        {
            this.textureName = textureName;
            this.color = color;
        }

        public String getTextureName()
        {
            return textureName;
        }

        public Color getColor()
        {
            return color;
        }
    }

    private InventoryItem inventoryItem;

    @JsonCreator
    public Item(@JsonProperty(INVENTORY_ITEM) final InventoryItem inventoryItem,
            @JsonProperty(POSITION) final Vector2 position)
    {
        this.inventoryItem = inventoryItem;
        this.position.set(position);
    }

    public void init(final TextureAtlas textureAtlas)
    {
        super.init(textureAtlas, inventoryItem.getTextureName(), false);
        sprite.setColor(inventoryItem.getColor());
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void update(final float dt)
    {
        // TODO Auto-generated method stub
    }

    public InventoryItem getInventoryItem()
    {
        return inventoryItem;
    }
}