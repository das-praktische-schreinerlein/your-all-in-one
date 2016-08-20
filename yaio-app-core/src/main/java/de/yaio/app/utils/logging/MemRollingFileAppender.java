package de.yaio.app.utils.logging;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;

public class MemRollingFileAppender extends RollingFileAppender {

    @Override
    protected void subAppend(LoggingEvent event) {
        String modifiedMessage = String.format(getMem() + "%s", event.getMessage());
        LoggingEvent modifiedEvent = new LoggingEvent(event.getFQNOfLoggerClass(), event.getLogger(), event.getTimeStamp(), event.getLevel(), modifiedMessage,
                event.getThreadName(), event.getThrowableInformation(), event.getNDC(), event.getLocationInformation(),
                event.getProperties());

        super.subAppend(modifiedEvent);
    }

    public String getMem() {
        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        return "[Mem U:" + usedMB + " T:" + rt.totalMemory() / 1024 / 1024 + " F:" + rt.freeMemory() / 1024 / 1024 + "]";
    }
}