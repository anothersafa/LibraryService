package com.mylibrary.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceManager {
	
	@Bean("lms")
	@ConfigurationProperties(prefix = "app.lms")
	DataSource elmasDs() {
		return DataSourceBuilder.create().build();
	}

}
