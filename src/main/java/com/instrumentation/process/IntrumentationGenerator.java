package com.instrumentation.process;

import com.instrumentation.cache.CacheManager;
import com.instrumentation.cache.ingester.CacheIngester;
import com.instrumentation.cache.ingester.fileingester.CacheIngesterFromFileImpl;
import com.instrumentation.cache.listener.CacheListener;
import com.instrumentation.cache.listener.CacheListenerImpl;
import com.instrumentation.writer.HsqldbWriter;

import java.util.logging.Level;
import java.util.logging.Logger;


public class IntrumentationGenerator {
    private static final Logger LOGGER = Logger.getLogger(IntrumentationGenerator.class.getName());
    CacheManager cacheManager = CacheManager.getInstance();
    CacheIngester cacheIngester = null;
    HsqldbWriter hsqldbWriter = null;
    CacheListener cacheListener = null;

    public IntrumentationGenerator(CacheIngester cacheIngester) throws Exception{
        this.cacheIngester = cacheIngester;
        this.hsqldbWriter = HsqldbWriter.getInstance();
        this.cacheListener = new CacheListenerImpl();
        this.cacheIngester.registerListener(this.cacheListener);
    }

    public void process() throws Exception {
        this.cacheIngester.updateCache();
    }

    /*
     * Ensures that both the ingestion is complete and consumer is empty
     * before the process can be allowed to clean up and terminate
     *
     */
    public void awaitTermination() {
        do {
            try {
            	
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!this.cacheIngester.isIngestionComplete() && !this.cacheListener.isEmpty());

    }

    public void cleanUp() {
        hsqldbWriter.cleanUp();
        cacheListener.cleanUp();
    }

    public static void main(String... args) throws Exception{
        if (args.length < 1) {
            LOGGER.log(Level.WARNING,"Please provide an argument to the program for the file path and name");
            return;
        }
        IntrumentationGenerator intrumentationGenerator = null;
        try {
            String logfile = args[0];
            intrumentationGenerator = new IntrumentationGenerator(new CacheIngesterFromFileImpl(logfile));
            intrumentationGenerator.process();
 
            intrumentationGenerator.awaitTermination();
            intrumentationGenerator.cleanUp();
        } catch (Exception ex) {
            intrumentationGenerator.cleanUp();
            LOGGER.log(Level.SEVERE,"Process exited with error...");
            ex.printStackTrace();
        }

        LOGGER.info("Process Completed...");
    }
}
