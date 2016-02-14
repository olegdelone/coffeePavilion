package de.gg.coffee.actor.proxied;

import de.gg.coffee.actor.CoffeeMachine;
import de.gg.coffee.actor.ICoffeeMachine;
import de.gg.coffee.util.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by olegdelone on 14.02.2016.
 */
public class CoffeeMachineSttstcs implements ICoffeeMachine, IStat {
    private static Logger log = Logger.getLogger(CoffeeMachineSttstcs.class);

    private final CoffeeMachine coffeeMachine;
    private final Map<CoffeeMachine.CoffeeType, Integer> type2cnt = new HashMap<>(CoffeeMachine.CoffeeType.values().length);


    public CoffeeMachineSttstcs(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    @Override
    public Set<CoffeeMachine.CoffeeType> getSupportedTypes() {
        return coffeeMachine.getSupportedTypes();
    }

    @Override
    public boolean doesSupportSuchType(CoffeeMachine.CoffeeType coffeeType) {
        return coffeeMachine.doesSupportSuchType(coffeeType);
    }

    @Override
    public CoffeeMachine.Cup getCup() {
        return coffeeMachine.getCup();
    }

    @Override
    public CoffeeMachine.Cup takeCup() {
        return coffeeMachine.takeCup();
    }

    @Override
    public void putCup(CoffeeMachine.Cup cup) {
        coffeeMachine.putCup(cup);
    }

    @Override
    public void setCurrentCoffeeType(CoffeeMachine.CoffeeType coffeeType) {
        coffeeMachine.setCurrentCoffeeType(coffeeType);
    }

    @Override
    public void fillCup() {
        coffeeMachine.fillCup();
        CoffeeMachine.CoffeeType type = coffeeMachine.getCurrentCoffeeType();
        Integer cnt = type2cnt.get(type);
        if(cnt == null){
            cnt = 0;
        }
        type2cnt.put(type, ++cnt);
    }

@Override
    public void printStat() {
        log.info("---stats for {} ---", toString());
        for (Map.Entry<CoffeeMachine.CoffeeType, Integer> entry : type2cnt.entrySet()) {
            log.info("{} => {}", entry.getKey(), entry.getValue());
        }
        log.info("----------");
    }

    @Override
    public String toString() {
        return coffeeMachine.toString();
    }

    @Override
    public Map<?, ?> getStatMap() {
        return type2cnt;
    }
}
