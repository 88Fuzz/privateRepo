package com.pixel.wars.game.config;

import java.util.HashMap;
import java.util.Map;

//TODO Have the player be able to upgrade their damage done, and % of health after take over, and everything else (THIS MAKE THESE UPGRADE-ABLE)
public class TeamConfig
{
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

    public enum TeamConfigKeys
    {
        HEALTH(BASE_HEALTH, MULTI_HEALTH), RES_HEALTH(BASE_PERCENT_RES_HEALTH, MULTI_PRECENT_RES_HEALTH), MAX_ATTACK(BASE_MAX_ATTACK, MULTI_MAX_ATTACK), MIN_ATTACK(BASE_MIN_ATTACK, MULTI_MIN_ATTACK), CRIT_PERCENT(
                BASE_CRIT_PERCENT, MULTI_CRIT_PERCENT), CRIT_MULTIPLIER(BASE_CRIT_MULTIPLIER, MULTI_CRIT_MULTIPLIER), MAX_DEFENSE(BASE_MAX_DEFENSE, MULTI_MAX_DEFENSE), MIN_DEFENSE(BASE_MIN_DEFENSE,
                MULTI_MIN_DEFENSE), PARA_PIXEL_HEALTH_MULTIPLIER(BASE_PARA_PIXEL_HEALTH_MULTIPLIER, MULTI_PARA_PIXEL_HEALTH_MULTIPLIER), PIXEL_PUNCH_HEALTH_MULTIPLIER(
                BASE_PIXEL_PUNCH_HEALTH_MULTIPLIER, MULTI_PIXEL_PUNCH_HEALTH_MULTIPLIER);

        private final float baseValue;
        private final float multiplierValue;

        private TeamConfigKeys(final float baseValue, final float multiplierValue)
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

    public static Map<TeamConfigKeys, Float> getConfigForLevel(final float level)
    {
        final Map<TeamConfigKeys, Float> map = new HashMap<TeamConfigKeys, Float>();
        for(final TeamConfigKeys key: TeamConfigKeys.values())
        {
            map.put(key, getLinearValue(key, level));
        }

        return map;
    }

    private static float getLinearValue(final TeamConfigKeys key, final float level)
    {
        return (float) (Math.pow(key.getMultiplierValue(), level) * key.getBaseValue());
    }
}