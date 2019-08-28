package com.instrumentation.cache.ingester;

import com.instrumentation.cache.listener.CacheListener;

public interface CacheIngester {
    public void updateCache()  throws Exception;

    public void registerListener(CacheListener cacheListener);

    public boolean isIngestionComplete();
}
