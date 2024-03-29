package com.yarn.unravelry.states;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yarn.unravelry.utils.MathUtils;
import com.yarn.unravelry.utils.constants.TextureConstants;

public class GameState extends ApplicationAdapter
{
    private static final float RED = 0;
    private static final float GREEN = 38 / 255f;
    private static final float BLUE = 87 / 255f;
    private static final float ALPHA = 1;
    private static final float TIMEPERFRAME = 1.0f / 60.0f;
    private static final float UNRAVEL_FACTOR = 0.01f;

    private SpriteBatch batch;
    private Vector2 pastTouchPos;
    private TextureAtlas atlas;
    private AssetManager assMan;
    private Sprite sprite;
    private Sprite coverSprite;
    private Vector2 spritePos;
    private float timeSinceLastUpdate;
    private float percentFilled;
    private float deltaScale;
    private float xScale;
    private float baseYScale;

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        pastTouchPos = null;
        percentFilled = 100.0f;
        assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        sprite = new Sprite(atlas.findRegion(TextureConstants.SHIRT_KEY));
        sprite.setPosition(0, 0);
        float x = Gdx.graphics.getWidth()/2;
        x -= sprite.getWidth()/2;
        float y = Gdx.graphics.getHeight() - sprite.getHeight();
        sprite.setPosition(x, y);
        
        coverSprite = new Sprite(atlas.findRegion(TextureConstants.SQUARE_KEY));
        coverSprite.setColor(RED, GREEN, BLUE, ALPHA);
        coverSprite.setPosition(0, 0);

        xScale = Gdx.graphics.getWidth() / coverSprite.getWidth();
        baseYScale = Gdx.graphics.getHeight() / y;
        System.out.println("yScale " + baseYScale);
        coverSprite.setScale(xScale, baseYScale);
        spritePos = new Vector2();
        Rectangle boundingRectangle = coverSprite.getBoundingRectangle();
        spritePos.x = boundingRectangle.x * -1;
        spritePos.y = boundingRectangle.y * -1;
        coverSprite.setPosition(spritePos.x, spritePos.y);
        
        deltaScale = Gdx.graphics.getHeight()/coverSprite.getHeight();
        deltaScale = deltaScale - baseYScale;

//        spritePos = new Vector2();
//        xScale = Gdx.graphics.getWidth() / sprite.getWidth();
//        yScale = Gdx.graphics.getHeight() / sprite.getHeight();
//        sprite.setScale(xScale, yScale * percentFilled / 100.0f);
//        Rectangle boundingRectangle = sprite.getBoundingRectangle();
//        spritePos.x = boundingRectangle.x * -1;
//        spritePos.y = boundingRectangle.y * -1;
//
//        sprite.setPosition(spritePos.x, spritePos.y);
    }

    @Override
    public void render()
    {
        float dt = Gdx.graphics.getDeltaTime();

        timeSinceLastUpdate += dt;
        while(timeSinceLastUpdate > TIMEPERFRAME)
        {
            timeSinceLastUpdate -= TIMEPERFRAME;
            update(TIMEPERFRAME);
        }
        draw();
    }

    private void draw()
    {
        Gdx.gl.glClearColor(RED, GREEN, BLUE, ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        coverSprite.draw(batch);

        batch.end();
    }

    private void update(float dt)
    {
        boolean touch;
        int posX;
        int posY;

        touch = Gdx.input.isTouched(0);
        posX = Gdx.input.getX(0);
        posY = Gdx.input.getY(0);

        if(touch)
        {
            if(pastTouchPos == null)
            {
                pastTouchPos = new Vector2();
            }else if(posY < pastTouchPos.y)
            {
                float distance = MathUtils.getDistance(posX, posY, pastTouchPos.x, pastTouchPos.y);
                percentFilled -= distance * UNRAVEL_FACTOR;
                setSpriteScale();
            }

            pastTouchPos.x = posX;
            pastTouchPos.y = posY;
        }else
            pastTouchPos = null;
    }

    private void setSpriteScale()
    {
        float yScale = deltaScale * (100 - percentFilled) / 100.0f;
        coverSprite.setScale(xScale, yScale + baseYScale);
//        sprite.setScale(xScale, yScale * percentFilled / 100.0f);
        Rectangle boundingRectangle = coverSprite.getBoundingRectangle();
        spritePos.x -= boundingRectangle.x;
        spritePos.y -= boundingRectangle.y;
//
        coverSprite.setPosition(spritePos.x, spritePos.y);

        // sprite.setPosition(boundingRectangle.x * -1, boundingRectangle.y *
        // -1);
        // System.out.println("setting x to " + boundingRectangle.x * -1);
        // System.out.println("setting y to " + boundingRectangle.y * -1);
    }
}