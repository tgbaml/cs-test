To run the code:
================
- com.instrumentation.process.IntrumentationGenerator is the entry point of the application
- the program takes an argument with is the full path and name of the log file
- the sample log file that can be used to run the above program is under the sample subfolder - file name is: sample_log_file.log

Details on how the code works:
==============================
- the HSQLDB gets created under the embedded_db subfolder. The name of the DB is instrument_cs
- logger currently throws logs to console
- The IntrumentationGenerator instantiates a CacheIngester, CacheManager, CacheListener and the HsqldbWriter
- The CacheIngester reads the logs file and updates 2 maps (one for start logs and one for finished logs) using the CacheManager
- As the CacheIngester does so, it checks if the start and finished melogs for an event are both updated to the cache, if it is then the 
registered CacheListener(s) are notified about the event. The CacheListener(s) are registered in the IntrumentationGenerator at the start
and currently it is just one CacheListener
- The CacheListener in turn spawns up a new Thread called WriterThread which in turn uses HsqldbWriter to calculate the duration of the event
and write it to the HSQLDB. The Threads are managed using ExecutorService
- The application is stopped once it is ensured the ingestion to the cache is complete as well as there are not active threads in the ThreadPoolExecutor

Outstanding items:
==================
Few things as TODOs, going forward:
1. I have used Maven instead of Gradle. Since the requirement is Gradle, so worth looking into that subsequently
2. I have mostly added couple of key tests but would have liked to add more
3. Worth logging the logs into a log file instead of the console.
4. Currently writes to the HSQLDB would be done 1 record at at time as opposed to batches of inserts together. I am not 100% how bad that would
perform given that they are happening in concurrent threads. So ideally some test on large files would have revealed if this is sufficient or
executing in batches would have been better. Also would have liked to use a connection pool for the DB connections.
