package com.brainstation23.skeleton.common.utils;

import java.security.MessageDigest;

public class ChecksumUtil {

    public static String createChecksum(String data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(data.getBytes());

            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static boolean verifyChecksum(String data, String checksum) {
        try {
            String calculatedChecksum = createChecksum(data);
            return checksum.equals(calculatedChecksum);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
