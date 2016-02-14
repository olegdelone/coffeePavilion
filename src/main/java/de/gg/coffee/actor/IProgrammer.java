package de.gg.coffee.actor;

import de.gg.coffee.service.CoffeeService;

/**
 * Created by olegdelone on 14.02.2016.
 */
public interface IProgrammer {
    void gatherInfo();

    void payCoffee(ICashRegister cashRegister);

    CoffeeService findMachine();

    void startGettingCoffee(ICoffeeMachine coffeeMachine);
}
