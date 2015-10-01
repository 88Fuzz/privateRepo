package com.libgdx.airplane.game.drawable.buildings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractDrawable;
import com.libgdx.airplane.game.drawable.weapons.Hittable;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Building extends AbstractDrawable implements Hittable
{
    public Building(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position, final float width,
            final float height)
    {
        super();
        init(atlas, mapDetails, position, width, height);
    }

    public void init(final TextureAtlas atlas, final MapDetails mapDetails, final Vector2 position, final float width,
            final float height)
    {
        super.init(mapDetails, true, position);
        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));

        sprite.setColor(Color.DARK_GRAY);
        sprite.setBounds(0, 0, width, height);
        sprite.setPosition(position.x, position.y);
    }

    @Override
    public void update(float dt)
    {
        // Do nothing. It's a fucking building... for now
    }

    @Override
    public float hit()
    {
        return kill();
    }

    @Override
    public float kill()
    {
        setAlive(false);
        return 0;
    }

    @Override
    protected void drawCurrent(SpriteBatch batch)
    {
        // TODO Auto-generated method stub

    }
}