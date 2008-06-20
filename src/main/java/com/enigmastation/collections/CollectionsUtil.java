/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.enigmastation.collections;

/**
 * @author jottinger
 */
public class CollectionsUtil {
    private static boolean[] isLetterArray = new boolean[256];
    private static boolean limitArray;

    static {
        String limitArrayStr = System.getProperty("classifier.limitarray");
        if ("true".equalsIgnoreCase(limitArrayStr)) {
            limitArray = true;
        }
        for (byte b = 'a'; b <= 'z'; b++) {
            isLetterArray[b] = true;
        }
    }

    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ConstantConditions"})
    public static boolean validKey(String k) {
        if (limitArray) {
            byte[] bytes = k.getBytes();
            boolean okay = true;
            int bi = 0;
            while (bi < bytes.length && okay) {
                okay = okay && isLetterArray[bytes[bi]];
                bi++;
            }
            return okay;
        }
        return true;
    }

    public static String join(String[] wordIds) {
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (String word : wordIds) {
            sb.append(separator);
            sb.append(word);
            separator = ":";
        }
        return sb.toString();
    }
}
