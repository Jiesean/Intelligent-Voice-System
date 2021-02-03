package com.jiesean.opuscodec.opus;

/**
 * @author Wangjie
 * @description:
 * @date :2021/2/3 2:45 PM
 */
public class Opus {
    private static Opus instance = null ;
    private Opus(){
    }

    public static Opus getInstance(){
        if(instance == null){
            instance = new Opus();
        }
        return instance;
    }
}
