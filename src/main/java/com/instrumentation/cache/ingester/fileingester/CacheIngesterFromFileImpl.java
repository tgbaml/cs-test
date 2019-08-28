package com.instrumentation.cache.ingester.fileingester;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instrumentation.cache.CacheManager;
import com.instrumentation.cache.ingester.CacheIngester;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import com.instrumentation.cache.listener.CacheListener;
import com.instrumentation.valueobjects.LogData;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheIngesterFromFileImpl implements CacheIngester {
    ObjectMapper objectMapper = new ObjectMapper();
    CacheManager cachManager = CacheManager.getInstance();
    String filePath = null;
    List<CacheListener> cacheListeners = new ArrayList<>();
    private AtomicBoolean ingestionComplete = new AtomicBoolean(false);
    public CacheIngesterFromFileImpl(String sourceurl) {
        this.filePath = sourceurl;
    }

    public void registerListener(CacheListener cacheListener) {
        this.cacheListeners.add(cacheListener);
    }

    public void updateCache() throws Exception {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(this :: updateCache);
        }
        ingestionComplete.set(true);
    }

    public boolean isIngestionComplete() {
        return ingestionComplete.get();
    }

    private void updateCache(String s) {
        LogData logData = readToObject(s);
        cachManager.updateCache(logData);

        if (cachManager.isComplete(logData.getId())) {
            cacheListeners.forEach(c -> c.eventComplete(logData.getId()));
        }

    }

    private LogData readToObject(String s) throws RuntimeException{
        try {
            return objectMapper.readValue(s, LogData.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
