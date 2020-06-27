package notu.cs.springboot.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import notu.cs.springboot.entity.Song;

@Component
public class PlaylistConverter {

	private final String accessTokenAPI = "https://open.spotify.com/get_access_token?reason=transport&productType=web_player";
	private final String playlistAPIPrefix = "https://api.spotify.com/v1/playlists/";
	
	private String getAccessTokenJSONString() throws IOException {
		URL url = new URL(accessTokenAPI);
		HttpURLConnection connect = (HttpURLConnection)url.openConnection();
		connect.setRequestMethod("GET");
		connect.connect();
		/*int status = connect.getResponseCode();
		System.out.println("AccessTokenAPI Response Code:" + status);*/
		
	    BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(),"utf-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
	    	sb.append(line + "\n");
	    }
	    br.close();
	    
	    return sb.toString();
	}
	
	private String getAccessToken() throws IOException {
		JSONObject obj = new JSONObject(getAccessTokenJSONString());
		String accessToken = obj.getString("accessToken");
		return accessToken;
	}
	
	private String getPlaylistJSONString(String playlistToken) throws IOException {
		URL url = new URL(playlistAPIPrefix + playlistToken);
		HttpURLConnection connect = (HttpURLConnection)url.openConnection();
		String auth = "Bearer " + getAccessToken();
		
		connect.setRequestMethod("GET");
		connect.setRequestProperty("Authorization", auth);
		connect.connect();
		/*int status = connect.getResponseCode();
		System.out.println("Response Code:" + status);*/
		
	    BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream(),"utf-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
	    	sb.append(line + "\n");
	    }
	    br.close();
	    
	    return sb.toString();
	}
	
	public List<Song> convert(String playlistToken) throws IOException {
		List<Song> songList = new ArrayList<Song>();
		JSONObject obj = new JSONObject(getPlaylistJSONString(playlistToken));
		JSONObject tracksObj = obj.getJSONObject("tracks");
		JSONArray playlist = tracksObj.getJSONArray("items");
		String title, artist, album;
		
		for (int i=0;i<playlist.length();i++) {
			JSONObject trackObj = playlist.getJSONObject(i).getJSONObject("track");
			JSONObject albumObj = trackObj.getJSONObject("album");
			Song temp = new Song();
			title = trackObj.getString("name");
			artist = albumObj.getJSONArray("artists").getJSONObject(0).getString("name");
			album = albumObj.getString("name");
			
			temp.setTitle(title);
			temp.setArtist(artist);
			temp.setAlbum(album);
			
			songList.add(temp);
		}
		
		return songList;
	}
	
}
