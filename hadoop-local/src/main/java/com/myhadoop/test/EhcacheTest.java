package com.myhadoop.test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheTest {

    public static void test1() {
        Cache testCache = CacheManager.getInstance().getCache("testCache");
        Element ljm = new Element("ljm", "123321");
        testCache.put(ljm);
        System.out.println("seted......");
    }


    public static void main(String[] args) {
        test1();
    }
}
