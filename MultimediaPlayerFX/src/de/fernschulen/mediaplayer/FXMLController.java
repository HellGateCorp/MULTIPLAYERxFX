package de.fernschulen.mediaplayer;

import java.io.File;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class FXMLController {
	//für die Bühne
	private Stage meineStage;
	private ActionEvent event;
	//für den Player
	private MediaPlayer mediaplayer;
	// Speichert die eingelesene Datei zwischen
	// für 20 Listeneinträge der GUI
	private File[] datei = new File[20];
	//ein Counter für das File array
	private int counterD=0;
	// für den vergleich der Einträge
	private boolean vorhanden=false;
	//für die MediaView
	@FXML private MediaView mediaview;
	//für die ImageView mit dem Symbol
	@FXML private ImageView symbol;
	//für das Listenfeld
	@FXML private ListView<String> liste;
	// für den wechsel der Symbole zu Play/Break 
	// sowie dem deaktivieren der Steuerung
	@FXML private Button pause;
	@FXML private Button stop;
	@FXML private Button play;
	@FXML private Button volume;
		  
	//die Methode setzt die Bühne auf den übergebenen Wert
	public void setMeineStage(Stage meineStage) {
		this.meineStage = meineStage;
		mitOderOhne();
		}
	//Methode die erfragt ob das EasterEgg abgespielt werden soll
	@FXML private void mitOderOhne() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Your Content are loaded !");
		alert.setContentText("You want to see what it is?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				anfangsZustand(event);
			} else 
				return;
		}
	
	//Methode welche die Datei des EasterEggs einliest und durchreicht
	@FXML public void anfangsZustand(ActionEvent event) {
		// Für die Auswahl einer eingespielten Datei
		File datei = new File("videoplayback.mp4");
		if (datei != null)
		dateiLaden(datei);
		// und die Steuerungsbutton aktivieren
		buttonsOn(event);
		}

	//die Methode zum Laden
	@FXML protected void ladenKlick(ActionEvent event) {
		//eine neue Instanz der Klasse FileChooser erzeugen
		FileChooser oeffnenDialog = new FileChooser();
		//den Titel für den Dialog setzen
		oeffnenDialog.setTitle("Datei öffnen");
		//die Filter setzen
		oeffnenDialog.getExtensionFilters().add(new ExtensionFilter("Audiodateien", "*.mp3"));
		oeffnenDialog.getExtensionFilters().add(new ExtensionFilter("Videodateien", "*.mp4"));
		//den Startordner auf den Benutzerordner setzen
		oeffnenDialog.setInitialDirectory(new File(System.getProperty("user.home")));
		//den Öffnendialog anzeigen und das Ergebnis beschaffen
		File datei = oeffnenDialog.showOpenDialog(meineStage);
		if (this.datei[counterD] != null)
			compare(datei.toString());
		//wurde eine Datei ausgewählt
		if (datei != null  && vorhanden == false)
			//dann über eine eigene Methode laden
			dateiLaden(datei);
			// und die Steuerungsbutton aktivieren
			buttonsOn(event);
	}
	
	// Methode für die Aktivierung der Steuerungseinheit
	@FXML protected void buttonsOn(ActionEvent event) {
		play.setDisable(false);
		pause.setDisable(false);
		stop.setDisable(false);
		volume.setDisable(false);
		
	}

	//die Methode zum Stoppen
	@FXML protected void stoppKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		if (mediaplayer != null)
			//dann anhalten
			mediaplayer.stop();
			//umschalten play/pause
			play.setVisible(true);
			pause.setVisible(false);
	}

	//die Methode für die Pause
	@FXML protected void pauseKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		if (mediaplayer != null)
			//dann unterbrechen
			mediaplayer.pause();
			play.setVisible(true);
			pause.setVisible(false);
	}

	//die Methode für die Wiedergabe
	@FXML protected void playKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		if (mediaplayer != null)
			//dann wiedergeben
			mediaplayer.play();
			//umschalten play/pause
			play.setVisible(false);
			pause.setVisible(true);
	}

	//die Methode für das Ein- und Ausschalten der Lautstärke
	@FXML protected void lautsprecherKlick(ActionEvent event) {
		//gibt es überhaupt einen Mediaplayer?
		String dateiname;
		if (mediaplayer != null) {
			//ist die Lautstärke 0?
			if (mediaplayer.getVolume() == 0) {
				//dann auf 100 setzen
				mediaplayer.setVolume(100);
				//und das "normale" Symbol setzen
				dateiname = "icons/mute.gif";
			}
			else {
				//sonst auf 0 setzen
				mediaplayer.setVolume(0);
				//und das durchgestrichene Symbol setzen
				dateiname = "icons/mute_off.gif";	
			}
			//das Bild erzeugen und anzeigen
			File bilddatei = new File(dateiname);
			Image bild = new Image(bilddatei.toURI().toString());
			symbol.setImage(bild);	
		}
	}

	//die Methode zum Beenden
	@FXML protected void beendenKlick(ActionEvent event) {
		Platform.exit();
	}
	
	// die Methode zum wiederholten Abspielen 
	// einer angeklickten Datei, implementiert MouseEvent
	@FXML public void eintragKlick(javafx.scene.input.MouseEvent e) {
		//wurde der eintrag 1x angeklickt und überprüfe
		// ob überhaupt ein Eintrag markiert ist
		if(e.getClickCount() ==1 && liste.getSelectionModel().getSelectedItem() != null)
			compare(liste.getSelectionModel().getSelectedItem());
		System.out.println(liste.getSelectionModel().getSelectedItem());
	}
	//Methode zum Vergleichen bereits gesetzter Einträge 
	private void compare(String eintrag) {
		//Schleife die alle bereits eingelsenen Dateinamen
		//mit dem eingereichten vergleicht
		for(int i=0; i <= counterD; i++) {
			if(this.datei[i].toString().compareTo(eintrag) == 0) { 
				vorhanden = true;
				dateiLaden(this.datei[i]);
				break;
		}
			else 
				vorhanden = false;
		}
	}
	//die Methode lädt eine Datei
	public void dateiLaden(File datei) {
		//läuft schon eine Wiedergabe?
		if (mediaplayer != null && mediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
			//dann anhalten
			mediaplayer.stop();
		}
		//das Medium, den Mediaplayer und die MediaView erzeugen
		try {	
			Media medium = new Media(datei.toURI().toString());
			mediaplayer = new MediaPlayer(medium);
			mediaview.setMediaPlayer(mediaplayer);
			//die Wiedergabe starten
			mediaplayer.play();
			// und switch zu pause
			play.setVisible(false);
			pause.setVisible(true);
			// existiert bisher ein Eintrag
			if(vorhanden == false && this.datei[counterD] == null) {
				this.datei[counterD] = datei;
				
				// und der Itemliste hinzufügen
				liste.getItems().add(this.datei[counterD++].toString());
			}
			vorhanden = false;
			//und die Titelleiste anpassen
			meineStage.setTitle("JavaFX Multimedia-Player " + medium.toString());	
			
		}
		catch(Exception ex) {
			//den Dialog erzeugen und anzeigen
			Alert meinDialog = new Alert(AlertType.INFORMATION, "Beim Laden hat es ein Problem gegeben.\n" + ex.getMessage());
			//den Text setzen
			meinDialog.setHeaderText("Bitte beachten");
			meinDialog.initOwner(meineStage);
			//den Dialog anzeigen
			meinDialog.showAndWait();
		}
	}
}
