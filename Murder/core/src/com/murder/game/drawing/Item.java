package com.murder.game.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;

public class Item extends Drawable
{
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

    public Item(final InventoryItem inventoryItem, final Vector2 position)
    {
        super();
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