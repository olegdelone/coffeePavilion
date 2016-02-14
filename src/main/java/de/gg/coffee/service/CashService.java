package de.gg.coffee.service;

import de.gg.coffee.actor.ICashRegister;
import de.gg.coffee.actor.IProgrammer;
import de.gg.coffee.util.AppProperties;
import de.gg.coffee.util.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by olegdelone on 13.02.2016.
 */
public class CashService extends Thread {
    private static Logger log = Logger.getLogger(CashService.class);
    private final ICashRegister cashRegister;

    private final BlockingQueue<IProgrammer> programmersToPay = new LinkedBlockingQueue<>(Integer.valueOf(AppProperties.get("programmers.topay")));

    public CashService(ICashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    public BlockingQueue<IProgrammer> getProgrammersToPay() {
        return programmersToPay;
    }

    @Override
    public void run() {
        try {
            log.info("CashService is about to start...");
            while (ContextRegistry.LATCH.getCount() > 0) {
                IProgrammer programmer = programmersToPay.take();

                programmer.payCoffee(cashRegister);
                CoffeeService coffeeService = programmer.findMachine();
                coffeeService.getProgrammersToGet().put(programmer);
            }
        } catch (InterruptedException e) {
            log.info("Interrupting thread...");
        }
    }
}
