package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.serialize.MyVector2;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Text.class, name = "Text"), @Type(value = PercentageText.class, name = "PercentageText") })
public class Text extends NonBodyDrawable
{
    private static final String POSITION = "position";
    protected static final String FONT_TYPE = "fontType";
    protected static final String TEXT = "text";
    protected static final String ROTATION = "rotation";

    @JsonIgnore
    private BitmapFont font;
    private FontType fontType;
    private String text;

    public Text(@JsonProperty(POSITION) final MyVector2 position, @JsonProperty(FONT_TYPE) final FontType fontType,
            @JsonProperty(TEXT) final String text, @JsonProperty(ROTATION) final float rotation)
    {
        super(position, rotation);
        this.fontType = fontType;
        this.text = text;
    }

    public void init(final FontManager fontManager)
    {
        font = fontManager.getFont(fontType);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, text, position.x, position.y);
    }

    @Override
    protected void updateCurrent(float dt)
    {
        // do nothing
    }

    public FontType getFontType()
    {
        return fontType;
    }

    public String getText()
    {
        return text;
    }
}