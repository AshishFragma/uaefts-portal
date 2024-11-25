package com.uaefts.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("com.uaefts.repository")
@ComponentScan(basePackages={"com.uaefts","com.uaefts.controller","com.uaefts.service","com.uaefts.repository"})
public class UaeftsPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaeftsPortalApplication.class, args);
	}

}
