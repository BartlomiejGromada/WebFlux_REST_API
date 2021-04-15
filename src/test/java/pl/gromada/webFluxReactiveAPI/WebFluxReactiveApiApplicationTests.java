package pl.gromada.webFluxReactiveAPI;

import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static pl.gromada.webFluxReactiveAPI.model.Gender.MAN;
import static pl.gromada.webFluxReactiveAPI.model.Gender.WOMAN;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import pl.gromada.webFluxReactiveAPI.controller.UserController;
import pl.gromada.webFluxReactiveAPI.model.User;
import pl.gromada.webFluxReactiveAPI.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@WebFluxTest(controllers = UserController.class)
@ExtendWith(SpringExtension.class)
class WebFluxReactiveApiApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private UserService userService;
	
	private final String ENDPOINT = "http://localhost:8080/api/v1/users/";
	private final String ID = "sdsds23";
	private final User USER = new User();


	@Test
	void findAllUser_Should_Return_200OK() {
		Flux<User> users = Flux.just(
				new User("Bartek", "Nowak", MAN), 
				new User("Kasia", "Kowal", WOMAN),
				new User("Jan", "Bednarz", MAN));
		
		Mockito.when(userService.findAllUsers()).thenReturn(users);
		
		webTestClient.get()
					 .uri(ENDPOINT)
					 .accept(APPLICATION_STREAM_JSON)
					 .exchange()
					 .expectStatus().isOk()
					 .expectBody(User.class);
		
		Mockito.verify(userService, times(1)).findAllUsers();
	}
	
	@Test
	void findUserById_Should_Return_200OK() {
		Mockito.when(userService.findUserById(ID)).thenReturn(Mono.just(ResponseEntity.ok(USER)));
		
		webTestClient.get()
					 .uri(ENDPOINT+"{id}", ID)
					 .accept(APPLICATION_JSON)	 				
					 .exchange()
					 .expectStatus().isOk()
					 .expectBody(User.class);		
		
		Mockito.verify(userService, times(1)).findUserById(ID);
	}
	
	@Test 
	void findUserById_Should_Return_404NOTFOUND() {
		Mockito.when(userService.findUserById(ID)).thenReturn(Mono.just(ResponseEntity.notFound().build()));
		
		webTestClient.get()
					 .uri(ENDPOINT+"{id}", ID)
					 .exchange()
					 .expectStatus().isNotFound();
	
		Mockito.verify(userService, times(1)).findUserById(ID);
	}
	
	@Test
	void saveUser_Should_Return_201CREATED() {	
		Mockito.when(userService.saveUser(USER)).thenReturn(Mono.just(new ResponseEntity<User>(USER, HttpStatus.CREATED)));
		
		webTestClient.post()
					 .uri(ENDPOINT)
					 .contentType(APPLICATION_JSON)
					 .body(BodyInserters.fromPublisher(Mono.just(USER), User.class))
					 .exchange()
					 .expectStatus().isCreated();
				
		Mockito.verify(userService, times(1)).saveUser(USER);
	}
	
	@Test
	void updateUser_Should_Return_200OK() {
		Mockito.when(userService.updateUser(ID, USER)).thenReturn(Mono.just(ResponseEntity.ok().build()));
		
		webTestClient.put()
					 .uri(ENDPOINT+"{id}", ID)
					 .contentType(APPLICATION_JSON)
					 .body(BodyInserters.fromPublisher(Mono.just(USER), User.class))
					 .exchange()
					 .expectStatus().isOk();
		
		Mockito.verify(userService, times(1)).updateUser(ID, USER);
	}

}
