package kopo.poly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@SpringBootApplication
public class SpringBootOracleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOracleApplication.class, args);
    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://192.168.115.130:3306/prDB");
        dataSource.setUsername("pr_user");
        dataSource.setPassword("1234");
        return dataSource;
    }

}

