package com.github.eokasta.economy.storage;

import com.github.eokasta.economy.storage.provider.DatabaseProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class StorageManager implements DatabaseProvider {

    private final HikariDataSource dataSource;

    private final String table;

    public StorageManager(ConfigurationSection sqlSettings) throws SQLException {
        this.table = sqlSettings.getString("table");

        final HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("serverName", sqlSettings.getString("host"));
        config.addDataSourceProperty("port", sqlSettings.getString("port"));
        config.addDataSourceProperty("databaseName", sqlSettings.getString("database"));
        config.addDataSourceProperty("user", sqlSettings.getString("user"));
        config.addDataSourceProperty("password", sqlSettings.getString("password"));
        config.addDataSourceProperty("autoReconnect", true);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);

        config.setMaximumPoolSize(8);
        this.dataSource = new HikariDataSource(config);

        createTable(table,
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(16) NOT NULL UNIQUE, " +
                        "coins DOUBLE NOT NULL, " +
                        "INDEX(name)");
    }

    private void createTable(String table, String values) throws SQLException {
        try (
                final Connection connection = dataSource.getConnection();
                final PreparedStatement statement = connection.prepareStatement(String.format("CREATE TABLE IF NOT EXISTS %s (%s);", table, values))
        ) {
            statement.execute();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
