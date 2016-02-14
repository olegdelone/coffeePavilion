package de.gg.coffee.actor;

import java.util.Set;

/**
 * Created by olegdelone on 14.02.2016.
 */
public interface ICoffeeMachine {
    Set<CoffeeMachine.CoffeeType> getSupportedTypes();

    boolean doesSupportSuchType(CoffeeMachine.CoffeeType coffeeType);

    CoffeeMachine.Cup getCup();

    CoffeeMachine.Cup takeCup();

    void putCup(CoffeeMachine.Cup cup);

    void setCurrentCoffeeType(CoffeeMachine.CoffeeType coffeeType);

    void fillCup();
}
