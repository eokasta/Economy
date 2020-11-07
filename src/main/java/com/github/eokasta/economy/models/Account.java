package com.github.eokasta.economy.models;

import com.github.eokasta.economy.manager.EconomyManager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {

    private int id;
    private final String name;
    private double coins;
    private boolean modified;

    public void addCoins(double coins) {
        setCoins(this.coins += coins);
    }

    public void removeCoins(double coins) {
        setCoins(Math.max(0, this.coins - coins));
    }

    public boolean hasCoins(double coins) {
        return this.coins >= coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
        modified = true;
        EconomyManager.getInstance().getAccountCache().save(this);
    }
}
