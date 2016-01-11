package com.pixel.wars.game.config;

import java.util.HashMap;
import java.util.Map;

import com.pixel.wars.game.config.PixelConfig.PixelConfigKeys;

/**
 * Class that holds what a team's attribute levels are at. Attribute levels are
 * defined in PixelConfig.
 * 
 */
public class TeamConfig
{
    private static final float DEFAULT_LEVEL = 0;
    private final Map<PixelConfigKeys, Float> configMap;

    public TeamConfig()
    {
        this.configMap = new HashMap<PixelConfigKeys, Float>();
        init();
    }

    public void init()
    {
        init(DEFAULT_LEVEL);
    }

    public void init(final float value)
    {
        for(final PixelConfigKeys key: PixelConfigKeys.values())
        {
            configMap.put(key, value);
        }
    }

    // TODO come up with an equations for this and not just all +1, so that
    // levels at beginning are < 1 but later is > 1
    public void incrementAllStats()
    {
        for(final Map.Entry<PixelConfigKeys, Float> entry: configMap.entrySet())
        {
            configMap.put(entry.getKey(), entry.getValue() + 1);
        }
    }

    public void incrementSingleStatByOne(final PixelConfigKeys key)
    {
        incrementSingleStat(key, 1);
    }

    public void incrementSingleStat(final PixelConfigKeys key, final float value)
    {
        configMap.put(key, configMap.get(key) + value);
    }

    public float getStatLevel(final PixelConfigKeys key)
    {
        return configMap.get(key);
    }
}