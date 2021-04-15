package pl.gromada.webFluxReactiveAPI.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.gromada.webFluxReactiveAPI.model.Gender;
import pl.gromada.webFluxReactiveAPI.model.User;
import pl.gromada.webFluxReactiveAPI.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@ResponseStatus(HttpStatus.OK)
	public Flux<User> findAllUsers() {
		return userRepository.findAll();
	}

	public Mono<ResponseEntity<User>> findUserById(String id) {
		return userRepository.findById(id).map(user -> ResponseEntity.ok(user))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	public Mono<ResponseEntity<User>> saveUser(User user) {
		return userRepository.save(user).map(saveUser -> new ResponseEntity<User>(saveUser, HttpStatus.CREATED))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	public Mono<ResponseEntity<Void>> deleteUserById(String id) {
		return userRepository.findById(id)
				.flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	public Mono<ResponseEntity<Void>> updateUser(String id, User user) {
		return userRepository.findById(id).flatMap(monoUser -> {
			monoUser.setFirstName(user.getFirstName());
			monoUser.setLastName(user.getLastName());
			monoUser.setGender(user.getGender());
			return userRepository.save(monoUser).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)));
		}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

	@PostConstruct
	public void initData() {
		userRepository.deleteAll().subscribe();
		saveUser(new User("Tomasz", "Kowal", Gender.MAN)).subscribe();
		saveUser(new User("Patryk", "Wrzos", Gender.MAN)).subscribe();
		saveUser(new User("Joanna", "Ptak", Gender.WOMAN)).subscribe();
	}

}
