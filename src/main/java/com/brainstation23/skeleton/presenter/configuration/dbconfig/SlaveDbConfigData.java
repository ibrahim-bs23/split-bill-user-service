package com.brainstation23.skeleton.presenter.configuration.dbconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource.slave")
@Getter
@Setter
public class SlaveDbConfigData {
    private String url;
    private String username;
    private String password;
    private int maximumPoolSize;
    private int minimumIdle;
    private int connectionTimeout;
    private int idleTimeout;
    private int maxLifetime;
    private String poolName;
    private int validationTimeout;
    private int leakDetectionThreshold;
}
