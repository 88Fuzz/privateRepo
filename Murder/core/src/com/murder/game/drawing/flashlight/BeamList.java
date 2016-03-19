package com.murder.game.drawing.flashlight;

/**
 * Special double-linked list of {@link Beam}. Traditional java LinkedList does
 * not work here since one cannot start an iteration over the list starting at
 * any point.
 */
public class BeamList
{
    private Beam head;
    private Beam tail;

    public BeamList(final int length)
    {
        head = new Beam();
        tail = head;

        Beam lastBeam = head;
        for(int i = 0; i < length - 1; i++)
        {
            Beam nextBeam = new Beam();
            nextBeam.setPrevBeam(lastBeam);
            lastBeam.setNextBeam(nextBeam);
            lastBeam = nextBeam;
        }
        tail = lastBeam;
    }

    /**
     * Returns the first item in the list.
     * 
     * @return
     */
    public Beam getHead()
    {
        return head;
    }

    /**
     * Removes the first item in the list and returns it.
     * 
     * @return
     */
    public Beam pollHead()
    {
        final Beam retVal = head;
        head = head.getNextBeam();
        head.setPrevBeam(null);

        retVal.setNextBeam(null);
        retVal.setPrevBeam(null);
        return retVal;
    }

    /**
     * Adds beam to the head of the list.
     * 
     * @param beam
     */
    public void addHead(final Beam beam)
    {
        head.setPrevBeam(beam);
        beam.setNextBeam(head);
        beam.setPrevBeam(null);
        head = beam;
    }

    /**
     * Returns the last item in the list.
     * 
     * @return
     */
    public Beam getTail()
    {
        return tail;
    }

    /**
     * Removes the last item in the list and returns it.
     * 
     * @return
     */
    public Beam pollTail()
    {
        final Beam retVal = tail;
        tail = tail.getPrevBeam();
        tail.setNextBeam(null);
        
        retVal.setNextBeam(null);
        retVal.setPrevBeam(null);
        return retVal;
    }

    /**
     * Add beam to the tail of the list.
     * 
     * @param beam
     */
    public void addTail(final Beam beam)
    {
        tail.setNextBeam(beam);
        beam.setPrevBeam(tail);
        beam.setNextBeam(null);
        tail = beam;

    }
}