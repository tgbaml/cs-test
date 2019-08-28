package com.instrumentation.writer;

import com.instrumentation.cache.ingester.fileingester.CacheIngesterFromFileImpl;
import com.instrumentation.cache.listener.CacheListener;
import com.instrumentation.cache.listener.CacheListenerImpl;
import com.instrumentation.valueobjects.WriteObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class WriterThreadTest{

    @Test
    public void processDataTest() throws Exception {
        CacheIngesterFromFileImpl cacheIngesterFromFile = new CacheIngesterFromFileImpl("./sample/sample_log_file.log");
        CacheListener cacheListener = new CacheListenerImpl();
        cacheIngesterFromFile.registerListener(cacheListener);
        cacheIngesterFromFile.updateCache();

        WriterThread writerThread = new WriterThread("scsmbstgrc");
        WriteObject writeObject = writerThread.processData();

        assertEquals(true, writeObject.getAlert());
        assertEquals("scsmbstgrc", writeObject.getEvent());
        assertEquals("NA", writeObject.getType());
        assertEquals(8, writeObject.getEventDuration());


    }

}
