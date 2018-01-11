package com.myhadoop;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.Test;

public class EhcacheTest {

    @Test
    public void test1() {
        Cache testCache = CacheManager.getInstance().getCache("testCache");
        Element ljm = new Element("ljm", "123321");
        testCache.put(ljm);
        Element ljm1 = testCache.get("ljm");
        System.out.println(ljm1.getObjectValue());
    }
}
