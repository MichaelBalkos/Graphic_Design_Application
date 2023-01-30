package application.controllers;

import application.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class NewCanvasController implements Controller {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();
	
	private Stage newCanvasStage; 
	private Stage parentStage;
	
	// Graphical node elements and their fx:ids
    @FXML private Button cancel_BTN;
    @FXML private Button ok_BTN;
    @FXML private TextField width_TF;
    @FXML private TextField height_TF;
    @FXML private Label message_LBL;
   
    
	/**
	 * Logic and handlers for creating a new canvas.
	 */
	@FXML
	public void initialize() {

		// Take user back to canvas editor.
		cancel_BTN.setOnAction(event -> {
			newCanvasStage.close();
			parentStage.show();
		});

		// Create new canvas based on the user's inputed dimensions.
		ok_BTN.setOnAction(event -> {
			try {
				Double width = Double.parseDouble(width_TF.getText());
				Double height = Double.parseDouble(height_TF.getText());

				// Enforce a minimum canvas size
				if (width < 200 || height < 200) {
					message_LBL.setText("Error: Canvas too small. Please enter height or width larger than 200px.");
					message_LBL.setTextFill(Color.RED);
					// Enforce a maximum canvas size
				} else if (width > 1200 || height > 1200) {
					message_LBL.setText("Error: Canvas too big. Please enter height or width smaller than 1200px.");
					message_LBL.setTextFill(Color.RED);
				} else {
					// Set canvas size.
					model.setCanvasHeight(height);
					model.setCanvasWidth(width);
					model.setCanvasChange(true);

					newCanvasStage.close();
					parentStage.show();
				}
			} catch (NumberFormatException e) {
				message_LBL.setText("Error: Please enter numbers only.");
				message_LBL.setTextFill(Color.RED);
			}
		});
	}

	
	/**
	 * Obtain the previous stage and model from other classes.
	 */
	public NewCanvasController(Stage parentStage) {
		this.newCanvasStage = new Stage();
		this.parentStage = parentStage;
	}

	
	/**
	 * Create a new login scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/resources/stylesheets/Board.css").toExternalForm());
		newCanvasStage.setScene(scene);
		newCanvasStage.setResizable(false);
		newCanvasStage.setTitle("Create new canvas");
		newCanvasStage.show();
	}
}