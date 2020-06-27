package notu.cs.springboot.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import notu.cs.springboot.entity.Song;
import notu.cs.springboot.model.PlaylistConverter;
import notu.cs.springboot.model.YouTubeUrlConverter;

@Service
public class ConverterService {

	private PlaylistConverter playlistConverter;
	private YouTubeUrlConverter urlConverter;
	private ObjectMapper mapper;
	
	public ConverterService() throws IOException {
		playlistConverter = new PlaylistConverter();
		urlConverter = new YouTubeUrlConverter();
		mapper = new ObjectMapper();
	}
	
	public String parsePlaylistToken(String playlistToken) throws IOException {
		List<Song> oriList = playlistConverter.convert(playlistToken);
		urlConverter.setVideoUrl(oriList);
		List<Song> newList = urlConverter.getPlaylist();
		
		return mapper.writeValueAsString(newList);
	}
	
}
