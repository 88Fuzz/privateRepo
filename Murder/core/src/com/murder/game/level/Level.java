package com.murder.game.level;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.murder.game.drawing.Drawable;
import com.murder.game.level.room.Room;

public class Level extends Drawable
{
    private Map<String, List<Room>> rooms;
    private List<List<Tile>> tiles;

    public Level(final Map<String, List<Room>> rooms, final List<List<Tile>> tiles)
    {
        this.tiles = tiles;
        this.rooms = rooms;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.draw(batch);
            }
        }

        for(final List<Room> roomList: rooms.values())
        {
            for(final Room room: roomList)
            {
                room.draw(batch);
            }
        }
    }

    @Override
    public void update(final float dt)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.update(dt);
            }
        }
    }

    public Rectangle getLevelBounds()
    {
        // TODO this width is hard coded. FIX THAT.
        return new Rectangle(0, 0, tiles.size() * 200, tiles.get(0).size() * 200);
    }

    public Tile getTile(final int x, final int y)
    {
        final List<Tile> list = tiles.get(x);
        return list.get(y);
    }

    public List<Room> getRooms(final String roomId)
    {
        return new LinkedList<Room>();
        // return rooms.get(roomId);
    }
}