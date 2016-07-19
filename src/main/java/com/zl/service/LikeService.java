package com.zl.service;

import com.zl.util.JedisAdapter;
import com.zl.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zl on 2016/7/18.
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 如果喜欢返回1，如果不喜欢返回-1，否则返回0
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(userId))?-1:0;

    }
    public long like(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
    public long dislike(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        String dislikeKey=RedisKeyUtil.getDislikeKey(entityId,entityType);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}
