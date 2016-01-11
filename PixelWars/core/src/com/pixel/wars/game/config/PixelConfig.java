package com.pixel.wars.game.config;

import java.util.HashMap;
import java.util.Map;

import com.pixel.wars.game.config.TeamConfig.TeamConfigKeys;
import com.pixel.wars.game.drawing.Pixel.Team;

public class PixelConfig
{
    private final Map<Team, Map<TeamConfigKeys, Float>> pixelConfig;

    public PixelConfig()
    {
        pixelConfig = new HashMap<Team, Map<TeamConfigKeys, Float>>();
    }

    public void setTeamConfigValues(final Team team, final float level)
    {
        pixelConfig.put(team, TeamConfig.getConfigForLevel(level));
    }

    public Map<TeamConfigKeys, Float> getTeamConfigValues(final Team team)
    {
        return pixelConfig.get(team);
    }
}