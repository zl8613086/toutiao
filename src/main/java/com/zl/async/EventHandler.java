package com.zl.async;

import java.util.List;

/**
 * Created by zl on 2016/7/19.
 */
public interface EventHandler {
    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
