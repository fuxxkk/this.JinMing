package com.test;

import java.util.*;

public class TestClone {
    public static void main(String[] args) {
        int[] array = {1, 2, 3};

        List<int[]> ints = Arrays.asList(array);

        List<Integer> list = new ArrayList<Integer>();
        for (int i =0;i<array.length;i++) {
            list.add(array[i]);
        }
        System.out.println(list);

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
            if (num==2) {
                iterator.remove();
            }
        }
        Integer[] integers = list.toArray(new Integer[]{});
        System.out.println(list);
        System.out.println(Arrays.toString(integers));
    }
}
