package com.murder.game.level.pathfinder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.utils.MathUtils;

public class PathFinder
{
    private class TileComparator implements Comparator<Tile>
    {
        @Override
        public int compare(final Tile x, final Tile y)
        {
            float difference = getFDistance(x) - getFDistance(y);
            if(difference < 0)
                return -1;
            else if(difference > 0)
                return 1;

            /*
             * If the F value is the same, fall back and the one that is closest
             * to the end to be picked next in the list.
             */
            difference = getEndDistance(x) - getEndDistance(y);
            if(difference < 0)
                return -1;
            else if(difference > 0)
                return 1;

            return 0;
        }
    }

    private final Comparator<Tile> comparator;
    private PriorityQueue<Tile> openList;
    private Map<Tile, PathFinderState> tileStateMap;
    private Map<Tile, Float> distanceToStartMap;
    private Map<Tile, Float> distanceToEndMap;
    private Map<Tile, Tile> parentTileMap;
    private Level level;

    public PathFinder()
    {
        comparator = new TileComparator();
    }

    public void init(final Level level)
    {
        final int numberOfTiles = level.getNumberOfTiles();
        openList = new PriorityQueue<Tile>(numberOfTiles, comparator);
        tileStateMap = new HashMap<Tile, PathFinderState>(numberOfTiles);
        distanceToStartMap = new HashMap<Tile, Float>(numberOfTiles);
        distanceToEndMap = new HashMap<Tile, Float>(numberOfTiles);
        parentTileMap = new HashMap<Tile, Tile>(numberOfTiles);
        this.level = level;
    }

    /**
     * Find a path from start (x,y) and end (x,y) tiles.
     * 
     * Returns the next tile the Actor should travel to to make it to the
     * endPosition.
     * 
     * @param level
     * @param startPositionX
     * @param startPositionY
     * @param endPositionX
     * @param endPositionY
     * @return
     */
    public Tile findPath(final int startPositionX, final int startPositionY, final int endPositionX, final int endPositionY)
    {
        resetData();
        final Tile startTile = level.getTile(startPositionX, startPositionY);
        final Tile endTile = level.getTile(endPositionX, endPositionY);

        if(startTile == null || endTile == null)
            return null;

        tileStateMap.put(startTile, PathFinderState.OPEN);
        startTile.setPathFinderState(PathFinderState.OPEN);
        distanceToStartMap.put(startTile, 0f);
        distanceToEndMap.put(startTile, 0f);
        openList.add(startTile);

        while(!openList.isEmpty())
        {
            final Tile currentTile = openList.poll();
            tileStateMap.put(currentTile, PathFinderState.CLOSED);
            currentTile.setPathFinderState(PathFinderState.CLOSED);

            // Path found!
            if(currentTile == endTile)
            {
                return getNextTile(startTile, endTile);
            }

            final int tileX = currentTile.getTilePositionX();
            final int tileY = currentTile.getTilePositionY();

            // For each adjacent tile, calculate the start and end distance
            // values for A*
            for(int i = tileX - 1; i <= tileX + 1; i++)
            {
                for(int j = tileY - 1; j <= tileY + 1; j++)
                {
                    if(i == tileX && j == tileY)
                        continue;

                    final Tile adjacentTile = level.getTile(i, j);

                    if(adjacentTile == null)
                        continue;
                    else if(!adjacentTile.isTraversable())
                        continue;
                    else if(tileStateMap.get(adjacentTile) == PathFinderState.CLOSED)
                        continue;
                    else if(i != tileX && j != tileY)
                    {
                        // If checking a diagonal, make sure it is possible to
                        // get there
                        Tile diagonalTile = level.getTile(i, tileY);
                        if(diagonalTile == null || !diagonalTile.isTraversable())
                            continue;

                        diagonalTile = level.getTile(tileX, j);
                        if(diagonalTile == null || !diagonalTile.isTraversable())
                            continue;
                    }

                    // If adjacent tile is not closed or open, it has not been
                    // traveled to yet. Mark it as traveled.
                    if(tileStateMap.get(adjacentTile) == null)
                    {
                        placeTileInOpenList(currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                    /*
                     * If adjacent tile has a greater distance to start than
                     * traveling from the current tile, that means a shorter
                     * path to the adjacent tile has been found.
                     */
                    else if(getTotalStartDistance(currentTile, adjacentTile) < getStartDistance(adjacentTile))
                    {
                        /*
                         * Remove the old reference to adjacent tile because the
                         * F value is being recalculated and should be
                         * reordered.
                         */
                        if(tileStateMap.get(adjacentTile) == PathFinderState.OPEN)
                            openList.remove(adjacentTile);

                        placeTileInOpenList(currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                }
            }
        }

        return null;
    }

    private void resetData()
    {
        openList.clear();
        tileStateMap.clear();
        distanceToStartMap.clear();
        distanceToEndMap.clear();
        parentTileMap.clear();
    }

    private void placeTileInOpenList(final Tile currentTile, final Tile adjacentTile, final int endPositionX, final int endPositionY)
    {
        setDistanceValues(currentTile, adjacentTile, endPositionX, endPositionY);
        parentTileMap.put(adjacentTile, currentTile);

        tileStateMap.put(adjacentTile, PathFinderState.OPEN);
        adjacentTile.setPathFinderState(PathFinderState.OPEN);
        openList.add(adjacentTile);
    }

    private Tile getNextTile(final Tile startTile, final Tile endTile)
    {
        Tile adjacentTile = endTile;
        Tile parentTile;

        do
        {
            parentTile = parentTileMap.get(adjacentTile);
            parentTile.setPathFinderState(PathFinderState.NONE);
            if(parentTile == startTile)
                return adjacentTile;

            adjacentTile = parentTile;
        }while(parentTile != null);

        return null;
    }

    private float getStartDistance(final Tile tile)
    {
        final Float distance = distanceToStartMap.get(tile);
        return (distance == null) ? Float.MAX_VALUE : distance;
    }

    private float getEndDistance(final Tile tile)
    {
        final Float distance = distanceToEndMap.get(tile);
        return (distance == null) ? Float.MAX_VALUE : distance;
    }

    private float getFDistance(final Tile tile)
    {
        final Float startValue = getStartDistance(tile);
        if(startValue == Float.MAX_VALUE)
            return Float.MAX_VALUE;

        final Float endValue = getEndDistance(tile);
        if(endValue == Float.MAX_VALUE)
            return Float.MAX_VALUE;

        return startValue + endValue;
    }

    private float getTotalStartDistance(final Tile currentTile, final Tile adjacentTile)
    {
        return MathUtils.getDistance(currentTile.getTilePositionX(), currentTile.getTilePositionY(), adjacentTile.getTilePositionX(),
                adjacentTile.getTilePositionY()) + getStartDistance(currentTile);
    }

    private void setDistanceValues(final Tile currentTile, final Tile adjacentTile, final int endPositionX, final int endPositionY)
    {
        final float value = getTotalStartDistance(currentTile, adjacentTile);

        distanceToStartMap.put(adjacentTile, value);

        if(getEndDistance(adjacentTile) == Float.MAX_VALUE)
        {
            final float endValue = MathUtils.getDistance(adjacentTile.getTilePositionX(), adjacentTile.getTilePositionY(), endPositionX,
                    endPositionY);
            distanceToEndMap.put(adjacentTile, endValue);
        }
    }
}