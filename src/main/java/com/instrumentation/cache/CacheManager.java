package com.instrumentation.cache;

import com.instrumentation.valueobjects.LogData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
    private static CacheManager cacheManager = null;
    private ConcurrentHashMap<String, LogData> startCache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LogData> endCache = new ConcurrentHashMap<>();

    private CacheManager() {}

    public static synchronized CacheManager getInstance() {
        if (cacheManager == null) {
            cacheManager = new CacheManager();
        }
        return cacheManager;
    }

    public void updateCache(LogData toCache) {
        if (toCache.getState().equals(CacheType.FINISHED)) {
            endCache.put(toCache.getId(), toCache);
        } else {
            startCache.put(toCache.getId(), toCache);
        }
    }

    public LogData getCachedValue(CacheType cacheType, String key) {
        if (cacheType.equals(CacheType.FINISHED)) {
            return endCache.get(key);
        } else {
            return startCache.get(key);
        }
    }

    public LogData getStartData(String event) {
        return startCache.get(event);
    }

    public LogData getEndData(String event) {
        return endCache.get(event);
    }

    public boolean isComplete(String event) {
        return startCache.containsKey(event) && endCache.containsKey(event);
    }

}
