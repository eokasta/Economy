package com.github.eokasta.economy.storage.provider;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseProvider {

    HikariDataSource getDataSource();
    Connection getConnection() throws SQLException;
    String getTable();

}
