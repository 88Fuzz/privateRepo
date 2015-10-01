package com.libgdx.airplane.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractDrawable;
import com.libgdx.airplane.game.drawable.airplanes.Player;
import com.libgdx.airplane.game.drawable.buildings.Building;
import com.libgdx.airplane.game.drawable.weapons.Bomb;
import com.libgdx.airplane.game.rendering.WorldRenderer;
import com.libgdx.airplane.game.utils.CollisionDetection;
import com.libgdx.airplane.game.utils.MapDetails;

//TODO make a GameState Abstract/interface that implements ApplicationsAdapter and
//InputProcessor. It should also have an update method.
public class Game extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    private AssetManager assMan;
    private TextureAtlas atlas;
    private Player player;
    private List<Bomb> activeBombs;
    private List<Bomb> activeBullets;
    private List<AbstractDrawable> buildings;
    private float timeSinceLastUpdate;
    private WorldRenderer worldRenderer;
    private MapDetails mapDetails;

    @Override
    public void create()
    {
        mapDetails = new MapDetails(6500);

        assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        player = new Player(atlas, mapDetails, new Vector2(100, 700), new Vector2(0, 0), 1000, 10, 0, 0, 500, 1);
        activeBombs = new LinkedList<Bomb>();
        activeBullets = new LinkedList<Bomb>();

        constructBuildings();

        timeSinceLastUpdate = 0;
        worldRenderer = new WorldRenderer(player);
        worldRenderer.addDrawables(buildings);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();

        timeSinceLastUpdate += dt;
        while(timeSinceLastUpdate > TIMEPERFRAME)
        {
            timeSinceLastUpdate -= TIMEPERFRAME;
            update(TIMEPERFRAME);
            checkCollisions();
        }
        worldRenderer.render();
    }

    // TODO remove this random thing
    static private final Random random = new Random(1);

    public void constructBuildings()
    {
        int numBuildings = 9;
        buildings = new LinkedList<AbstractDrawable>();
        float x = 400;
        float y = 0;

        float width = 80;
        float height = 1000;

        int xMax = 1000;
        int xMin = 400;

        int yMax = 500;
        int yMin = 100;

        for(int j = 0; j < numBuildings; j++)
        {
            buildings.add(new Building(atlas, mapDetails, new Vector2(x, y), width, height));
            height = 500;

            x += random.nextInt(xMax - xMin + 1) + xMin;
            height = random.nextInt(yMax - yMin + 1) + yMin;
            System.out.println("blaaaah " + x + " y: " + y);
        }
    }

    /**
     * Checks the collisions between bomb -> buildings and missiles -> airplanes
     */
    private void checkCollisions()
    {
        // Use iterator to safely remove objects in the list
        for(Iterator<Bomb> it = activeBombs.iterator(); it.hasNext();)
        {
            Bomb bomb = it.next();

            if(!bomb.isAlive())
            {
                it.remove();
                continue;
            }

            for(AbstractDrawable building: buildings)
            {
                if(!building.isAlive())
                {
                    continue;
                }

                if(CollisionDetection.checkCollision(bomb, building))
                {
                    bomb.hit();
                    building.hit();
                }
            }
        }
    }

    private void update(final float dt)
    {
        player.update(dt);
        getActiveWeapons();
    }

    /**
     * For every Movable that can send Weapons of destruction, get their newly
     * placed weapons for collision detection
     */
    private void getActiveWeapons()
    {
        activeBombs.addAll(player.getNewBombs());
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        player.processKeyDown(keyCode);
        return true;
    }

    @Override
    public boolean keyUp(final int keyCode)
    {
        player.processKeyUp(keyCode);
        return true;
    }

    @Override
    public boolean keyTyped(final char character)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(final int amount)
    {
        // TODO Auto-generated method stub
        return false;
    }
}
