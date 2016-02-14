package de.gg.coffee.actor.proxied;

import de.gg.coffee.actor.*;
import de.gg.coffee.service.CoffeeService;
import de.gg.coffee.service.ContextRegistry;
import de.gg.coffee.util.Logger;
import de.gg.coffee.util.Timer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegdelone on 14.02.2016.
 */
public class ProgrammerSttstcs implements IProgrammer, IStat {
    private static Logger log = Logger.getLogger(ProgrammerSttstcs.class);

    @Override
    public Map<?, ?> getStatMap() {
        return type2cnt;
    }

    public enum StatKey {
        avg, min, max
    }
    private static final Map<StatKey, Long> type2cnt = new HashMap<>(StatKey.values().length);


    private final Programmer programmer;

    public ProgrammerSttstcs(Programmer programmer) {
        this.programmer = programmer;
    }

    @Override
    public void gatherInfo() {
        programmer.gatherInfo();
    }

    @Override
    public void payCoffee(ICashRegister cashRegister) {
        programmer.payCoffee(cashRegister);
    }

    @Override
    public CoffeeService findMachine() {
        return programmer.findMachine();
    }

    @Override
    public void startGettingCoffee(ICoffeeMachine coffeeMachine) {
        Timer t = Timer.registerStart();
        programmer.startGettingCoffee(coffeeMachine);
        long ms = t.registerStop();
        Long vol = type2cnt.get(StatKey.avg);
        if(vol == null){
            type2cnt.put(StatKey.avg, ms/ContextRegistry.PROGRAMMERS_SIZE);
        } else {
            type2cnt.put(StatKey.avg, ms/ContextRegistry.PROGRAMMERS_SIZE + vol);
        }

        Long min = type2cnt.get(StatKey.min);
        if(min == null || min > ms){
            type2cnt.put(StatKey.min, ms);
        }

        Long max = type2cnt.get(StatKey.max);
        if(max == null || max < ms){
            type2cnt.put(StatKey.max, ms);
        }

    }

    @Override
    public String toString() {
        return programmer.toString();
    }
}
