package ru.cetelem.supplier;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
;

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
	private static final Log log = LogFactory.getLog(Application.class); 

    public static void main(String[] args) {
    	h2Server();
        SpringApplication.run(Application.class, args);
    }


	public static Server h2Server() {
		Server h2Server = null;
        try {
            h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers").start();
            if (h2Server.isRunning(true)) {
                log.info("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
        	log.error(e);	
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
        
        return h2Server;
    }     
   
}
