package de.gg.coffee.actor.proxied;

import de.gg.coffee.actor.CashRegister;
import de.gg.coffee.actor.ICashRegister;
import de.gg.coffee.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegdelone on 14.02.2016.
 */
public class CashRegisterSttstcs implements ICashRegister, IStat {
    private static Logger log = Logger.getLogger(CashRegisterSttstcs.class);

    private final Map<CashRegister.PaymentType, Integer> type2cnt = new HashMap<>(CashRegister.PaymentType.values().length);

    private final CashRegister cashRegister;

    public CashRegisterSttstcs(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    @Override
    public void payCash() {
        cashRegister.payCash();
        synchronized (type2cnt) {
            Integer cnt = type2cnt.get(CashRegister.PaymentType.cash);
            if (cnt == null) {
                cnt = 0;
            }
            type2cnt.put(CashRegister.PaymentType.cash, ++cnt);
        }
    }

    @Override
    public void payCard() {
        cashRegister.payCard();
        synchronized (type2cnt) {
            Integer cnt = type2cnt.get(CashRegister.PaymentType.card);
            if (cnt == null) {
                cnt = 0;
            }
            type2cnt.put(CashRegister.PaymentType.card, ++cnt);
        }
    }


    @Override
    public Map<?, ?> getStatMap() {
        return type2cnt;
    }
}
