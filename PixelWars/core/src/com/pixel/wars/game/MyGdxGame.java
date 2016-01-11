package com.pixel.wars.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.pixel.wars.game.drawing.Pixel;
import com.pixel.wars.game.drawing.Pixel.Team;
import com.pixel.wars.game.powers.PowerProcessor;
import com.pixel.wars.game.rendering.TextureConstants;
import com.pixel.wars.game.rendering.WorldRenderer;
import com.pixel.wars.game.utils.GraphicsUtils;
import com.pixel.wars.game.utils.ProcessingUtils;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor
{
    private static final int DEFAULT_HEALTH = 100;
    private static final int BASE_HEIGHT = 100;
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    private TextureAtlas atlas;
    private WorldRenderer worldRenderer;
    private PowerProcessor powerProcessor;
    private List<Pixel> pixels;
    private float tileSize;
    private int height;
    private int width;
    private float timeSinceLastUpdate;

    @Override
    public void create()
    {
        final AssetManager assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        worldRenderer = new WorldRenderer();

        pixels = new ArrayList<Pixel>((int) (width * height));
        populateTeams(BASE_HEIGHT);

//        powerProcessor = new PowerProcessor(pixels, worldRenderer, tileSize, width, height, DEFAULT_HEALTH);
        Gdx.input.setInputProcessor(new GestureDetector(20, 0.25f, 0.25f, 0.15f, powerProcessor));
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
//        worldRenderer.render(pixels);
    }

    private void update(final float dt)
    {
        for(final Pixel attackPixel: pixels)
        {
            final Vector2 gridPosition = attackPixel.getGridPosition();
            for(int i = (int) gridPosition.x - 1; i < gridPosition.x + 2; i++)
            {
                if(i < 0 || i >= width)
                {
                    continue;
                }

                for(int j = (int) gridPosition.y - 1; j < gridPosition.y + 2; j++)
                {
                    if(j < 0 || j >= height || (i == gridPosition.x && j == gridPosition.y))
                    {
                        continue;
                    }
                    final Pixel defensePixel = pixels.get(ProcessingUtils.get1d(i, j, height));

                    final Team attackTeam = attackPixel.getTeam();
                    final Team defenseTeam = defensePixel.getTeam();

                    if(attackTeam != defenseTeam)
                    {
                        attackPixel.attack(defensePixel.getAttack(), defenseTeam);
                        defensePixel.attack(attackPixel.getAttack(), attackTeam);
                    }
                }
            }
        }

        powerProcessor.update(dt);
    }

    private void populateTeams(final int height)
    {
        this.height = height;

        // TODO this truncation will lead to some amount of pixels not fitting
        // to the end of the screen
        tileSize = Gdx.graphics.getHeight() / height;
        width = (int) (Gdx.graphics.getWidth() / tileSize);
        float numBlacks = (width * height) / 2;

        int count = 0;
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                final Team team;

                if(count++ < numBlacks)
                {
                    team = Team.PLAYER;
                }
                else
                {
                    team = Team.OTHER;
                }

//                pixels.add(new Pixel(atlas, DEFAULT_HEALTH, i, j, tileSize, team));
            }
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        populateTeams(BASE_HEIGHT);
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
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