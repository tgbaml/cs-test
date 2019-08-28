package com.instrumentation.cache.listener;

public interface CacheListener {

    public void eventComplete(String event);

    public boolean isEmpty();

    public void cleanUp();
}
