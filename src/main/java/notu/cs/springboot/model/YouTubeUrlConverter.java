package notu.cs.springboot.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import notu.cs.springboot.entity.Song;

@Component
public class YouTubeUrlConverter {
	
	private final String getVideoInfoAPI ="https://www.googleapis.com/youtube/v3/search?maxResults=1&key=AIzaSyDeP8Cr9uCG8gJbKC12OqosIrH3WybEzz8&type=video&q=";
	private final String videoUrlPrefix = "https://www.youtube.com/watch?v=";
	private List<Song> tracklist;
	//System.out.println();
	public void setVideoUrl(List<Song> list) throws IOException {
		String q, queryUrl, videoUrl, videoId, artist, title;
		tracklist = list;
		
		for (Song song:tracklist) {
			artist = song.getArtist().replaceAll(" +", "+");
			title = song.getTitle().replaceAll(" +", "+");
			q = artist + "+" + title;
			queryUrl = getVideoInfoAPI + q;
			//System.out.println(queryUrl);
			videoId = getVideoId(queryUrl);
			videoUrl = videoUrlPrefix + videoId;
			song.setUrl(videoUrl);
			song.setVideoId(videoId);
		}
	}
	
	private String getVideoInfoJSONString(String urlStr) throws IOException {
		URL url = new URL(urlStr);
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
	
	private String getVideoId(String url) throws IOException {
		JSONObject obj = new JSONObject(getVideoInfoJSONString(url));
		String videoId = obj.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId");
		return videoId;
	}
	
	public List<Song> getPlaylist() {
		return tracklist;
	}

}
