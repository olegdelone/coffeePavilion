package de.gg.coffee.service;

import de.gg.coffee.actor.CoffeeMachine;
import de.gg.coffee.actor.ICoffeeMachine;
import de.gg.coffee.actor.IProgrammer;
import de.gg.coffee.util.AppProperties;
import de.gg.coffee.util.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by olegdelone on 12.02.2016.
 */

public class CoffeeService extends Thread {
    private static Logger log = Logger.getLogger(CoffeeService.class);

    private final ICoffeeMachine coffeeMachine;
    private final BlockingQueue<IProgrammer> programmersToGet = new LinkedBlockingQueue<>(Integer.valueOf(AppProperties.get("programmers.toget")));

    public CoffeeService(ICoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public BlockingQueue<IProgrammer> getProgrammersToGet() {
        return programmersToGet;
    }

    public ICoffeeMachine getCoffeeMachine() {
        return coffeeMachine;
    }

    @Override
    public void run() {
        log.info("CoffeeService is about to start");
        while (ContextRegistry.LATCH.getCount() > 0) {
            try {
                IProgrammer IProgrammer = programmersToGet.take();
                log.info("programmer is taken: {} for machine {}", IProgrammer, coffeeMachine);
                IProgrammer.startGettingCoffee(coffeeMachine);
            } catch (InterruptedException e) {
                log.info("Interrupting thread...");
                return;
            } finally {
                ContextRegistry.LATCH.countDown();
            }
        }
    }
}
