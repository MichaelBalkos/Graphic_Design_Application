package application.controllers;

import application.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutPageController implements Controller {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	private Stage newCanvasStage;
	private Stage parentStage;

	// Graphical node elements and their fx:ids
	@FXML private Button ok_BTN;

	/**
	 * About us page event handler
	 */
	@FXML
	public void initialize() {
		// Create new canvas based on the user's inputted dimensions.
		ok_BTN.setOnAction(event -> {
			newCanvasStage.close();
			parentStage.show();
		});
	}

	/**
	 * Obtain the previous stage and model from other classes.
	 */
	public AboutPageController(Stage parentStage) {
		this.newCanvasStage = new Stage();
		this.parentStage = parentStage;
	}

	/**
	 * Create a new about page scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root);
		newCanvasStage.setScene(scene);
		newCanvasStage.setResizable(false);
		newCanvasStage.setTitle("About Us");
		newCanvasStage.show();
	}
}