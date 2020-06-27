package notu.cs.springboot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import notu.cs.springboot.entity.Playlist;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {
	List<Playlist> findByOwner(String owner);
}
