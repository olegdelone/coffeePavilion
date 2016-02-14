package de.gg.coffee.actor;

import de.gg.coffee.util.ThreadUtil;

/**
 * Created by olegdelone on 12.02.2016.
 */
public class CashRegister implements ICashRegister {

    private static volatile CashRegister instance;

    public static CashRegister getInstance() {
        if(instance == null){
            synchronized (CashRegister.class){
                if(instance == null){
                    instance = new CashRegister();
                }
            }
        }
        return instance;
    }

    private CashRegister() {
    }

    public enum PaymentType{
        cash, card
    }

    @Override
    public void payCash() {
        ThreadUtil.busyFor(500);
    }
    @Override
    public void payCard(){
        ThreadUtil.busyFor(250);
    }
}
