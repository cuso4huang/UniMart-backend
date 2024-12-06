package org.jnu.unimart;

import org.jnu.unimart.service.DatabaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniMartApplication.class, args);
	}

}
