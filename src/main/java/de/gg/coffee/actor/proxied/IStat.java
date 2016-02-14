package de.gg.coffee.actor.proxied;

import de.gg.coffee.actor.CashRegister;
import de.gg.coffee.util.Logger;

import java.util.Map;

/**
 * Created by olegdelone on 14.02.2016.
 */
public interface IStat {
    Logger log = Logger.getLogger(IStat.class);

    default void printStat(){
        log.info("---{} stats---", getClass().getSimpleName());
        for (Map.Entry entry : getStatMap().entrySet()) {
            log.info("{} => {}", entry.getKey(), entry.getValue());
        }
        log.info("----------");
    };
    Map<?,?> getStatMap();
}
