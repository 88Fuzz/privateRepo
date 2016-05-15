package com.murder.game.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.murder.game.MurderMainMain;
import com.murder.game.constants.drawing.TextureType;
import com.murder.game.drawing.NonBodyDrawable;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;

public class FadeEffect extends NonBodyDrawable
{
    public static enum FadeDirection
    {
        FADE_IN(-1, 1, 0),
        FADE_OUT(1, 0, 1);

        private final int fadeDirection;
        private final int alpha;
        private final int finalAlpha;

        private FadeDirection(final int fadeDirection, final int alpha, final int finalAlpha)
        {
            this.fadeDirection = fadeDirection;
            this.alpha = alpha;
            this.finalAlpha = finalAlpha;
        }

        public int getFadeDirection()
        {
            return fadeDirection;
        }

        public int getAlpha()
        {
            return alpha;
        }

        public int getFinalAlpha()
        {
            return finalAlpha;
        }
    }

    private FadeDirection fadeDirection;
    private float incrementalFade;
    private Sprite sprite;
    private Color color;
    private boolean finished;

    public FadeEffect()
    {
        super(new MyVector2(), 0);
    }

    public void init(final TextureManager textureManager, final Rectangle size, final float timeToFade, final FadeDirection fadeDirection)
    {
        this.fadeDirection = fadeDirection;
        color = new Color(0, 0, 0, fadeDirection.getAlpha());
        sprite = new Sprite(textureManager.getTexture(TextureType.SINGLE_PIXEL_TEXTURE), (int) size.width, (int) size.height);
        sprite.setColor(color);
        finished = false;
        incrementalFade = MurderMainMain.TIMEPERFRAME / timeToFade * fadeDirection.getFadeDirection();
    }

    public boolean isFinished()
    {
        return finished;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    protected void updateCurrent(final float dt)
    {
        color.a += incrementalFade;
        if(fadeDirection.fadeDirection < 0)
        {
            if(color.a <= fadeDirection.getFinalAlpha())
            {
                color.a = fadeDirection.getFinalAlpha();
                finished = true;
            }
        }
        else
        {
            if(color.a >= fadeDirection.getFinalAlpha())
            {
                color.a = fadeDirection.getFinalAlpha();
                finished = true;
            }
        }
        sprite.setColor(color);
    }
}