package com.instrumentation.cache.ingester.fileingester;
import com.instrumentation.cache.CacheManager;
import com.instrumentation.cache.CacheType;
import com.instrumentation.cache.listener.CacheListener;
import com.instrumentation.cache.listener.CacheListenerImpl;
import com.instrumentation.valueobjects.LogData;
import java.sql.Timestamp;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class CacheIngesterFromFileImplTest {

    /*
    * Tests the reading of file and updating of the cache and also the content of the cache using the CacheManager
    */
    @Test
    public void updateCacheTest() throws Exception {
        CacheIngesterFromFileImpl cacheIngesterFromFile = new CacheIngesterFromFileImpl("./sample/sample_log_file.log");
        CacheListener cacheListener = new CacheListenerImpl();
        cacheIngesterFromFile.registerListener(cacheListener);
        cacheIngesterFromFile.updateCache();

        LogData startData = CacheManager.getInstance().getStartData("scsmbstgra");
        LogData endData = CacheManager.getInstance().getEndData("scsmbstgra");

        assertEquals("scsmbstgra", startData.getId());
        assertEquals("APPLICATION_LOG", startData.getType());
        assertEquals("12345", startData.getHost());
        assertEquals(CacheType.STARTED, startData.getState());
        assertEquals(Timestamp.valueOf("2017-04-05 08:31:35.212"), startData.getTimestamp());

        assertEquals("scsmbstgra", endData.getId());
        assertEquals("APPLICATION_LOG", endData.getType());
        assertEquals("12345", endData.getHost());
        assertEquals(CacheType.FINISHED, endData.getState());
        assertEquals(Timestamp.valueOf("2017-04-05 08:31:35.217"), endData.getTimestamp());
    }

//    TODO more tests to add
}
