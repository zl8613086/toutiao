package com.zl.util;

/**
 * Created by zl on 2016/7/18.
 */
public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }
    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
    }
    public static String getDislikeKey(int entityId,int entityType){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityId)+SPLIT+String.valueOf(entityType);
    }
}
