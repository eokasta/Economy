package com.github.eokasta.economy.dao;

import com.github.eokasta.economy.dao.provider.Dao;
import com.github.eokasta.economy.entities.Account;
import com.github.eokasta.economy.storage.provider.DatabaseProvider;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountDao implements Dao<String, Account> {

    private final DatabaseProvider databaseProvider;

    @Override
    public Optional<Account> get(int id) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + databaseProvider.getTable() + " WHERE id = '" + id + "'");
                final ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return Optional.of(Account.builder()
                        .name(resultSet.getString("name"))
                        .id(resultSet.getInt("id"))
                        .coins(resultSet.getDouble("coins"))
                        .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> get(String name) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + databaseProvider.getTable() + " WHERE name = '" + name + "'");
                final ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return Optional.of(Account.builder()
                        .name(resultSet.getString("name"))
                        .id(resultSet.getInt("id"))
                        .coins(resultSet.getDouble("coins"))
                        .build()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean has(String name) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + databaseProvider.getTable() + " WHERE name = '" + name + "'");
                final ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> getAll() {
        final List<Account> accounts = new ArrayList<>();

        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + databaseProvider.getTable() + ";");
                final ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                accounts.add(Account.builder()
                        .name(resultSet.getString("name"))
                        .id(resultSet.getInt("id"))
                        .coins(resultSet.getDouble("coins"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public Account[] getAllOrder(int limit) {
        final Account[] accounts = new Account[limit];

        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM " + databaseProvider.getTable() + " WHERE coins > 0 ORDER BY coins DESC LIMIT " + limit);
                final ResultSet resultSet = statement.executeQuery()
        ) {
            int i = 0;
            while (resultSet.next()) {
                final Account account = Account.builder()
                        .name(resultSet.getString("name"))
                        .id(resultSet.getInt("id"))
                        .coins(resultSet.getDouble("coins"))
                        .build();
                accounts[i++] = account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    @Override
    public void save(Account account) {
        if (!has(account.getName())) {
            insert(account);
        } else {
            update(account);
        }
    }

    @Override
    public void delete(Account account) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("DELETE FROM " + databaseProvider.getTable() + " WHERE name = ?")
        ) {
            statement.setString(1, account.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(Account account) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("INSERT INTO " + databaseProvider.getTable() +
                        " (name, coins) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, account.getName());
            statement.setDouble(2, account.getCoins());

            final int affectedRows = statement.executeUpdate();
            if (affectedRows == 0)
                throw new SQLException("Creating user failed, no rows affected.");

            try (final ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    account.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(Account account) {
        try (
                final Connection connection = databaseProvider.getConnection();
                final PreparedStatement statement = connection.prepareStatement("UPDATE " + databaseProvider.getTable() + " SET coins = ? WHERE name = ?")
        ) {
            statement.setDouble(1, account.getCoins());
            statement.setString(2, account.getName());

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
