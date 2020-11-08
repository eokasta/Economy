package com.github.eokasta.economy.cache;

import com.github.eokasta.economy.cache.provider.Cache;
import com.github.eokasta.economy.models.Account;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCache implements Cache<String, Account> {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> get(String name) {
        return Optional.ofNullable(accounts.get(name));
    }

    @Override
    public boolean has(String name) {
        return accounts.containsKey(name);
    }

    @Override
    public Collection<String> getKeys() {
        return accounts.keySet();
    }

    @Override
    public Collection<Account> getValues() {
        return accounts.values();
    }

    @Override
    public void put(String name, Account account) {
        accounts.put(name, account);
    }

    @Override
    public void remove(String name) {
        accounts.remove(name);
    }

}
