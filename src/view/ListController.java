package view;

import java.util.ArrayList;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
		Song s2 = new Song ("cwdnt", "park");
		Song s3 = new Song ("C want an A", "CS213");
		song.add(s1);
		song.add(s2);
		song.add(s3);
		
		//access song name and artist
		for(int counter = 0; counter < song.size(); ++counter) {
			Song currSong = song.get(counter);
			String listShow = currSong.songName + "-" + currSong.songArtist;
			
			obsList.add(listShow);
		}
		
		//Sorting observable list, case insensitive
		Comparator<String> comparator = Comparator.comparing(String::toString,String.CASE_INSENSITIVE_ORDER);
		FXCollections.sort(obsList, comparator);
		
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
		if(newName.isEmpty() ||
				newArtist.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Name and Field Must be Populated!");
			String content = "Send by: Tongle Yao and Soo Park";
			alert.setContentText(content);
			alert.show();
		}
		else {
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
			
			//sorting new list
			Comparator<String> comparator = Comparator.comparing(String::toString,String.CASE_INSENSITIVE_ORDER);
			FXCollections.sort(obsList, comparator);
			listView.setItems(obsList);
		}
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
	
	@FXML TextField addName;
	@FXML TextField addArtist;
	@FXML TextField addYear;
	@FXML TextField addAlbum;
	
	public void addSong(ActionEvent e) {
		String newName = addName.getText();
		String newArtist = addArtist.getText();
		String newYear = addYear.getText();
		String newAlbum = addAlbum.getText();
		
		Song newSong = new Song(newName, newArtist);
		if(newYear != null) { newSong.songYear = newYear; }
		if(newAlbum != null) { newSong.songAlbum = newAlbum; }
		
		//Check if Exists
		for(int counter = 0; counter < song.size(); ++counter) {
			Song currSong = song.get(counter);
			if(currSong.songName.equals(newName) && currSong.songArtist.equals(newArtist)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Warning");
				alert.setHeaderText("Song already exists in the database!");
				String content = "Send by: Tongle Yao and Soo Park";
				alert.setContentText(content);
				alert.show();
				return;
			}
		}		
		
		song.add(newSong);
		obsList.add(newName + "-" + newArtist);
		Comparator<String> comparator = Comparator.comparing(String::toString,String.CASE_INSENSITIVE_ORDER);
		FXCollections.sort(obsList, comparator);
		listView.setItems(obsList);
		
		//Revert to empty fields
		addName.setText("");
		addArtist.setText("");
		addYear.setText("");
		addAlbum.setText("");
		
		//Select first item in list
		listView.getSelectionModel().select(0);	
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add Success!");
		alert.setHeaderText(newName + " by " + newArtist + " has been input into the database!");
		String content = "Send by: Tongle Yao and Soo Park";
		alert.setContentText(content);
		alert.show();
	}
	
	public void addClear() {
		addName.setText("");
		addArtist.setText("");
		addYear.setText("");
		addAlbum.setText("");
	}
	
	public void deleteSong() {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
		String songName = item.substring(0, item.lastIndexOf('-'));
		String artistName = item.substring(item.lastIndexOf('-') + 1, item.length());
		
		//Remove song from arraylist
		for(int counter = 0; counter < song.size(); ++counter) {
			Song currSong = song.get(counter);
			if(currSong.songName.equals(songName) && currSong.songArtist.equals(artistName)) {
				song.remove(counter);
				break;				
			}
		}
		
		//Remove song from ObsList
		obsList.remove(index);
		listView.setItems(obsList.sorted());
		
		//Revert to empty show details
		selectName.setText("");
		selectArtist.setText("");
		selectYear.setText("");
		selectAlbum.setText("");
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Delete Success!");
		alert.setHeaderText(songName + " by " + artistName + " has been deleted from the database!");
		String content = "Send by: Tongle Yao and Soo Park";
		alert.setContentText(content);
		alert.show();

	}
}