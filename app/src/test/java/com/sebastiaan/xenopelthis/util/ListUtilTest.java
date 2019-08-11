package com.sebastiaan.xenopelthis.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class ListUtilTest {
    private static ArrayList<Integer> makeList(int size, int bound) {
        if (bound < size)
            throw new IllegalArgumentException("bound must be larger than size");

        Random r = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int randint = r.nextInt(bound);
            if (!list.contains(randint))
                list.add(randint);
        }
        return list;
    }

    @Test
    public void getRemovedTest() {
        ArrayList<Integer> listOld = makeList(100, 150);
        ArrayList<Integer> listNew = makeList(120, 170);
        List<Integer> test = ListUtil.getRemoved(listOld, listNew);
        for (Integer i : test)
            assertTrue(listOld.contains(i) && !listNew.contains(i));
        listOld.removeAll(test);
        assertTrue(listNew.containsAll(listOld));
    }

    @Test
    public void getAddedTest() {
        ArrayList<Integer> listOld = makeList(100, 150);
        ArrayList<Integer> listNew = makeList(120, 170);
        List<Integer> test = ListUtil.getAdded(listOld, listNew);
        for (Integer i : test)
            assertTrue(!listOld.contains(i) && listNew.contains(i));
        listNew.removeAll(test);
        assertTrue(listOld.containsAll(listNew));
    }
}
