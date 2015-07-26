package com.circleboy.gamestates;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

        tmpLayer = new Layer(atlas, LayerType.PEOPLE, 0);
//        Sprite sprite = Square.generateSprite(atlas, 170, 170, 170, 170);
//        Square moveable = new Square(100, 700, sprite, 0, 0, null);
//        Square moveable = new Square(movementFactor, movementFactor, sprite, movementFactor, movementFactor, null);
//        public Square(final float x, final float y, final Sprite sprite, final float baseScreenMovement,
//            final float baseMovement, final TextChangeEvent textEvent)
//        moveable.setText("Fuck yo couch!");
//        tmpLayer.addMoveable(moveable);
        scene.put(LayerType.PEOPLE, tmpLayer);

        tmpLayer = new Layer(atlas, LayerType.CIRCLE, 1);
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
