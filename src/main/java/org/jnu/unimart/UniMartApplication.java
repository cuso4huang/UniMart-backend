package org.jnu.unimart;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Key;
import java.util.Base64;

@SpringBootApplication
public class UniMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniMartApplication.class, args);
	}

}
