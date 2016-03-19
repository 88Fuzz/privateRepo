package com.murder.game.drawing.flashlight;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeamListTest
{
    private static final int TEST_SIZE = 5;

    private BeamList target;

    @Before
    public void setUp()
    {
        target = new BeamList(TEST_SIZE);
        verifySize(TEST_SIZE);
    }

    @Test
    public void testPollHead()
    {
        final Beam prevHead = target.pollHead();

        Assert.assertNotEquals(prevHead, target.getHead());
        Assert.assertNull(prevHead.getNextBeam());
        Assert.assertNull(prevHead.getPrevBeam());
        verifySize(TEST_SIZE - 1);
    }

    @Test
    public void testAddHead()
    {
        final Beam beam = new Beam();
        target.addHead(beam);

        Assert.assertEquals(beam, target.getHead());
        Assert.assertNull(target.getHead().getPrevBeam());
        verifySize(TEST_SIZE + 1);
    }

    @Test
    public void testPollTail()
    {
        final Beam prevTail = target.pollTail();

        Assert.assertNotEquals(prevTail, target.getTail());
        Assert.assertNull(prevTail.getNextBeam());
        Assert.assertNull(prevTail.getPrevBeam());
        verifySize(TEST_SIZE - 1);
    }

    @Test
    public void testAddTail()
    {
        final Beam beam = new Beam();
        target.addTail(beam);

        Assert.assertEquals(beam, target.getTail());
        Assert.assertNull(target.getTail().getNextBeam());
        verifySize(TEST_SIZE + 1);
    }

    private void verifySize(final int size)
    {
        Beam beam = target.getHead();
        int count = 0;

        while(beam != null)
        {
            beam = beam.getNextBeam();
            count++;
        }
        Assert.assertEquals(size, count);
    }
}