package view;

public class Song {
	public String songName;
	public String songArtist;
	public String songYear;
	public String songAlbum;
	
	public Song(String songName, String songArtist) {
		this.songName = songName;
		this.songArtist = songArtist;
	}
		
	public Song(String songName, String songArtist, String songYear, String songAlbum) {
		this.songName = songName;
		this.songArtist = songArtist;
		this.songYear = songYear;
		this.songAlbum = songAlbum;
	}
}
