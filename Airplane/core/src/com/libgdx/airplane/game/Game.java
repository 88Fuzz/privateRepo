package com.libgdx.airplane.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractDrawable;
import com.libgdx.airplane.game.drawable.airplanes.Airplane;
import com.libgdx.airplane.game.drawable.airplanes.Player;
import com.libgdx.airplane.game.drawable.buildings.Building;
import com.libgdx.airplane.game.drawable.weapons.Bomb;
import com.libgdx.airplane.game.drawable.weapons.Missile;
import com.libgdx.airplane.game.rendering.WorldRenderer;
import com.libgdx.airplane.game.utils.CollisionDetection;
import com.libgdx.airplane.game.utils.MapDetails;
import com.libgdx.airplane.game.utils.RandomNumberUtils;

//TODO make a GameState Abstract/interface that implements ApplicationsAdapter and
//InputProcessor. It should also have an update method.
public class Game extends ApplicationAdapter implements InputProcessor
{
    private static final float TIMEPERFRAME = 1.0f / 60.0f;

    private AssetManager assMan;
    private TextureAtlas atlas;
    private Player player;
    private List<AbstractDrawable> airplanes;
    private List<Bomb> activeBombs;
    private List<Missile> activeBullets;
    private List<Missile> activeMissiles;
    private List<AbstractDrawable> buildings;
    private float timeSinceLastUpdate;
    private WorldRenderer worldRenderer;
    private MapDetails mapDetails;
    private World physicsWorld;

