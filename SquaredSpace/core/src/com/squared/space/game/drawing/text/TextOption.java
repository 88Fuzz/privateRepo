package com.squared.space.game.drawing.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.game.constants.TextureConstants;

public class TextOption extends Text
{
    // TODO find a better way to do this, maybe a ratio of
    // gdx.graphics.getHeight()?
    private static final int INITIAL_OFFSET = 60;
    private static final int OPTION_OFFSET = 14;
    private static final int PIXELS_PER_LINE = 17;
    private static final String NEWLINE = "\n";
    private int option;
    private boolean drawOptions;
    private int yOffset;
    private List<Text> textOptions;
    private Sprite highLightOption;

    public TextOption()
    {
        super();
        option = 0;
        // TODO figure out the max that can fit on screen
        textOptions = new ArrayList<Text>();
    }

    @Override
    public void init(final TextureAtlas atlas, final BitmapFont font, final Vector2 position, final Vector2 namePosition)
    {
        super.init(atlas, font, position, namePosition);

        highLightOption = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        highLightOption.setColor(1, 0, 0, 1);
        yOffset = (int) position.y - text.split(NEWLINE).length * INITIAL_OFFSET;
        drawOptions = false;
    }

    public void setTextOptions(final List<Text> textOptions)
    {
        this.textOptions.addAll(textOptions);
    }

    @Override
    public void setTexts(final String text, final String name)
    {
        super.setTexts(text, "");
    }

    @Override
    public boolean selectionUp()
    {
        option--;
        if(option < 0)
        {
            option = textOptions.size() - 1;
        }

        return true;
    }

    @Override
    public boolean selectionDown()
    {
        option++;

        if(option > textOptions.size() - 1)
        {
            option = 0;
        }

        return true;
    }

    @Override
    public void finishText()
    {
        super.finishText();
        drawOptions = true;
    }

    @Override
    public Text getNextText()
    {
        super.getNextText();
        final int nextOption = option;
        option = 0;
        return textOptions.get(nextOption).getNextText();
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        super.draw(batch);

        if(drawOptions)
        {
            int tmpYOffset = yOffset;
            for(int j = 0; j < textOptions.size(); j++)
            {
                if(j == option)
                {
                    final int height = (OPTION_OFFSET
                            + (textOptions.get(j).getText().split(NEWLINE).length * PIXELS_PER_LINE));
                    highLightOption.setBounds(0, tmpYOffset - height, Gdx.graphics.getWidth(), height);
                    highLightOption.draw(batch);
                }

                font.draw(batch, textOptions.get(j).getText(), position.x, tmpYOffset);
                tmpYOffset -= (OPTION_OFFSET + (textOptions.get(j).getText().split(NEWLINE).length * PIXELS_PER_LINE));
            }
        }
    }
}