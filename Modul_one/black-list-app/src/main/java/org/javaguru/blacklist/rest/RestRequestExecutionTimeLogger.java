package org.javaguru.blacklist.rest;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RestRequestExecutionTimeLogger {

    private static final Logger logger = LoggerFactory.getLogger(RestRequestExecutionTimeLogger.class);

    public void logExecutionTime(Stopwatch stopwatch) {
        stopwatch.stop();
        long endMillis = stopwatch.elapsed().toMillis();
        logger.info("Request processing time (ms): " + endMillis);
    }
}
