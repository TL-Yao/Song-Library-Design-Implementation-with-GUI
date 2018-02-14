package view;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
		
		load();
		listView.getSelectionModel().select(0);
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
	
	public boolean isBlank (String str) {
		if(str == null) {
			return false;
		}
		
		String s = str.trim();
		
		if(s.equals("")) {
			return false;
		}else{
			return true;
		}
		
	}
	
	public void showInfo(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		if(item == null) {
			return;
		}
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
		
		int index = listView.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			//no pre-selection item
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Please select a pre-existing item in the list first!");
			String content = "Send by: Tongle Yao and Soo Park";
			alert.setContentText(content);
			alert.show();
			return;
		}
		
		//access current selection
		String item = listView.getSelectionModel().getSelectedItem();
		String songName = item.substring(0, item.lastIndexOf('-'));
		String artistName = item.substring(item.lastIndexOf('-') + 1, item.length());
		
		//user conformation
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Look, a Confirmation Dialog");
		alert.setContentText("Are you ok with this?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
			//access user input
			String newName = selectName.getText();
			String newArtist = selectArtist.getText();
			String newAlbum = selectAlbum.getText();
			String newYear = selectYear.getText();
			
			if(newName.isEmpty() ||
					newArtist.isEmpty() ||
					newName.trim().isEmpty() ||
					newArtist.trim().isEmpty()) {
				
				//Name and Artist can not be empty 
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Warning");
				alert2.setHeaderText("Name and Field Must be Populated!");
				String content = "Send by: Tongle Yao and Soo Park";
				alert2.setContentText(content);
				alert2.show();
			}
			else {
				if(newYear != null) {
					newYear = newYear.trim();
					
					if(!newYear.equals("")) {
						//newYear is  not whitespace
						for(int pos = 0; pos < newYear.length(); ++pos) {
							if(newYear.charAt(pos) < 48 ||
									newYear.charAt(pos) > 57 ||
									newYear.length() != 4 ||
									Integer.parseInt(newYear) > 2018) {
								//check if user input valid year
								Alert alert3 = new Alert(AlertType.INFORMATION);
								alert3.setTitle("Warning");
								alert3.setHeaderText("Year must be between 1000 - 2018.");
								String content = "Send by: Tongle Yao and Soo Park";
								alert3.setContentText(content);
								alert3.show();
							 	return;
							}
						}
					}
				}
				
				//Check if the item already exists
				for(int counter = 0; counter < song.size(); ++counter) {
					Song currSong = song.get(counter);
					if(currSong.songName.equals(newName) && 
							currSong.songArtist.equals(newArtist) &&
							!currSong.songName.equals(songName) &&
							!currSong.songArtist.equals(artistName)) {

						Alert alert1 = new Alert(AlertType.INFORMATION);
						alert1.setTitle("Warning");
						alert1.setHeaderText("Song already exists in the database!");
						String content = "Send by: Tongle Yao and Soo Park";
						alert1.setContentText(content);
						alert1.show();
						return;
					}
				}
				
				newName = newName.trim();
				newArtist = newArtist.trim();
				if(newAlbum != null) { newAlbum = newAlbum.trim(); }
				
				for(Song s : song) {
					if(s.songArtist.equals(artistName) &&
							s.songName.equals(songName)) {
						//find target song and edit it
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
		} else {
		    return;
		}
		
		
	}
	
	public void revertOverWrite(ActionEvent e) {
		int index = listView.getSelectionModel().getSelectedIndex();
		
		if(index == -1) {
			//no pre-selection item
			return;
		}
		
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
		
		if(newName.isEmpty() ||
				newArtist.isEmpty()) {
		//Name and Artist can not be empty 
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Name and Field Must be Populated!");
			String content = "Send by: Tongle Yao and Soo Park";
			alert.setContentText(content);
			alert.show();
			return;
		}
		
		if(newYear != null) {
			for(int pos = 0; pos < newYear.length(); ++pos) {
				if(newYear.charAt(pos) < 48 ||
						newYear.charAt(pos) > 57 ||
						newYear.length() != 4 ||
						Integer.parseInt(newYear) > 2018) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setHeaderText("Year must be between 1000 - 2018.");
					String content = "Send by: Tongle Yao and Soo Park";
					alert.setContentText(content);
					alert.show();
				 	return;
				}
			}
			newSong.songYear = newYear;
		}

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
		
		//Select the new item in list
		String target = newName + "-" + newArtist;
		int index = obsList.indexOf(target);
		listView.getSelectionModel().select(index);	
		
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
		
		if(index == -1) {
			return;
		}
		
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
		
		//pre-select the previous one
		if(index != 0) {
			listView.getSelectionModel().select(index - 1);	
		}
		else {
			listView.getSelectionModel().select(index);	
		}
		
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
	
	public void save() {
		File saveFile = new File("save.txt");
		try {
			if(saveFile == null) { return; }
			
			//Write into file
			FileWriter fw = new FileWriter(saveFile);
	
			fw.write("SPTYSONGLIBRARY");
			fw.write(System.getProperty("line.separator"));
			
			for(Song s : song) {
				fw.write(
						"Name:" + s.songName + "\t" +
						"Artist:" + s.songArtist + "\t" +
						"Album:" + s.songAlbum + "\t" +
						"Year:" + s.songYear
						);
				fw.write(System.getProperty("line.separator"));
			}

			
			fw.close();
			
		} catch(IOException ex) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Save File Failed - Please Try Again.");
			String content = "Send by: Tongle Yao and Soo Park";
			alert.setContentText(content);
			alert.show();
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success!");
		alert.setHeaderText("Song list has been saved!");
		String content = "Send by: Tongle Yao and Soo Park";
		alert.setContentText(content);
		alert.show();
		
	}
	
	public void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("save.txt"));
			String fileLine = br.readLine();
			String[] songContents;

			if(fileLine == null) {
				br.close();
				return;
			}
									
			song = new ArrayList<Song>();
			obsList.clear();
			
			while((fileLine = br.readLine()) != null) {
				songContents = fileLine.split("\t");
				int count = 0;
				String newName = null, newArtist = null, newYear = null, newAlbum = null;

				for(String songPart : songContents) {

					int colon = songPart.indexOf(":");
					if(count == 0) {
						newName = songPart.substring(colon + 1, songPart.length());
					} else if(count == 1) {
						newArtist = songPart.substring(colon + 1, songPart.length());
					} else if (count == 2) {
						newYear = songPart.substring(colon + 1, songPart.length());
					} else {
						newAlbum = songPart.substring(colon + 1, songPart.length());						
					}
					count++;
				}				
				Song newSong = new Song(newName, newArtist, newYear, newAlbum);
				song.add(newSong);
				obsList.add(newName + "-" + newArtist);

			}
			
			br.close();
		} catch(IOException ex) {			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Oops!");
			alert.setHeaderText("Something went wrong with the initial load.!");
			String content = "Send by: Tongle Yao and Soo Park";
			alert.setContentText(content);
			alert.show();			
		}
		
		//Select first item in list
		Comparator<String> comparator = Comparator.comparing(String::toString,String.CASE_INSENSITIVE_ORDER);
		FXCollections.sort(obsList, comparator);
		listView.setItems(obsList);
		listView.getSelectionModel().select(0);	

	}
}