package pl.gromada.webFluxReactiveAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.gromada.webFluxReactiveAPI.model.User;
import pl.gromada.webFluxReactiveAPI.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@SuppressWarnings("deprecation")
	@GetMapping(path = "/", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<User> findAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
		return userService.findUserById(id);
	}

	@PostMapping("/")
	public Mono<ResponseEntity<User>> saveUser(@RequestBody User user) {
		return userService.saveUser(user);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String id) {
		return userService.deleteUserById(id);
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Void>> updateUser(@PathVariable String id, @RequestBody User user) {
		return userService.updateUser(id, user);
	}
	
}
