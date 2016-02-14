package de.gg.coffee.service;

import de.gg.coffee.actor.CashRegister;
import de.gg.coffee.actor.CoffeeMachine;
import de.gg.coffee.actor.IProgrammer;
import de.gg.coffee.actor.Programmer;
import de.gg.coffee.actor.proxied.CashRegisterSttstcs;
import de.gg.coffee.actor.proxied.CoffeeMachineSttstcs;
import de.gg.coffee.actor.proxied.IStat;
import de.gg.coffee.actor.proxied.ProgrammerSttstcs;
import de.gg.coffee.util.AppProperties;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by olegdelone on 12.02.2016.
 */
public class ContextRegistry {
    public final static int PROGRAMMERS_SIZE = Integer.valueOf(AppProperties.get("programmers.size"));
    private final static CashService CASH_SERVICE;
    private final static List<IStat> I_STATS;

    public static CountDownLatch LATCH = new CountDownLatch(ContextRegistry.PROGRAMMERS_SIZE);

    private final static List<IProgrammer> PROGRAMMERS;
    private final static List<CoffeeService> COFFEE_SERVICES;

    static {
        I_STATS = new ArrayList<>();

        final Set<CoffeeMachine.CoffeeType> coffeeTypes1 = new HashSet<CoffeeMachine.CoffeeType>() {{
            add(CoffeeMachine.CoffeeType.CAPPUCCINO);
            add(CoffeeMachine.CoffeeType.LATTE_MACCHIATO);
        }};
        final Set<CoffeeMachine.CoffeeType> coffeeTypes2 = new HashSet<CoffeeMachine.CoffeeType>() {{
            add(CoffeeMachine.CoffeeType.CAPPUCCINO);
            add(CoffeeMachine.CoffeeType.LATTE_MACCHIATO);
            add(CoffeeMachine.CoffeeType.ESPRESSO);
        }};
        final Set<CoffeeMachine.CoffeeType> coffeeTypes3 = new HashSet<CoffeeMachine.CoffeeType>() {{
            add(CoffeeMachine.CoffeeType.ESPRESSO);
        }};

        CashRegisterSttstcs cashRegisterSttstcs = new CashRegisterSttstcs(CashRegister.getInstance());
        CASH_SERVICE  = new CashService(cashRegisterSttstcs);
        I_STATS.add(cashRegisterSttstcs);

        COFFEE_SERVICES = new ArrayList<CoffeeService>() {{
            CoffeeMachineSttstcs coffeeMachineSttstcs = new CoffeeMachineSttstcs(new CoffeeMachine(coffeeTypes1));
            add(new CoffeeService(coffeeMachineSttstcs));
            I_STATS.add(coffeeMachineSttstcs);

            coffeeMachineSttstcs = new CoffeeMachineSttstcs(new CoffeeMachine(coffeeTypes1));
            add(new CoffeeService(coffeeMachineSttstcs));
            I_STATS.add(coffeeMachineSttstcs);

            coffeeMachineSttstcs = new CoffeeMachineSttstcs(new CoffeeMachine(coffeeTypes2));
            add(new CoffeeService(coffeeMachineSttstcs));
            I_STATS.add(coffeeMachineSttstcs);

            coffeeMachineSttstcs = new CoffeeMachineSttstcs(new CoffeeMachine(coffeeTypes3));
            add(new CoffeeService(coffeeMachineSttstcs));
            I_STATS.add(coffeeMachineSttstcs);
        }};


        PROGRAMMERS = new ArrayList<>(PROGRAMMERS_SIZE);
        ProgrammerSttstcs programmerSttstcs = null;
        for (int i = 0; i < PROGRAMMERS_SIZE; i++) {
            programmerSttstcs = new ProgrammerSttstcs(new Programmer());
            PROGRAMMERS.add(programmerSttstcs);
        }
        if(programmerSttstcs != null){
            I_STATS.add(programmerSttstcs);
        }
    }

    public static List<CoffeeService> getCoffeeSevices() {
        return COFFEE_SERVICES;
    }

    public static List<IProgrammer> getProgrammers() {
        return PROGRAMMERS;
    }

    public static CashService getCashService() {
        return CASH_SERVICE;
    }

    public static List<IStat> getStats() {
        return I_STATS;
    }
}
