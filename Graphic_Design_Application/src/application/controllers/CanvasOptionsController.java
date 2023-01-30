package application.controllers;

import application.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CanvasOptionsController {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	@FXML private ColorPicker bgColour_CP;
	
	// Initialize values
	Color shapeBGColour;
	Color bgColour;
	String backgroundColour;
	boolean initialised;
	
	/**
	 * Contains event handlers for canvas options
	 */
	@FXML
	public void initialize() {
		
		// Obtain the current node.
		Node node = model.getSelectedNode();

		// Initialize background colour.
		if (initialised) {
		bgColour = (Color) ((Pane) node).getUserData();
		bgColour_CP.setValue(bgColour);
		// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
		backgroundColour = "rgb("+(int)(bgColour.getRed() * 255)+", "+(int)(bgColour.getGreen() * 255)+", "+(int)(bgColour.getBlue() * 255)+")";
		}
		
		// Change background colour.
		bgColour_CP.setOnAction(e -> {
			bgColour = (Color) bgColour_CP.getValue();
			// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
			backgroundColour = "rgb("+(int)(bgColour.getRed() * 255)+", "+(int)(bgColour.getGreen() * 255)+", "+(int)(bgColour.getBlue() * 255)+")";
			setBackgroundColour(node);
			setInitialisation(node);
			initialised = true;
		});
	}
	
	
	/**
	 * Set canvas background colour.
	 */
	public void setBackgroundColour(Node node) {
		((Pane) node).setStyle("-fx-background-color: " + backgroundColour + ";");
	}
	
	
	/**
	 * Set initialization data inside node, as data cannot be accessed from node itself.
	 */
	public void setInitialisation(Node node) {
		((Pane) node).setUserData(bgColour);
	}
}
