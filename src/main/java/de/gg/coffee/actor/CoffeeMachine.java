package de.gg.coffee.actor;

import de.gg.coffee.util.Logger;
import de.gg.coffee.util.ThreadUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by olegdelone on 12.02.2016.
 */
public class CoffeeMachine implements ICoffeeMachine {
    private static Logger log = Logger.getLogger(CoffeeMachine.class);

    public enum CoffeeType {
        ESPRESSO(250), LATTE_MACCHIATO(500), CAPPUCCINO(750);
        private final long preparingTime;

        CoffeeType(long preparingTime) {
            this.preparingTime = preparingTime;
        }
    }
    private CoffeeType currentCoffeeType;
    private Cup cup;


    private final Set<CoffeeType> supportedTypes;

    public CoffeeType getCurrentCoffeeType() {
        return currentCoffeeType;
    }

    public CoffeeMachine(Set<CoffeeType> supportedTypes) {
        this.supportedTypes = new HashSet<>(supportedTypes);
    }

    @Override
    public Set<CoffeeType> getSupportedTypes() {
        return supportedTypes;
    }

    @Override
    public boolean doesSupportSuchType(CoffeeType coffeeType) {
        if(coffeeType == null){
            throw new IllegalArgumentException("Arg [CoffeeType coffeeType] may not be null");
        }
        return supportedTypes.contains(coffeeType);
    }

    // assuming that there is no limit
    @Override
    public Cup getCup() {
        return new Cup();
    }

    @Override
    public Cup takeCup() {
        Cup c = this.cup;
        if(c.coffeeType == null){
            throw new IllegalStateException("fill the cup first!"); // todo: just warning IRL
        }
        this.cup = null;
        this.currentCoffeeType = null;
        return c;
    }

    @Override
    public void putCup(Cup cup) {
        if(cup == null){
            throw new IllegalArgumentException("Arg [Cup cup] may not be null");
        }
        this.cup = cup;
    }

    @Override
    public void setCurrentCoffeeType(CoffeeType coffeeType){
        if(coffeeType == null){
            throw new IllegalArgumentException("Arg [CoffeeType coffeeType] may not be null");
        }
        currentCoffeeType = coffeeType;
    }

    @Override
    public void fillCup(){
        if(cup == null || currentCoffeeType == null){
            throw new IllegalStateException("you have to choose coffeeType and put a cup first!");
        }
        ThreadUtil.busyFor(currentCoffeeType.preparingTime);
        cup.fill(currentCoffeeType);
    }

    public static class Cup{
        private CoffeeType coffeeType;

        public void fill(CoffeeType coffeeType) {
            this.coffeeType = coffeeType;
        }
    }


    private int id;
    private static int cnt;
    {
        id = ++cnt;
    }
    @Override
    public String toString() {
        return "CoffeeMachine{"+id+"}";
    }
}
