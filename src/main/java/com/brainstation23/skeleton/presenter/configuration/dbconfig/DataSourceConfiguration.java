package com.brainstation23.skeleton.presenter.configuration.dbconfig;

import com.brainstation23.skeleton.presenter.context.DatabaseEnvironment;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final MasterDbConfigData masterDbConfigData;
    private final SlaveDbConfigData slaveDbConfigData;

    @Bean
    public DataSource dataSource(){
        MasterSlaveRoutingDataSource masterSlaveRoutingDataSource = new MasterSlaveRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseEnvironment.UPDATABLE, masterDataSource());
        targetDataSources.put(DatabaseEnvironment.READONLY, slaveDataSource());
        masterSlaveRoutingDataSource.setTargetDataSources(targetDataSources);

        masterSlaveRoutingDataSource.setDefaultTargetDataSource(masterDataSource());
        return masterSlaveRoutingDataSource;
    }

    public DataSource slaveDataSource() {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(slaveDbConfigData.getUrl());
        config.setUsername(slaveDbConfigData.getUsername());
        config.setPassword(slaveDbConfigData.getPassword());
        config.setMaximumPoolSize(slaveDbConfigData.getMaximumPoolSize());
        config.setMinimumIdle(slaveDbConfigData.getMinimumIdle());
        config.setConnectionTimeout(slaveDbConfigData.getConnectionTimeout());
        config.setIdleTimeout(slaveDbConfigData.getIdleTimeout());
        config.setMaxLifetime(slaveDbConfigData.getMaxLifetime());
        config.setPoolName(slaveDbConfigData.getPoolName());
        config.setValidationTimeout(slaveDbConfigData.getValidationTimeout());
        config.setLeakDetectionThreshold(slaveDbConfigData.getLeakDetectionThreshold());

        config.setReadOnly(Boolean.TRUE);

        return new HikariDataSource(config);
    }

    public DataSource masterDataSource() {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(masterDbConfigData.getUrl());
        config.setUsername(masterDbConfigData.getUsername());
        config.setPassword(masterDbConfigData.getPassword());
        config.setMaximumPoolSize(masterDbConfigData.getMaximumPoolSize());
        config.setMinimumIdle(masterDbConfigData.getMinimumIdle());
        config.setConnectionTimeout(masterDbConfigData.getConnectionTimeout());
        config.setIdleTimeout(masterDbConfigData.getIdleTimeout());
        config.setMaxLifetime(masterDbConfigData.getMaxLifetime());
        config.setPoolName(masterDbConfigData.getPoolName());
        config.setValidationTimeout(masterDbConfigData.getValidationTimeout());
        config.setLeakDetectionThreshold(masterDbConfigData.getLeakDetectionThreshold());

        config.setReadOnly(Boolean.FALSE);
        return new HikariDataSource(config);
    }
}
