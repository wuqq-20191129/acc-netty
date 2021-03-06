package com.wuqq.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Classname CmDataSourceConfig
 * @Description TODO
 * @Date 2020/12/31 10:23
 * @Created by mh
 */
@Configuration
@MapperScan(basePackages = "com.wuqq.dao.cm",sqlSessionFactoryRef = "cmSqlSesssionFactory")
public class CmDataSourceConfig {

    @Value("${cm.datasource.url}")
    private String url;

    @Value("${cm.datasource.username}")
    private  String username;

    @Value("${cm.datasource.password}")
    private  String password;

    @Value("${cm.datasource.driver-class-name}")
    private String driverClass;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMills;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMills;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.preparedStatements}")
    private  boolean preparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionsSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;

    @Bean(name = "cmDataSource")
    @Primary //标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean 优先被考虑。
    public DataSource cmDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);

        //具体配置
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMills);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMills);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(preparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionsSize);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }

    @Bean(name = "cmTransactionManager")
    @Primary
    public DataSourceTransactionManager cmTransactionManager() {
        return new DataSourceTransactionManager(cmDataSource());
    }

    @Bean(name = "cmSqlSesssionFactory")
    @Primary
    public SqlSessionFactory cmSqlSessionFactory(@Qualifier("cmDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/cm/*.xml"));
        //分页插件
      //  Interceptor interceptor = new PageInterceptor();
      //  Properties properties = new Properties();
      //  //数据库
      //  properties.setProperty("helperDialect", "mysql");
      //  //是否将参数offset作为PageNum使用
      //  properties.setProperty("offsetAsPageNum", "true");
      //  //是否进行count查询
      //  properties.setProperty("rowBoundsWithCount", "true");
      //  //是否分页合理化
      //  properties.setProperty("reasonable", "false");
      //  interceptor.setProperties(properties);
      //  sessionFactory.setPlugins(new Interceptor[] {interceptor});

        return sessionFactory.getObject();
    }

}

