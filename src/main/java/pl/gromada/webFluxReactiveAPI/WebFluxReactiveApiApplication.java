package pl.gromada.webFluxReactiveAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:mongoDB.properties")
public class WebFluxReactiveApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxReactiveApiApplication.class, args);
	}
}
