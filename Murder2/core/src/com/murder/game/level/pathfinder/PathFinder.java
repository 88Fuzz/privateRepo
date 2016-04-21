package com.murder.game.level.pathfinder;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.utils.MathUtils;

public class PathFinder
{
    private static class TileComparator implements Comparator<Tile>
    {
        public String pathKey = null;

        @Override
        public int compare(final Tile x, final Tile y)
        {
            float difference = x.getFValue(pathKey) - y.getFValue(pathKey);
            if(difference < 0)
                return -1;
            else if(difference > 0)
                return 1;

            /*
             * If the F value is the same, fall back and the one that is closest
             * to the end to be picked next in the list.
             */
            difference = x.getDistanceToEnd(pathKey) - y.getDistanceToEnd(pathKey);
            if(difference < 0)
                return -1;
            else if(difference > 0)
                return 1;

            return 0;
        }
    }

    private static final Comparator<Tile> comparator = new TileComparator();
    private PriorityQueue<Tile> openList;
    private Level level;

    public PathFinder()
    {}

    public void init(final Level level)
    {
        openList = new PriorityQueue<Tile>(level.getNumberOfTiles(), comparator);
        this.level = level;
    }

    /**
     * Find a path from start (x,y) and end (x,y) tiles.
     * 
     * Returns true if a path is available from start tile to end tile.
     * 
     * This algorithm assumes space is more available than time. This may be
     * bad. Or good. Who knows. TODO figure out if this is bad.
     * 
     * @param level
     * @param pathKey
     * @param startPositionX
     * @param startPositionY
     * @param endPositionX
     * @param endPositionY
     * @return
     */
    public boolean findPath(final String pathKey, final int startPositionX, final int startPositionY, final int endPositionX, final int endPositionY)
    {
        System.out.println("\nFINDING PATH " + pathKey);
        openList.clear();
        ((TileComparator) comparator).pathKey = pathKey;
        final Tile startTile = level.getTile(startPositionX, startPositionY);
        final Tile endTile = level.getTile(endPositionX, endPositionY);
        if(startTile == null || endTile == null)
            return false;

        if(startTile.getChildTile(pathKey) != null)
        {
            // TODO remove the generatePath, this is only used to draw the path
            generatePath(pathKey, endTile);
            return true;
        }

        startTile.setPathFinderState(pathKey, PathFinderState.OPEN);
        startTile.setDistanceToStart(pathKey, 0);
        startTile.setDistanceToEnd(pathKey, 0);
        openList.add(startTile);
        while(!openList.isEmpty())
        {
            final Tile currentTile = openList.poll();
            System.out.println("\nPulled");
            printTile(currentTile);
            System.out.println("");
            currentTile.setPathFinderState(pathKey, PathFinderState.CLOSED);

            // Path found!
            if(currentTile == endTile)
            {
                generatePath(pathKey, endTile);
                return true;
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
                    if(adjacentTile == null || !adjacentTile.isTraversable() || adjacentTile.getPathFinderState(pathKey) == PathFinderState.CLOSED)
                        continue;

                    // If adjacent tile is not closed or open, it has not been
                    // traveled to yet. Mark it as traveled.
                    if(adjacentTile.getPathFinderState(pathKey) == PathFinderState.NONE)
                    {
                        placeTileInOpenList(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                    /*
                     * If adjacent tile has a greater distance to start than
                     * traveling from the current tile, that means a shorter
                     * path to the adjacent tile has been found.
                     */
                    else if(getStartDistanceValue(pathKey, currentTile, adjacentTile) < adjacentTile.getDistanceToStart(pathKey))
                    {
                        // TODO figure out if changing the adjacentTile F value
                        // outside of the add method causes the queue to be out
                        // of order.
                        /*
                         * Remove the old reference to adjacent tile because the
                         * F value is being recalculated and should be
                         * reordered.
                         */
                        if(adjacentTile.getPathFinderState(pathKey) == PathFinderState.OPEN)
                            openList.remove(adjacentTile);

                        placeTileInOpenList(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                }
            }
            System.out.println("OpenList");
            printOpenList();
        }

        return false;

    }

    private void placeTileInOpenList(final String pathKey, final Tile currentTile, final Tile adjacentTile, final int endPositionX,
            final int endPositionY)
    {
        setDistanceValues(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
        adjacentTile.setParentTile(pathKey, currentTile);
        adjacentTile.setPathFinderState(pathKey, PathFinderState.OPEN);
        openList.add(adjacentTile);

        System.out.print("    ");
        printTile(adjacentTile);
        System.out.print(" to start " + adjacentTile.getDistanceToStart(pathKey) + " to end " + adjacentTile.getDistanceToEnd(pathKey) + " F value "
                + adjacentTile.getFValue(pathKey));
        System.out.println("");
    }

    private float getStartDistanceValue(final String pathKey, final Tile currentTile, final Tile adjacentTile)
    {
        return MathUtils.getDistance(currentTile.getTilePositionX(), currentTile.getTilePositionY(), adjacentTile.getTilePositionX(),
                adjacentTile.getTilePositionY()) + currentTile.getDistanceToStart(pathKey);
    }

    private void setDistanceValues(final String pathKey, final Tile currentTile, final Tile adjacentTile, final int endPositionX,
            final int endPositionY)
    {
        final float value = getStartDistanceValue(pathKey, currentTile, adjacentTile);
        adjacentTile.setDistanceToStart(pathKey, value);
        if(adjacentTile.getDistanceToEnd(pathKey) == Float.MAX_VALUE)
        {
            final float endValue = MathUtils.getDistance(adjacentTile.getTilePositionX(), adjacentTile.getTilePositionY(), endPositionX,
                    endPositionY);
            adjacentTile.setDistanceToEnd(pathKey, endValue);
        }
    }

    /**
     * Connects the path from the start tile to the end tile.
     * 
     * @param pathKey
     * @param endTile
     */
    private void generatePath(final String pathKey, final Tile endTile)
    {
        Tile childTile = endTile;
        Tile parentTile = endTile.getParentTile(pathKey);
        childTile.setColor();
        while(parentTile != null)
        {
            parentTile.setChildTile(pathKey, childTile);
            parentTile.setColor();

            childTile = parentTile;
            parentTile = childTile.getParentTile(pathKey);
        }
    }

    private void printOpenList()
    {
        for(final Tile tile: openList)
        {
            System.out.print("  ");
            printTile(tile);
            System.out.println("");
        }
    }

    private void printTile(final Tile tile)
    {
        System.out.print("[" + tile.getTilePositionX() + ", " + tile.getTilePositionY() + "]");
    }
}