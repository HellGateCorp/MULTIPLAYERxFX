package de.fernschulen.mediaplayer;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Mediaplayer extends Application {

	public void start(Stage meineStage) throws Exception {
		//eine Instanz von FXMLLoader erzeugen
		FXMLLoader meinLoader = new FXMLLoader(getClass().getResource("sb_mediaplayer.fxml"));
		//die Datei laden
		Parent root = meinLoader.load();
		//den Controller beschaffen
		FXMLController meinController = meinLoader.getController();
		//und die Bühne übergeben
		meinController.setMeineStage(meineStage);
		//die Szene erzeugen
		//an den Konstruktor werden der oberste Knoten und die Größe übergeben
		Scene meineScene = new Scene(root, 800, 600);
		//den Titel über stage setzen
		meineStage.setTitle("MULTIMEDIAPLAYERxFX");
		//die Szene setzen
		meineStage.setScene(meineScene);
		//im Vollbild darstellen
		meineStage.setMaximized(true);
		//das Icon setzen
		File bilddatei = new File("icons/icon.gif");
		Image bild = new Image(bilddatei.toURI().toString());
		meineStage.getIcons().add(bild);
		//und anzeigen
		meineStage.show();
	}

	public static void main(String[] args) {
		//der Start
		launch(args);
	}
}
