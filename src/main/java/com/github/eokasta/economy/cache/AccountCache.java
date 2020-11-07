package com.github.eokasta.economy.cache;

import com.github.eokasta.economy.dao.provider.Dao;
import com.github.eokasta.economy.models.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AccountCache implements Dao<String, Account> {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> get(int id) {
        return accounts.values().stream().filter(account -> account.getId() == id).findFirst();
    }

    @Override
    public Optional<Account> get(String name) {
        return Optional.ofNullable(accounts.get(name));
    }

    @Override
    public boolean has(String name) {
        return accounts.containsKey(name);
    }

    @Override
    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void save(Account account) {
        accounts.put(account.getName(), account);
    }

    @Override
    public void delete(Account account) {
        accounts.remove(account.getName());
    }
}
