package com.cecgw.cq.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author denghualin
 * @version V1.0
 * @since 2018-11-20
 */
@Configuration
public class DBConfig {

//    @Bean
//    public JdbcTemplate creatJdbc(@Autowired DataSource dataSource){
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        jdbcTemplate.setDataSource(dataSource);
//        return jdbcTemplate;
//    }

    @Bean
    public JdbcTemplate dropJdbc(@Autowired DataSource dataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate ();
        jdbcTemplate.setDataSource (dataSource);
        return jdbcTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.datasource.other")
    public DataSource createDataSource(){
        return DataSourceBuilder.create().build();
    }

}
