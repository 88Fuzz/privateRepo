package com.murder.game.drawing.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.murder.game.constants.drawing.TextureType;

public class TextureManager
{
    final Map<TextureType, Texture> textureMap;

    public TextureManager()
    {
        textureMap = new HashMap<TextureType, Texture>();
    }

    public Texture getTexture(final TextureType textureName)
    {
        Texture texture = textureMap.get(textureName);
        if(texture != null)
            return texture;

        texture = new Texture(Gdx.files.internal(textureName.getFileName()));
        textureMap.put(textureName, texture);

        return texture;
    }

    public void dispose()
    {
        for(final Entry<TextureType, Texture> entry: textureMap.entrySet())
        {
            if(entry.getValue() != null)
                entry.getValue().dispose();
        }
    }
}