package com.instrumentation.writer;

import com.instrumentation.cache.CacheManager;
import com.instrumentation.valueobjects.LogData;
import com.instrumentation.valueobjects.WriteObject;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class WriterThread implements Callable<Boolean> {
    private String event = null;
    private static final Logger LOGGER = Logger.getLogger(WriterThread.class.getName());
    public WriterThread(String event) {
        this.event = event;
    }
    

    public WriteObject processData() {
        LogData startData = CacheManager.getInstance().getStartData(this.event);
        LogData endData = CacheManager.getInstance().getEndData(this.event);
        long duration = endData.getTimestamp().getTime() - startData.getTimestamp().getTime();
        WriteObject writeObject = new WriteObject();
        writeObject.setEvent(event);
        writeObject.setEventDuration(duration);
        writeObject.setType(endData.getType());
        writeObject.setAlert((duration > 4));
        return writeObject;
    }

    @Override
    public Boolean call() throws Exception {
        WriteObject writeObject = processData();
        LOGGER.info(String.format("Inserting to DB %s",writeObject));
        return HsqldbWriter.getInstance().insertInto(writeObject);
    }
}