    @Override
    public void create()
    {
        physicsWorld = new World(new Vector2(0, 0), true);

        mapDetails = new MapDetails(6500, -500.8f);

        assMan = new AssetManager();
        assMan.load(TextureConstants.TILE_TEXTURES, TextureAtlas.class);
        assMan.finishLoading();
        atlas = assMan.get(TextureConstants.TILE_TEXTURES);

        player = new Player(atlas, physicsWorld, mapDetails, new Vector2(100, 700), new Vector2(0, 0), 1000, 10, 0,
                500, 5, 5, 1, 2, .25f);
        activeBombs = new LinkedList<Bomb>();
        activeBullets = new LinkedList<Missile>();
        activeMissiles = new LinkedList<Missile>();

        constructBuildings();
        // constructAirplanes();
        airplanes = new LinkedList<AbstractDrawable>();

        timeSinceLastUpdate = 0;
        worldRenderer = new WorldRenderer(physicsWorld, player, true);
        worldRenderer.addDrawables(buildings);
        worldRenderer.addDrawables(airplanes);
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
            physicsWorld.step(TIMEPERFRAME, 8, 3);
            update(TIMEPERFRAME);
            checkCollisions();
        }
        worldRenderer.render();
    }

    private void constructBuildings()
    {
        int numBuildings = 9;
        buildings = new LinkedList<AbstractDrawable>();
        float x = 400;
        float y = 0;

        float width = 80;
        float height = 100;

        int xMax = 1000;
        int xMin = 400;

        int yMax = 500;
        int yMin = 100;

        for(int j = 0; j < numBuildings; j++)
        {
            buildings.add(new Building(atlas, physicsWorld, mapDetails, new Vector2(x, y), width, height));
            height = 500;

            x += RandomNumberUtils.getRandomInt(xMin, xMax);
            height = RandomNumberUtils.getRandomInt(yMin, yMax);
        }
    }

    private void constructAirplanes()
    {
        final int numAirplanes = 20;
        final int numBombs = 0;
        final int numMissiles = 0;
        final int bombDelay = 0;
        final int missileDelay = 0;
        final int bulletDelay = 0;
        final int maxAcceleration = 1000;
        final int maxPitchAcceleration = 0;
        final int pitch = 0;

        airplanes = new LinkedList<AbstractDrawable>();

        for(int j = 0; j < numAirplanes; j++)
        {
            // TODO this needs to be much better at spawning planes
            int x = RandomNumberUtils.getRandomInt(mapDetails.getMapWidth() / 2, mapDetails.getMapWidth());
            int y = RandomNumberUtils.getRandomInt(Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight());
            int singleDimensionVelocity = -1 * RandomNumberUtils.getRandomInt(maxAcceleration / 5, maxAcceleration / 2);

            airplanes.add(new Airplane(atlas, physicsWorld, mapDetails, new Vector2(x, y), new Vector2(0, 0),
                    maxAcceleration, maxPitchAcceleration, pitch, singleDimensionVelocity, numBombs, numMissiles,
                    bombDelay, missileDelay, bulletDelay));
        }
    }

    /**
     * Checks the collisions between bomb -> buildings and missiles -> airplanes
     * This should be replaced by a) Box2d physics engine or b) some kind of
     * tree that will only check for collisions with other objects in the node.
     */
    private void checkCollisions()
    {
        // Use iterator to safely remove objects in the list
        for(Iterator<Bomb> it = activeBombs.iterator(); it.hasNext();)
        {
            Bomb bomb = it.next();
            int attackDamage = bomb.getAttackDamage();

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
                    bomb.hit(attackDamage);
                    building.hit(attackDamage);
                }
            }

            for(AbstractDrawable airplane: airplanes)
            {
                if(!airplane.isAlive())
                {
                    continue;
                }

                if(CollisionDetection.checkCollision(bomb, airplane))
                {
                    bomb.hit(attackDamage);
                    airplane.hit(attackDamage);
                }
            }
        }

        // Use iterator to safely remove objects in the list
        for(Iterator<Missile> it = activeMissiles.iterator(); it.hasNext();)
        {
            Missile missile = it.next();
            int attackDamage = missile.getAttackDamage();

            if(!missile.isAlive())
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

                if(CollisionDetection.checkCollision(missile, building))
                {
                    missile.hit(attackDamage);
                    building.hit(attackDamage);
                }
            }

            for(AbstractDrawable airplane: airplanes)
            {
                if(!airplane.isAlive())
                {
                    continue;
                }

                if(CollisionDetection.checkCollision(missile, airplane))
                {
                    missile.hit(attackDamage);
                    airplane.hit(attackDamage);
                }
            }
        }

        // Use iterator to safely remove objects in the list
        for(Iterator<Missile> it = activeBullets.iterator(); it.hasNext();)
        {
            Missile bullet = it.next();
            int attackDamage = bullet.getAttackDamage();

            if(!bullet.isAlive())
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

                if(CollisionDetection.checkCollision(bullet, building))
                {
                    bullet.hit(attackDamage);
                    building.hit(attackDamage);
                }
            }

            for(AbstractDrawable airplane: airplanes)
            {
                if(!airplane.isAlive())
                {
                    continue;
                }

                if(CollisionDetection.checkCollision(bullet, airplane))
                {
                    bullet.hit(attackDamage);
                    airplane.hit(attackDamage);
                }
            }
        }
    }

    private void update(final float dt)
    {
        player.update(dt);
        getActiveWeapons();

        // Use iterator to safely remove objects in the list
        for(Iterator<AbstractDrawable> it = airplanes.iterator(); it.hasNext();)
        {
            AbstractDrawable airplane = it.next();

            if(!airplane.isAlive())
            {
                it.remove();
                continue;
            }

            airplane.update(dt);
        }
    }

    /**
     * For every Movable that can send Weapons of destruction, get their newly
     * placed weapons for collision detection
     */
    private void getActiveWeapons()
    {
        activeBombs.addAll(player.getNewBombs());
        activeBullets.addAll(player.getNewBullets());
        activeMissiles.addAll(player.getNewMissiles());
    }

    @Override
    public void resize(int width, int height)
    {
        worldRenderer.update(width, height);
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

    // TODO figure out what needs to be disposed
    @Override
    public void dispose()
    {
        worldRenderer.dispose();
    }
}
