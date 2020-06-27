package notu.cs.springboot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import notu.cs.springboot.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findBy_id(String _id); 
}
