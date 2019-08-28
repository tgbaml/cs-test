package com.instrumentation.cache.listener;

import com.instrumentation.cache.CacheManager;
import com.instrumentation.valueobjects.WriteObject;
import com.instrumentation.writer.WriterThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CacheListenerImpl implements CacheListener{
    int MAX_THREADS = 2;
    ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

    public void eventComplete(String event) {
        WriterThread writerThread = new WriterThread(event);
        executor.submit(writerThread);

    }

    public boolean isEmpty() {
        return ((ThreadPoolExecutor)executor).getActiveCount() == 0;
    }

    public void cleanUp() {
        executor.shutdown();
    }

}
