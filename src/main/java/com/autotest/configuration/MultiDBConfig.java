package com.autotest.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class MultiDBConfig {
	
	@Bean(name = "oracleDb")
	@ConfigurationProperties(prefix = "spring.datasource.db-oracle")
	public DataSource oracleDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "oracleJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("oracleDb") DataSource dsOracle) {
		return new JdbcTemplate(dsOracle);
	}
	
	@Bean(name = "mssqlDb")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.db-mssql")
	public DataSource mssqlDataSource() {
		return  DataSourceBuilder.create().build();
	}

	@Bean(name = "mssqlJdbcTemplate")
	@Autowired
	public JdbcTemplate mssqlJdbcTemplate(@Qualifier("mssqlDb") 
                                              DataSource dsMsSql) {
		return new JdbcTemplate(dsMsSql);
	}
	
	@Bean(name = "mssqlEdwsqaDb")
	@ConfigurationProperties(prefix = "spring.datasource.db-edw")
	public DataSource mssqlEdwsqaDataSource() {
		return  DataSourceBuilder.create().build();
	}

	@Bean(name = "mssqlEdwsqaJdbcTemplate")
	@Autowired
	public JdbcTemplate mssqlEdwsqaJdbcTemplate(@Qualifier("mssqlEdwsqaDb") 
                                              DataSource dsMsSqlEdwsqa) {
		return new JdbcTemplate(dsMsSqlEdwsqa);
	}

}
