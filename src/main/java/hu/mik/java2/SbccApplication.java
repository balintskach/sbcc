package hu.mik.java2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ServletComponentScan
@ComponentScan
@SpringBootApplication
public class SbccApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbccApplication.class, args);
	}
}
