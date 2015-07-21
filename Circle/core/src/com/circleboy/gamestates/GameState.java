package com.circleboy.gamestates;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.util.ScalingUtil;
import com.circleboy.util.definitions.DrawableEventDefinitions;
import com.circleboy.util.definitions.LayerEventDefinitions;
import com.circleboy.util.definitions.TextureConstants;
import com.circleboy.moveable.Layer;
import com.circleboy.moveable.Layer.LayerType;
import com.circleboy.moveable.Moveable;

public class GameState extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private AssetManager assMan;
    private LinkedHashMap<LayerType, Layer> scene;
    private Moveable circle;
    private float timeSinceLastUpdate;
    private float movementFactor;
    private Random rando;

    @Override
    public void create()
    {
        ScalingUtil.setScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        timeSinceLastUpdate = 0;
        movementFactor = 0;
        batch = new SpriteBatch();
        assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        rando = new Random();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        scene = new LinkedHashMap<LayerType, Layer>();
        constructScene();
        LayerEventDefinitions.configureLayerEvent(scene, atlas);
        DrawableEventDefinitions.initializeDrawableEventDefinitions();

        circle = scene.get(LayerType.CIRCLE).getFirstInList();

        Gdx.input.setInputProcessor(this);
    }

    private void constructScene()
    {
        Layer tmpLayer = new Layer(atlas, LayerType.BACKGROUND, 3);
        scene.put(LayerType.BACKGROUND, tmpLayer);

        tmpLayer = new Layer(atlas, LayerType.CIRCLE, 1);
        Sprite sprite = new Sprite(atlas.findRegion(TextureConstants.SQUARE_KEY));
        sprite.setColor(1, 1, 0, 1);

//        System.out.println("before " + sprite.getBoundingRectangle() + " width " + sprite.getWidth());
//        final Vector2 scale = ScalingUtil.getScale();
        sprite.setScale(.5f, .5f);
//        System.out.println("after " + sprite.getBoundingRectangle() + " width " + sprite.getWidth());
        sprite.setScale(1.6f, 1.6f);
//        System.out.println("second after: " + sprite.getBoundingRectangle() + " width " + sprite.getWidth());
        sprite.setScale(ScalingUtil.getScale().x, ScalingUtil.getScale().y);
//        System.out.println("third after: " + sprite.getBoundingRectangle() + " width " + sprite.getWidth());

        rando.nextInt(200);
        Moveable moveable = new Moveable(0, 700, sprite, 0, 0);
        tmpLayer.addMoveable(moveable);
        scene.put(LayerType.CIRCLE, tmpLayer);
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
        Gdx.gl.glClearColor(0, 38 / 255f, 87 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for(Entry<LayerType, Layer> entry: scene.entrySet())
        {
            Layer layer = entry.getValue();
            layer.draw(batch);
        }
        batch.end();
    }

    private void update(float dt)
    {
        for(Entry<LayerType, Layer> entry: scene.entrySet())
        {
            Layer layer = entry.getValue();
            layer.update(circle, dt, movementFactor);
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        // TODO make this an exponential decay
        movementFactor += 1;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        // TODO make this an exponential decay
        movementFactor = Math.max(--movementFactor, 0);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
