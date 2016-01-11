package com.pixel.wars.game.config;

import java.util.HashMap;
import java.util.Map;

import com.pixel.wars.game.drawing.Pixel.Team;

//TODO Have the player be able to upgrade their damage done, and % of health after take over, and everything else (THIS MAKE THESE UPGRADE-ABLE)
public class PixelConfig
{
    private final Map<Team, Map<PixelConfigKeys, Float>> pixelConfig;

    public PixelConfig()
    {
        pixelConfig = new HashMap<Team, Map<PixelConfigKeys, Float>>();
    }

    public void setTeamConfigValues(final Team team, final TeamConfig teamConfig)
    {
        pixelConfig.put(team, getConfigForLevel(teamConfig));
    }

    public Map<PixelConfigKeys, Float> getTeamConfigValues(final Team team)
    {
        return pixelConfig.get(team);
    }

    private static final int BASE_HEALTH = 100;
    private static final float MULTI_HEALTH = 1.07f;

    // TODO this should have a cap
    private static final float BASE_PERCENT_RES_HEALTH = 0.3f;
    private static final float MULTI_PRECENT_RES_HEALTH = 1.05f;

    private static final float BASE_MAX_ATTACK = 20;
    private static final float MULTI_MAX_ATTACK = 1.05f;

    private static final float BASE_MIN_ATTACK = 10;
    private static final float MULTI_MIN_ATTACK = 1.0489f;

    // TODO this should have a cap
    private static final float BASE_CRIT_PERCENT = 0.01f;
    private static final float MULTI_CRIT_PERCENT = 1.03f;

    // TODO this should have a cap
    private static final float BASE_CRIT_MULTIPLIER = 1.5f;
    private static final float MULTI_CRIT_MULTIPLIER = 1.05f;

    private static final float BASE_MAX_DEFENSE = 5;
    private static final float MULTI_MAX_DEFENSE = 1.03f;

    private static final float BASE_MIN_DEFENSE = 1;
    private static final float MULTI_MIN_DEFENSE = 1.029f;

    private static final float BASE_PARA_PIXEL_HEALTH_MULTIPLIER = 1.5f;
    private static final float MULTI_PARA_PIXEL_HEALTH_MULTIPLIER = 1.01f;

    private static final float BASE_PIXEL_PUNCH_HEALTH_MULTIPLIER = 1.5f;
    private static final float MULTI_PIXEL_PUNCH_HEALTH_MULTIPLIER = 1.01f;

    public enum PixelConfigKeys
    {
        HEALTH(BASE_HEALTH, MULTI_HEALTH), RES_HEALTH(BASE_PERCENT_RES_HEALTH, MULTI_PRECENT_RES_HEALTH), MAX_ATTACK(BASE_MAX_ATTACK, MULTI_MAX_ATTACK), MIN_ATTACK(BASE_MIN_ATTACK, MULTI_MIN_ATTACK), CRIT_PERCENT(
                BASE_CRIT_PERCENT, MULTI_CRIT_PERCENT), CRIT_MULTIPLIER(BASE_CRIT_MULTIPLIER, MULTI_CRIT_MULTIPLIER), MAX_DEFENSE(BASE_MAX_DEFENSE, MULTI_MAX_DEFENSE), MIN_DEFENSE(BASE_MIN_DEFENSE,
                MULTI_MIN_DEFENSE), PARA_PIXEL_HEALTH_MULTIPLIER(BASE_PARA_PIXEL_HEALTH_MULTIPLIER, MULTI_PARA_PIXEL_HEALTH_MULTIPLIER), PIXEL_PUNCH_HEALTH_MULTIPLIER(
                BASE_PIXEL_PUNCH_HEALTH_MULTIPLIER, MULTI_PIXEL_PUNCH_HEALTH_MULTIPLIER);

        private final float baseValue;
        private final float multiplierValue;

        private PixelConfigKeys(final float baseValue, final float multiplierValue)
        {
            this.baseValue = baseValue;
            this.multiplierValue = multiplierValue;
        }

        public float getBaseValue()
        {
            return baseValue;
        }

        public float getMultiplierValue()
        {
            return multiplierValue;
        }
    };

    public static Map<PixelConfigKeys, Float> getConfigForLevel(final TeamConfig teamConfig)
    {
        final Map<PixelConfigKeys, Float> map = new HashMap<PixelConfigKeys, Float>();
        for(final PixelConfigKeys key: PixelConfigKeys.values())
        {
            map.put(key, getLinearValue(key, teamConfig.getStatLevel(key)));
        }

        return map;
    }

    private static float getLinearValue(final PixelConfigKeys key, final float level)
    {
        return (float) (Math.pow(key.getMultiplierValue(), level) * key.getBaseValue());
    }
}