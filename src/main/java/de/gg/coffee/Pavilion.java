package de.gg.coffee;

import de.gg.coffee.actor.IProgrammer;
import de.gg.coffee.actor.proxied.IStat;
import de.gg.coffee.service.CoffeeService;
import de.gg.coffee.service.ContextRegistry;
import de.gg.coffee.util.AppProperties;
import de.gg.coffee.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by olegdelone on 13.02.2016.
 */
public class Pavilion {
    private static Logger log = Logger.getLogger(Pavilion.class);

    public static void main(String[] args) throws InterruptedException {

        ContextRegistry.getCashService().start();
        for (CoffeeService coffeeService : ContextRegistry.getCoffeeSevices()) {
            coffeeService.start();
        }

        ExecutorService service = Executors.newFixedThreadPool(Integer.valueOf(AppProperties.get("programmers.topick")));
        for (final IProgrammer IProgrammer : ContextRegistry.getProgrammers()) {
            service.submit((Runnable) () -> {
                IProgrammer.gatherInfo();
                try {
                    ContextRegistry.getCashService().getProgrammersToPay().put(IProgrammer);
                } catch (InterruptedException e) {
                    log.warn("InterruptedException occurred", e);
                }
            });
        }

        log.debug("awaiting latch...");
        ContextRegistry.LATCH.await();
        log.debug("latch awaken...");

        service.shutdown();

        log.debug("interrupting threads...");
        ContextRegistry.getCashService().interrupt();
        for (CoffeeService coffeeService : ContextRegistry.getCoffeeSevices()) {
            coffeeService.interrupt();
        }

        log.info("retrieving statistics...");

        for (IStat iStat : ContextRegistry.getStats()) {
            iStat.printStat();
        }

        log.debug("main thread finished...");

    }
}
