package notu.cs.springboot.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import notu.cs.springboot.entity.Playlist;
import notu.cs.springboot.entity.Song;
import notu.cs.springboot.repository.PlaylistRepository;

@Service
public class FunctionService {

	@Autowired
	private PlaylistRepository playlistRepository;
	
	public FunctionService(PlaylistRepository playlistRepository) {
		this.playlistRepository = playlistRepository;
	}
	
	private ObjectMapper mapper;
	
	public FunctionService() {
		mapper = new ObjectMapper();
	}
	
	public String getUserPlaylist(String owner) throws IOException {
		List<Playlist> list = playlistRepository.findByOwner(owner);
		return mapper.writeValueAsString(list);
	}
	
	public void insertPlaylist(String owner, String name, String listStr) throws JsonProcessingException, JsonProcessingException {
		List<Song> list = mapper.readValue(listStr, new TypeReference<List<Song>>(){});
		Playlist playlist = new Playlist();
		String id = owner + name;
		
		playlist.set_id(id);
		playlist.setOwner(owner);
		playlist.setName(name);
		playlist.setTracklist(list);
		
		playlistRepository.insert(playlist);
	}
	
	public void deletePlaylist(String owner, String name) {
		String id = owner + name;
		playlistRepository.deleteById(id);
	}
}
