package de.gg.coffee.actor;

import de.gg.coffee.service.CoffeeService;
import de.gg.coffee.service.ContextRegistry;
import de.gg.coffee.util.Logger;
import de.gg.coffee.util.ThreadUtil;

import java.util.*;

/**
 * Created by olegdelone on 12.02.2016.
 */
public class Programmer implements IProgrammer {
    private static Logger log = Logger.getLogger(Programmer.class);

//    todo: flyweight
//    private static Map<CoffeeMachine, List<CoffeeMachine.CoffeeType>>

    static final Random RANDOM = new Random();

    private CoffeeMachine.CoffeeType currentlyPreferredType;

    public CoffeeMachine.CoffeeType getCurrentlyPreferredType() {
        return currentlyPreferredType;
    }

    @Override
    public void gatherInfo() {
        log.info("gathering info...");
        int len = CoffeeMachine.CoffeeType.values().length;
        Set<CoffeeMachine.CoffeeType> availableCoffeeTypes = new HashSet<>(len);
        for (CoffeeService coffeeService : ContextRegistry.getCoffeeSevices()) {
            ICoffeeMachine ICoffeeMachine = coffeeService.getCoffeeMachine();
            availableCoffeeTypes.addAll(ICoffeeMachine.getSupportedTypes());
            if(len == availableCoffeeTypes.size()) {
                break;
            }
        }
        CoffeeMachine.CoffeeType[] typesArray = new CoffeeMachine.CoffeeType[availableCoffeeTypes.size()]; // todo: ?? the same for all....
        availableCoffeeTypes.toArray(typesArray);

        if(typesArray.length == 0) {
            throw new IllegalStateException("there is no choice from availableCoffeeTypes");
        }
        currentlyPreferredType = typesArray[RANDOM.nextInt(typesArray.length)];

        ThreadUtil.busyFor(500);
        log.info("{} opt for {} coffee",this, currentlyPreferredType);
    }

    @Override
    public void payCoffee(ICashRegister cashRegister) {
        if(currentlyPreferredType == null){
            throw new IllegalArgumentException("Make a choice first");
        }
        CashRegister.PaymentType type = CashRegister.PaymentType.values()[RANDOM.nextInt(CashRegister.PaymentType.values().length)];

        if (type == CashRegister.PaymentType.card) {
            cashRegister.payCard();
        } else {
            cashRegister.payCash();
        }
    }

    @Override
    public CoffeeService findMachine() {
        List<CoffeeService> supports = new ArrayList<>();
        for (CoffeeService coffeeService : ContextRegistry.getCoffeeSevices()) {
            ICoffeeMachine ICoffeeMachine = coffeeService.getCoffeeMachine();
            if (ICoffeeMachine.doesSupportSuchType(currentlyPreferredType)) {
                supports.add(coffeeService);
            }
        }
        if(supports.isEmpty()) {
            throw new IllegalStateException("Chosen type which is not supported by any machine: " + currentlyPreferredType);
        }
        return supports.get(RANDOM.nextInt(supports.size())); // evenly
    }

    @Override
    public void startGettingCoffee(ICoffeeMachine coffeeMachine){

        log.info("{} start getting coffee from {}", this, coffeeMachine);
        ThreadUtil.busyFor(250);
        CoffeeMachine.Cup cup = coffeeMachine.getCup();

        ThreadUtil.busyFor(250);
        coffeeMachine.putCup(cup);

        ThreadUtil.busyFor(250);
        coffeeMachine.setCurrentCoffeeType(currentlyPreferredType);

        coffeeMachine.fillCup();

        ThreadUtil.busyFor(250);
        coffeeMachine.takeCup();

        leave();
        log.info("{} has left from {}", this, coffeeMachine);
    }

    private void leave(){
        currentlyPreferredType = null;
    }

    private int id;
    private static int cnt;
    {
        id = ++cnt;
    }
    @Override
    public String toString() {
        return "Programmer{"+id+"}";
    }
}
