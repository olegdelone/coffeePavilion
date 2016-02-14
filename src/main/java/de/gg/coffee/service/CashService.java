package de.gg.coffee.service;

import de.gg.coffee.actor.ICashRegister;
import de.gg.coffee.actor.IProgrammer;
import de.gg.coffee.util.AppProperties;
import de.gg.coffee.util.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by olegdelone on 13.02.2016.
 */
public class CashService extends Thread {
    private static Logger log = Logger.getLogger(CashService.class);
    private final ICashRegister cashRegister;
    private final static int TO_PAY_SIZE = Integer.valueOf(AppProperties.get("programmers.topay"));
    private final BlockingQueue<IProgrammer> programmersToPay = new LinkedBlockingQueue<>(TO_PAY_SIZE);

    public CashService(ICashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    public BlockingQueue<IProgrammer> getProgrammersToPay() {
        return programmersToPay;
    }

    @Override
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(TO_PAY_SIZE);
        try {
            log.info("CashService is about to start...");
            while (ContextRegistry.LATCH.getCount() > 0) {
                IProgrammer programmer = programmersToPay.take(); // can avoid this approach at all
                service.execute(() -> {
                    programmer.payCoffee(cashRegister);
                    CoffeeService coffeeService = programmer.findMachine();
                    try {
                        coffeeService.getProgrammersToGet().put(programmer);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (InterruptedException e) {
            log.info("Interrupting thread...");
        } finally {
            service.shutdown();
        }
    }
}
