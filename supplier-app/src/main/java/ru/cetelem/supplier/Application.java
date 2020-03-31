package ru.cetelem.supplier;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan({"ru.cetelem.supplier.service", "ru.cetelem.supplier.integration"})
@EntityScan("ru.cetelem.cassiope.supplier.model")
@EnableJpaRepositories("ru.cetelem.supplier.repository")
@ImportResource("classpath:camel-context.xml")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
