package com.zl.async;

import com.zl.model.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zl on 2016/7/19.
 */
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwnerId;
    private Map<String,String> exts=new HashMap<String,String>();
    public EventModel(EventType type){
        this.type=type;
    }
    public EventModel(){

    }
    public String getExt(String key){
        return  exts.get(key);
    }
    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    public EventType getType(){
        return  type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return  this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return  this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return  this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
