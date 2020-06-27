package notu.cs.springboot.entity;

import java.util.List;

public class Playlist {

	String _id;
	String owner;
	String name;
	List<Song> tracklist;
	
	public String get_id() {
		return _id;
	}
	
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Song> getTracklist() {
		return tracklist;
	}
	
	public void setTracklist(List<Song> tracklist) {
		this.tracklist = tracklist;
	}
	
}
