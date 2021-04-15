package pl.gromada.webFluxReactiveAPI.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import pl.gromada.webFluxReactiveAPI.model.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>{
	
}
