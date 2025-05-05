package com.brainstation23.skeleton.common.utils;

import java.util.Random;

public class SessionIdUtil {

    public static String getSessionId() {
        long randomNumber = new Random().nextLong() & 0xffffffffffffL;
        String hexValue = String.format("%016x", randomNumber);
        return hexValue.substring(0, 16);
    }
}
