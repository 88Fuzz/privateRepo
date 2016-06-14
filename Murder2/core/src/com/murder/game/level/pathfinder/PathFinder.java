package com.murder.game.level.pathfinder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.utils.MathUtils;

public class PathFinder
{
    private class TileComparator implements Comparator<Tile>
    {
        public String pathKey = null;

        @Override
        public int compare(final Tile x, final Tile y)
        {
            float difference = getFDistance(x) - getFDistance(y);
            // float difference = x.getFValue(pathKey) - y.getFValue(pathKey);
            if(difference < 0)
                return -1;
            else if(difference > 0)
                return 1;

            /*
             * If the F value is the same, fall back and the one that is closest
             * to the end to be picked next in the list.
             */
            difference = getEndDistance(x) - getEndDistance(y);
            // difference = x.getDistanceToEnd(pathKey) -
            // y.getDistanceToEnd(pathKey);
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
    // TODO remove pathKey
    public Tile findPath(final String pathKey, final int startPositionX, final int startPositionY, final int endPositionX, final int endPositionY)
    {
        // System.out.println("\nFINDING PATH " + pathKey);
        resetData();
        ((TileComparator) comparator).pathKey = pathKey;
        final Tile startTile = level.getTile(startPositionX, startPositionY);
        final Tile endTile = level.getTile(endPositionX, endPositionY);
        // TODO touchedTiles will not be needed after pathKey is removed.
        // final Set<Tile> touchedTiles = new HashSet<Tile>();

        if(startTile == null || endTile == null)
            return null;

        // if(startTile.getChildTile(pathKey) != null)
        // {
        // // TODO remove the generatePath, this is only used to draw the path
        // generatePath(pathKey, endTile);
        // return true;
        // }

        tileStateMap.put(startTile, PathFinderState.OPEN);
        startTile.setPathFinderState(PathFinderState.OPEN);
        // startTile.setPathFinderState(pathKey, PathFinderState.OPEN);
        distanceToStartMap.put(startTile, 0f);
        // startTile.setDistanceToStart(pathKey, 0);
        distanceToEndMap.put(startTile, 0f);
        // startTile.setDistanceToEnd(pathKey, 0);
        openList.add(startTile);
        while(!openList.isEmpty())
        {
            final Tile currentTile = openList.poll();
            // touchedTiles.add(currentTile);
            // System.out.println("\nPulled");
            // printTile(currentTile);
            // System.out.println("");
            tileStateMap.put(currentTile, PathFinderState.CLOSED);
            currentTile.setPathFinderState(PathFinderState.CLOSED);
            // currentTile.setPathFinderState(pathKey, PathFinderState.CLOSED);

            // Path found!
            if(currentTile == endTile)
            {
                generatePath(pathKey, endTile);
                return getNextTile(startTile, endTile);
            }

//            System.out.println(currentTile.getTilePositionX() + "," + currentTile.getTilePositionY());
//            System.out.println("\t" + getStartDistance(currentTile));
//            System.out.println("\t" + getEndDistance(currentTile));
//            System.out.println("\t" + getFDistance(currentTile));

            final int tileX = currentTile.getTilePositionX();
            final int tileY = currentTile.getTilePositionY();

            // For each adjacent tile, calculate the start and end distance
            // values for A*
            for(int i = tileX - 1; i <= tileX + 1; i++)
            {
                // System.out.println("I " + i);
                for(int j = tileY - 1; j <= tileY + 1; j++)
                {
                    // System.out.println("J " + j);
                    if(i == tileX && j == tileY)
                        continue;

                    final Tile adjacentTile = level.getTile(i, j);

                    // if(adjacentTile != null)
                    // touchedTiles.add(currentTile);

                    if(adjacentTile == null)
                    {
                        // System.out.println(" " + i + " " + j + " null");
                        continue;
                    }
                    else if(!adjacentTile.isTraversable())
                    {
                        // System.out.println(" " + i + " " + j + " not
                        // traversable");
                        continue;
                    }
                    else if(tileStateMap.get(adjacentTile) == PathFinderState.CLOSED)
                    // else if(adjacentTile.getPathFinderState(pathKey) ==
                    // PathFinderState.CLOSED)
                    {
                        // System.out.println(" " + i + " " + j + " closed");
                        continue;
                    }
                    else if(i != tileX && j != tileY)
                    {
                        // If checking a diagonal, make sure it is possible to
                        // get there
                        Tile diagonalTile = level.getTile(i, tileY);
                        if(diagonalTile == null || !diagonalTile.isTraversable())
                        {
                            // System.out.println(" " + i + " " + j + "
                            // diagonal");
                            continue;
                        }

                        diagonalTile = level.getTile(tileX, j);
                        if(diagonalTile == null || !diagonalTile.isTraversable())
                        {
                            // System.out.println(" " + i + " " + j + "
                            // diagonal");
                            continue;
                        }
                    }

                    // If adjacent tile is not closed or open, it has not been
                    // traveled to yet. Mark it as traveled.
                    if(tileStateMap.get(adjacentTile) == null)
                    // if(adjacentTile.getPathFinderState(pathKey) ==
                    // PathFinderState.NONE)
                    {
                        placeTileInOpenList(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                    /*
                     * If adjacent tile has a greater distance to start than
                     * traveling from the current tile, that means a shorter
                     * path to the adjacent tile has been found.
                     */
                    else if(getTotalStartDistance(pathKey, currentTile, adjacentTile) < getStartDistance(adjacentTile))
                    // else if(getStartDistanceValue(pathKey, currentTile,
                    // adjacentTile) < adjacentTile.getDistanceToStart(pathKey))
                    {
                        // TODO figure out if changing the adjacentTile F value
                        // outside of the add method causes the queue to be out
                        // of order.
                        /*
                         * Remove the old reference to adjacent tile because the
                         * F value is being recalculated and should be
                         * reordered.
                         */
                        if(tileStateMap.get(adjacentTile) == PathFinderState.OPEN)
                            // if(adjacentTile.getPathFinderState(pathKey) ==
                            // PathFinderState.OPEN)
                            openList.remove(adjacentTile);

                        placeTileInOpenList(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
                    }
                }
            }
            // System.out.println("OpenList");
            // printOpenList();
        }

        // for(final Tile tile: touchedTiles)
        // tile.clearPathInformation(pathKey);

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

    private void placeTileInOpenList(final String pathKey, final Tile currentTile, final Tile adjacentTile, final int endPositionX,
            final int endPositionY)
    {
        setDistanceValues(pathKey, currentTile, adjacentTile, endPositionX, endPositionY);
        // adjacentTile.setParentTile(pathKey, currentTile);
        parentTileMap.put(adjacentTile, currentTile);

        tileStateMap.put(adjacentTile, PathFinderState.OPEN);
        adjacentTile.setPathFinderState(PathFinderState.OPEN);
        // adjacentTile.setPathFinderState(pathKey, PathFinderState.OPEN);
        openList.add(adjacentTile);

        // System.out.print(" ");
        // printTile(adjacentTile);
        // System.out.print(" to start " +
        // adjacentTile.getDistanceToStart(pathKey) + " to end " +
        // adjacentTile.getDistanceToEnd(pathKey) + " F value "
        // + adjacentTile.getFValue(pathKey));
        // System.out.println("");
    }

    private Tile getNextTile(final Tile startTile, final Tile endTile)
    {
        Tile adjacentTile = endTile;
        Tile parentTile;// = parentTileMap.get(endTile);

//        System.out.println("FINISHED!");
        do
        {
            parentTile = parentTileMap.get(adjacentTile);
            parentTile.setPathFinderState(PathFinderState.NONE);
//            System.out.println("parent " + parentTile + " [" + parentTile.getTilePositionX() + "," + parentTile.getTilePositionY() + "]");
            if(parentTile == startTile)
                return adjacentTile;

            adjacentTile = parentTile;
        }while(parentTile != null);
        // while(parentTile != null)
        // {
        // if(parentTile == startTile)
        // return adjacentTile;
        //
        // adjacentTile = parentTile;
        // parentTile = parentTileMap.get(parentTile);
        // }

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

    private float getTotalStartDistance(final String pathKey, final Tile currentTile, final Tile adjacentTile)
    {
        return MathUtils.getDistance(currentTile.getTilePositionX(), currentTile.getTilePositionY(), adjacentTile.getTilePositionX(),
                adjacentTile.getTilePositionY()) + getStartDistance(currentTile);
    }

    private void setDistanceValues(final String pathKey, final Tile currentTile, final Tile adjacentTile, final int endPositionX,
            final int endPositionY)
    {
        final float value = getTotalStartDistance(pathKey, currentTile, adjacentTile);

        distanceToStartMap.put(adjacentTile, value);
        // adjacentTile.setDistanceToStart(pathKey, value);

        if(getEndDistance(adjacentTile) == Float.MAX_VALUE)
        // if(adjacentTile.getDistanceToEnd(pathKey) == Float.MAX_VALUE)
        {
            final float endValue = MathUtils.getDistance(adjacentTile.getTilePositionX(), adjacentTile.getTilePositionY(), endPositionX,
                    endPositionY);
            distanceToEndMap.put(adjacentTile, endValue);
            // adjacentTile.setDistanceToEnd(pathKey, endValue);
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
        // Tile childTile = endTile;
        // Tile parentTile = endTile.getParentTile(pathKey);
        // childTile.setColor();
        // while(parentTile != null)
        // {
        // parentTile.setChildTile(pathKey, childTile);
        // parentTile.setColor();
        //
        // childTile = parentTile;
        // parentTile = childTile.getParentTile(pathKey);
        // }
    }

    // private void printOpenList()
    // {
    // for(final Tile tile: openList)
    // {
    // System.out.print(" ");
    // printTile(tile);
    // System.out.println("");
    // }
    // }

    private void printTile(final Tile tile)
    {
        // System.out.print("[" + tile.getTilePositionX() + ", " +
        // tile.getTilePositionY() + "]");
    }
}