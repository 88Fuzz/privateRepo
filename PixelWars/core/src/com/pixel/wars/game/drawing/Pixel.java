package com.pixel.wars.game.drawing;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pixel.wars.game.config.PixelConfig;
import com.pixel.wars.game.config.TeamConfig.TeamConfigKeys;
import com.pixel.wars.game.rendering.TextureConstants;
import com.pixel.wars.game.utils.RandomUtil;

public class Pixel implements Drawable
{
    public enum Team
    {
        PLAYER(Color.RED), OTHER(Color.YELLOW), DEBUG(Color.GREEN);

        private final Color color;

        private Team(final Color color)
        {
            this.color = color;
        }

        public Color getColor()
        {
            return color;
        }
    }

    private PixelConfig pixelConfig;
    private Map<TeamConfigKeys, Float> teamConfig;
    private final Sprite sprite;
    private final RandomUtil numberGenerator;
    private Rectangle rectangle;
    private Vector2 gridPosition;
    private Team team;
    private float health;

    // TODO if player health is greater than max, have it slowly move back down
    // to max
    // TODO input parameters like health and percentReviveHealth should be read
    // in from a Context class that will have all this information instead of
    // passing things in one by one
    public Pixel(final TextureAtlas atlas, final PixelConfig pixelConfig, final float x, final float y, final float size, final Team team)
    {
        numberGenerator = new RandomUtil();
        gridPosition = new Vector2();
        sprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        init(new Rectangle(x * size, y * size, size, size), pixelConfig, team);
    }

    public void init(final Rectangle rectangle, final PixelConfig pixelConfig, final Team team)
    {
        this.pixelConfig = pixelConfig;
        setTeam(team);
        this.health = teamConfig.get(TeamConfigKeys.HEALTH);
        this.rectangle = rectangle;
        this.gridPosition.x = rectangle.x / rectangle.width;
        this.gridPosition.y = rectangle.y / rectangle.height;

        sprite.setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void takeOver(final float health, final Team team)
    {
        this.health = health;
        setTeam(team);
    }

    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public Color getColor()
    {
        return team.getColor();
    }

    public Vector2 getGridPosition()
    {
        return gridPosition;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(final Team team)
    {
        this.team = team;
        this.teamConfig = pixelConfig.getTeamConfigValues(team);
        sprite.setColor(team.getColor());
    }

    public void attack(final float attack, final Team attackingTeam)
    {
        health -= (attack - getDefense());
        if(health < 1)
        {
            setTeam(attackingTeam);
            health = teamConfig.get(TeamConfigKeys.HEALTH) * teamConfig.get(TeamConfigKeys.RES_HEALTH);
        }
    }

    private float getDefense()
    {
        return numberGenerator.getFloat(teamConfig.get(TeamConfigKeys.MIN_DEFENSE), teamConfig.get(TeamConfigKeys.MAX_DEFENSE));
    }

    public float getAttack()
    {
        float attack = numberGenerator.getFloat(teamConfig.get(TeamConfigKeys.MIN_ATTACK), teamConfig.get(TeamConfigKeys.MAX_ATTACK));
        if(isCrit())
        {
            attack *= teamConfig.get(TeamConfigKeys.CRIT_MULTIPLIER);
        }

        return attack;
    }

    private boolean isCrit()
    {
        return numberGenerator.getFloat(0, 1) < teamConfig.get(TeamConfigKeys.CRIT_PERCENT);
//        final float random = numberGenerator.getFloat(0, 1);
//        final float percent = teamConfig.get(TeamConfigKeys.CRIT_PERCENT);
//        return random < percent;
    }
}