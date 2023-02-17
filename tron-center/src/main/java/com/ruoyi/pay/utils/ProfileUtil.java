package com.ruoyi.pay.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ProfileUtil {
    private static AtomicInteger counter = new AtomicInteger(0);

    /**
     * 长生消息id
     */

    public static long getAtomicCounter() {
        if (counter.get() > 999999) {
            counter.set(1);

        }

        long time = System.currentTimeMillis();

        long returnValue = time * 100 + counter.incrementAndGet();

        return returnValue;

    }

    private static long incrementAndGet() {
        return counter.incrementAndGet();

    }

    public static void main(String[] args) {
        for (int i=0;i<100;i++){
            System.out.println(ProfileUtil.getAtomicCounter());
        }
    }

}
