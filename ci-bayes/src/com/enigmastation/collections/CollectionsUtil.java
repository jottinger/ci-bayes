/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.enigmastation.collections;

/**
 *
 * @author jottinger
 */
public class CollectionsUtil {
    static boolean[] isLetterArray=new boolean[256];
    static {
        for(byte b='a';b<='z';b++) {
            isLetterArray[b]=true;
        }
    }

    public static boolean validKey(String k) {
        byte[] bytes=k.getBytes();
        boolean okay=true;
        int bi=0;
        while(bi<bytes.length && okay) {            
            okay&=isLetterArray[bytes[bi]];
            bi++;
        }
        return okay;
    }
}
