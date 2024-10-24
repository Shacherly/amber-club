package com.loser.backend.club.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration("loserDataSourceConfig")
@MapperScan(
        basePackages = "com.trading.backend.club.losermapper",
        sqlSessionFactoryRef = "loserSqlSessionFactory"
)
@Profile({"prod"})
public class SlaverDataSource {


    @Bean("loserDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slaver")
    public DataSource loserDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("loserSqlSessionFactory")
    public SqlSessionFactory loserSqlSessionFactory(@Qualifier("loserDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:losermapper/*.xml"));
        sqlSessionFactory.setTypeHandlersPackage("com.trading.backend.club.common.type");
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "loserTransactionManager")
    public DataSourceTransactionManager loserTransactionManager(@Qualifier("loserDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "loserSqlSessionTemplate")
    public SqlSessionTemplate loserSqlSessionTemplate(@Qualifier("loserSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
