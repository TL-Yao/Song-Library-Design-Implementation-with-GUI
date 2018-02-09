package view;

import java.lang.Object;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListController {
	@FXML ListView<String> listView;                
	@FXML TextField selectArtist;
	@FXML TextField selectName;
	@FXML TextField selectYear;
	@FXML TextField selectAlbum;
	
	ArrayList<Song> song;
	private ObservableList<String> obsList;              

	public void start(Stage mainStage) {
		song = new ArrayList<Song>();
		obsList = FXCollections.observableArrayList();
		
		//fake data
		Song s1 = new Song ("good", "tongle", "1997", "great");
		Song s2 = new Song ("bad", "park");
		song.add(s1);
		song.add(s2);
		
		//access song name and artist
		for(int counter = 0; counter < song.size(); ++counter) {
			Song currSong = song.get(counter);
			String listShow = currSong.songName + "-" + currSong.songArtist;
			
			obsList.add(listShow);
		}
        
		//set listener for show song information
		listView
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) ->
				showInfo(mainStage)
				);
		listView.setItems(obsList);
	}
	
	public void showInfo(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		String songName = item.substring(0, item.lastIndexOf('-'));
		String artistName = item.substring(item.lastIndexOf('-') + 1, item.length());
		
		for(Song s : song) {
			if(s.songArtist.equals(artistName) &&
					s.songName.equals(songName)) {
				//show target song information in textField
				selectName.setText(s.songName);
				selectArtist.setText(s.songArtist);
				selectAlbum.setText(s.songAlbum);
				selectYear.setText(s.songYear);
				break;
			}
		}
	}
	
	public void saveOverWrite(ActionEvent e) {
		//access current selection
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		String songName = item.substring(0, item.lastIndexOf('-'));
		String artistName = item.substring(item.lastIndexOf('-') + 1, item.length());
		
		Button overWrite = (Button)e.getSource();
		
		//access user input
		String newName = selectName.getText();
		String newArtist = selectArtist.getText();
		String newAlbum = selectAlbum.getText();
		String newYear = selectYear.getText();
		
		//leave blank for alert empty input later!!!!!!!!!!!!
		//
		//
		//
		//
		//
		
		for(Song s : song) {
			if(s.songArtist.equals(artistName) &&
					s.songName.equals(songName)) {
				//edit song
				s.songName = newName;
				s.songArtist = newArtist;
				s.songAlbum = newAlbum;
				s.songYear = newYear;
				break;
			}
		}
		
		//reload listview
		String newTitle = newName + "-" + newArtist;
		obsList.set(index, newTitle);
		listView.setItems(obsList);
	}
	
	public void revertOverWrite(ActionEvent e) {
		int index = listView.getSelectionModel().getSelectedIndex();
		String item = listView.getSelectionModel().getSelectedItem();
		String songName = item.substring(0, item.lastIndexOf('-'));
		String artistName = item.substring(item.lastIndexOf('-') + 1, item.length());
		
		for(Song s : song) {
			if(s.songArtist.equals(artistName) &&
					s.songName.equals(songName)) {
				//show target song information in textField
				selectName.setText(s.songName);
				selectArtist.setText(s.songArtist);
				selectAlbum.setText(s.songAlbum);
				selectYear.setText(s.songYear);
				break;
			}
		}
	}
}
