package de.gg.coffee.util;

/**
 * Created by olegdelone on 12.02.2016.
 */
public class ThreadUtil {
    private static Logger log = Logger.getLogger(ThreadUtil.class);

    public static void busyFor(long ms){
        try {
//            log.debug("sleeping for: {}", ms);
            Thread.sleep(ms);
//            log.debug("awaking...");
        } catch (InterruptedException e) {
            log.warn("InterruptedException occurred, ignoring it...", e);
            // oops , somebody wants to stop this thread, ignore him
        }
    }
}
