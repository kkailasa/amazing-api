package com.amazing.api;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * This is the Main Entry point for the <b>OrganizationData Application</b>
 * 
 * @author kkailasa
 *
 */
@SpringBootApplication
@EnableJpaAuditing
public class OrganizationalDataApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OrganizationalDataApplication.class);
	    app.setBannerMode(Mode.OFF);
	    app.run(args);
	}

}
