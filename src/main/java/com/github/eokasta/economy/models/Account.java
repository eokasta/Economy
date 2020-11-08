package com.github.eokasta.economy.models;

import com.github.eokasta.economy.manager.EconomyManager;
import com.github.eokasta.economy.singleton.SingletonMapper;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@Builder
public class Account {

    private final EconomyManager economyManager = SingletonMapper.of(EconomyManager.class);

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

    @SneakyThrows
    public void setCoins(double coins) {
        this.coins = coins;
        modified = true;
        economyManager.getAccountCache().put(name, this);
    }
}
