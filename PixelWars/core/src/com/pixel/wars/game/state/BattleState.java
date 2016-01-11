package com.pixel.wars.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.pixel.wars.game.config.PixelConfig;
import com.pixel.wars.game.data.Pixels;
import com.pixel.wars.game.drawing.Pixel;
import com.pixel.wars.game.drawing.Pixel.Team;
import com.pixel.wars.game.powers.PowerProcessor;
import com.pixel.wars.game.rendering.WorldRenderer;
import com.pixel.wars.game.utils.ProcessingUtils;

//TODO Add count for number of player1 and number of other on the board in GUI
public class BattleState implements State
{
    private StateManager stateManager;
    private PixelConfig pixelConfig;
    private PowerProcessor powerProcessor;
    private Pixels pixels;
    private boolean finished;

    public BattleState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
        powerProcessor = new PowerProcessor();

        Gdx.input.setInputProcessor(new GestureDetector(20, 0.25f, 0.25f, 0.15f, powerProcessor));
    }

    public void init(final WorldRenderer worldRenderer, final Pixels pixels, final PixelConfig pixelConfig, final TextureAtlas atlas)
    {
        finished = false;
        this.pixelConfig = pixelConfig;
        this.pixels = pixels;
        powerProcessor.init(pixels, worldRenderer, pixelConfig);
    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(float dt)
    {
        powerProcessor.update(dt);
        finished = true;

        for(final Pixel attackPixel: pixels.getPixels())
        {
            final Vector2 gridPosition = attackPixel.getGridPosition();
            for(int i = (int) gridPosition.x - 1; i < gridPosition.x + 2; i++)
            {
                if(i < 0 || i >= pixels.getWidth())
                {
                    continue;
                }

                for(int j = (int) gridPosition.y - 1; j < gridPosition.y + 2; j++)
                {
                    if(j < 0 || j >= pixels.getHeight() || (i == gridPosition.x && j == gridPosition.y))
                    {
                        continue;
                    }
                    final Pixel defensePixel = pixels.getPixels().get(ProcessingUtils.get1d(i, j, pixels.getHeight()));

                    final Team attackTeam = attackPixel.getTeam();
                    final Team defenseTeam = defensePixel.getTeam();

                    if(attackTeam != defenseTeam)
                    {
                        finished = false;
                        attackPixel.attack(defensePixel.getAttack(), defenseTeam);
                        defensePixel.attack(attackPixel.getAttack(), attackTeam);
                    }
                }
            }
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.render(pixels.getPixels());
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    public boolean isFinished()
    {
        return finished;
    }

    public Team getWinner()
    {
        return pixels.getPixels().get(0).getTeam();
    }
}