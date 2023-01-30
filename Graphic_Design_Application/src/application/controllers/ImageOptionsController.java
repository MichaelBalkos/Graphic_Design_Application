package application.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import application.model.Model;
import application.utility.Picture;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ImageOptionsController {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	@FXML private Button image_BTN;
	@FXML private Label imageMessage_LBL;
	@FXML private Slider width_S;
	@FXML private Slider height_S;
	@FXML private Slider rotation_S;
	
	
	/**
	 * Contains event handlers for ImageView options.
	 */
	@FXML
	public void initialize() {

		// Obtain the current node.
		Node node = model.getSelectedNode();

		// Initialize rotation, width and height sliders.
		rotation_S.setValue(((ImageView) node).getRotate());

		// Initialize width and height sliders based on ImageView instance.
		width_S.setValue(((ImageView) node).getFitWidth());
		height_S.setValue(((ImageView) node).getFitHeight());

		// Rotation slider functionality.
		image_BTN.setOnAction(e -> {
			try {
				Image img = new Image(new ByteArrayInputStream(Picture.convertPicture(model.getCurrentStage())));
				((ImageView) node).setImage(img);
			} catch (NullPointerException e1) {
				imageMessage_LBL.setText("Error: Unable to convert selected image to byte array.");
				imageMessage_LBL.setTextFill(Color.RED);
			} catch (IOException e1) {
				imageMessage_LBL.setText("Error: Please select an image before closing the dialog box.");
				imageMessage_LBL.setTextFill(Color.RED);
			}
		});

		// Rotation slider functionality.
		rotation_S.valueProperty().addListener((o, ov, newValue) -> {
			((ImageView) node).setRotate(newValue.intValue());
		});

		// Width slider functionality for ImageView.
		width_S.valueProperty().addListener((o, ov, newValue) -> {
			((ImageView) node).setFitWidth(newValue.intValue());
		});

		// Height ImageView slider functionality
		height_S.valueProperty().addListener((o, ov, newValue) -> {
			((ImageView) node).setFitHeight(newValue.intValue());
		});
	}
}
