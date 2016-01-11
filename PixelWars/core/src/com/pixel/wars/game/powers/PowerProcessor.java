package com.pixel.wars.game.powers;

import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.pixel.wars.game.config.PixelConfig;
import com.pixel.wars.game.config.TeamConfig.TeamConfigKeys;
import com.pixel.wars.game.data.Pixels;
import com.pixel.wars.game.drawing.Pixel;
import com.pixel.wars.game.drawing.Pixel.Team;
import com.pixel.wars.game.rendering.WorldRenderer;
import com.pixel.wars.game.utils.GraphicsUtils;
import com.pixel.wars.game.utils.ProcessingUtils;

//TODO the wait times here should be part of the TeamConfig class.
//TODO in gameplay, to gain an extra punch or an extra para drop, 1000 points must be achieved. Wtih a winning battle being 2 points and a losing battle being 1 point?
//TODO this should probably be an interface with every power to implement specific methods. And the PowerProcessor will iterate over all available powers.
public class PowerProcessor implements GestureListener
{
    // ParaPixel Constants
    private static final int MAX_PARAPIXEL_DROPS = 20;
    private static final float PARAPIXEL_WAIT_TIME = 0.08f;

    // PixelPunch Constants
    private static final float FLING_RANGE = (float) Math.toRadians(10);
    private static final int PUNCH_CHANGES_PER_UPDATE = 1;
    private static final float PUNCH_CHANGE_WAIT_TIME = 0.008f;

    private final Random numberGenerator;
    private final Vector2 lastTouch;
    private WorldRenderer worldRenderer;
    private Pixels pixels;

    // ParaPixel variables
    private float paraPixelWaitTime;
    private boolean paraPixel;
    private int paraPixelDrops;
    private Vector2 paraPixelBaseLocation;

    // PixelPunch variables
    private float pixelPunchWaitTime;
    private boolean pixelPunch;
    private Vector2 pixelPunchLocation;
    private Map<TeamConfigKeys, Float> teamConfigValues;

    public PowerProcessor()
    {
        numberGenerator = new Random();
        lastTouch = new Vector2();
    }

    public void init(final Pixels pixels, final WorldRenderer worldRenderer, final PixelConfig pixelConfig)
    {
        this.pixels = pixels;
        this.worldRenderer = worldRenderer;
        teamConfigValues = pixelConfig.getTeamConfigValues(Team.PLAYER);

        paraPixel = false;
        paraPixelDrops = 0;
        paraPixelWaitTime = -1;

        pixelPunch = false;
        pixelPunchWaitTime = 0;
    }

    public void update(final float dt)
    {
        if(paraPixel)
        {
            processParaPixels(dt);
        }

        if(pixelPunch)
        {
            processPixelPunch(dt);
        }
    }

    private void processParaPixels(final float dt)
    {
        final int variation = 20;

        if(paraPixelDrops >= MAX_PARAPIXEL_DROPS)
        {
            paraPixel = false;
            return;
        }

        if(paraPixelWaitTime < 0)
        {

            final int x = getRandomNumberInGrid((int) paraPixelBaseLocation.x, variation, pixels.getWidth());
            final int y = getRandomNumberInGrid((int) paraPixelBaseLocation.y, variation, pixels.getHeight());
            final int newPixelOffset = 1;
            convertPixels(x, y, newPixelOffset, getSpecialHealth(teamConfigValues.get(TeamConfigKeys.PARA_PIXEL_HEALTH_MULTIPLIER)));

            paraPixelDrops++;
            paraPixelWaitTime = PARAPIXEL_WAIT_TIME;
        }
        paraPixelWaitTime -= dt;
    }

    private void convertPixels(final int x, final int y, final int radius, final float health)
    {
        for(int i = x - radius; i < x + radius + 1; i++)
        {
            if(i < 0 || i >= pixels.getWidth())
            {
                continue;
            }

            for(int j = y - radius; j < y + radius + 1; j++)
            {
                if(j < 0 || j >= pixels.getHeight())
                {
                    continue;
                }
                final int get1d = ProcessingUtils.get1d(i, j, pixels.getHeight());
                final Pixel pixelDrop = pixels.getPixels().get(get1d);
                pixelDrop.takeOver(health, Team.PLAYER);
            }
        }

    }

    private void processPixelPunch(final float dt)
    {
        if(pixelPunchLocation.x >= pixels.getWidth())
        {
            pixelPunch = false;
            return;
        }

        if(pixelPunchWaitTime < 0)
        {
            pixelPunchWaitTime = PUNCH_CHANGE_WAIT_TIME;

            int i = 0;
            for(i = (int) pixelPunchLocation.x; i < pixelPunchLocation.x + PUNCH_CHANGES_PER_UPDATE; i++)
            {
                if(i >= pixels.getWidth())
                {
                    break;
                }

                convertPixels(i, (int) pixelPunchLocation.y, 1, getSpecialHealth(teamConfigValues.get(TeamConfigKeys.PIXEL_PUNCH_HEALTH_MULTIPLIER)));
            }
            pixelPunchLocation.x = i;
        }
        pixelPunchWaitTime -= dt;
    }

    private float getSpecialHealth(final float multiplier)
    {
        return teamConfigValues.get(TeamConfigKeys.HEALTH) * multiplier;
    }

    // TODO, figure out if we should actually loop forever
    private int getRandomNumberInGrid(final int offset, final int variation, final int limit)
    {
        int number;
        do
        {
            number = offset + (numberGenerator.nextInt(variation) - (int) (variation / 2));
        }
        while(number < 0 || number >= limit);

        return number;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        lastTouch.x = x;
        lastTouch.y = y;
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float screenX, float screenY)
    {
        final Vector2 touchedTile = getTouchedTile(screenX, screenY);

        if(!paraPixel)
        {
            paraPixel = true;
            paraPixelDrops = 0;
            paraPixelBaseLocation = touchedTile;
            paraPixelWaitTime = -1;
        }

        return true;
    }

    // TODO If pixel hits the far right edge, have small explosion on the right
    // side
    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        final double angle = Math.atan2(velocityY, velocityX);
        if(angle < FLING_RANGE && angle > -1 * FLING_RANGE)
        {
            if(!pixelPunch)
            {
                pixelPunch = true;
                pixelPunchLocation = getTouchedTile(lastTouch.x, lastTouch.y);
                pixelPunchWaitTime = -1;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    private Vector2 getTouchedTile(final float x, final float y)
    {
        final Vector2 touch = GraphicsUtils.getNormalizedScreenTouch((int) x, (int) y, worldRenderer.getCameraPosition());
        final float cameraZoom = worldRenderer.getCameraZoom();
        final Vector2 touchedTile = new Vector2((int) (touch.x / (pixels.getSize() / cameraZoom)), (int) (touch.y / (pixels.getSize() / cameraZoom)));

        return touchedTile;
    }
}