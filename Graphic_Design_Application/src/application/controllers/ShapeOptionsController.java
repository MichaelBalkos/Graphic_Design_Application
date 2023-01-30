package application.controllers;

import application.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeOptionsController {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	@FXML private ColorPicker bgColour_CP;
	@FXML private TextField borderWidth_TF;
	@FXML private Slider height_S;
	@FXML private Slider rotation_S;
	@FXML private ColorPicker shapeColour_CP;
	@FXML private Label shapeMessage_LBL;
	@FXML private Slider width_S;
	@FXML private Label shapeWidth_LBL;
	@FXML private Label rotation_LBL;
	@FXML private Label height_LBL;
	 
	// Initialize values
	Color shapeColour;
	int borderWidth;
	Color shapeBGColour;
	
	
	/**
	 * Contains event handlers for shape options
	 */
	@FXML
	public void initialize() {
		// Obtain the current node.
		Node node = model.getSelectedNode();

		// Initialize shape border colour.
		Color fillBorder = (Color) ((Shape) node).getStroke();
		shapeColour_CP.setValue(fillBorder);

		// Initialize shape border width.
		double borderWidthDouble = ((Shape) node).getStrokeWidth();
		int borderWidthInt = (int) borderWidthDouble;
		borderWidth_TF.setText("" + borderWidthInt);
		borderWidth = borderWidthInt;

		// Initialize shape background colour.
		Color fill = (Color) ((Shape) node).getFill();
		bgColour_CP.setValue(fill);

		// Initialize rotation slider,
		rotation_S.setValue(((Shape) node).getRotate());

		// Initialize width and height sliders based on shape instance.
		if (node instanceof Rectangle) {
			width_S.setValue(((Rectangle) node).getWidth());
			height_S.setValue(((Rectangle) node).getHeight());
		} else {
			width_S.setValue(((Circle) node).getRadius());

			// Hide and change unnecessary options for circle shapes.
			height_S.setVisible(false);
			height_LBL.setVisible(false);
			rotation_S.setVisible(false);
			rotation_LBL.setVisible(false);
			shapeWidth_LBL.setText("Shape Radius");
		}

		// Change shape's border colour.
		shapeColour_CP.setOnAction(e -> {
			shapeColour = (Color) shapeColour_CP.getValue();
			((Shape) node).setStroke(shapeColour);
		});

		// Change text's border width.
		borderWidth_TF.setOnAction(e -> {
			try {
				borderWidth = Integer.parseInt(borderWidth_TF.getText());
				((Shape) node).setStrokeWidth(borderWidth);
			} catch (NumberFormatException ex) {
				shapeMessage_LBL.setText("Error: Please enter numbers only.");
				borderWidth_TF.setText("");
			}
		});

		// Change shape background colour.
		bgColour_CP.setOnAction(e -> {
			shapeBGColour = (Color) bgColour_CP.getValue();
			((Shape) node).setFill(shapeBGColour);
		});

		// Rotation slider functionality
		rotation_S.valueProperty().addListener((o, ov, newValue) -> {
			((Shape) node).setRotate(newValue.intValue());
		});

		// Width/Radius combined slider functionality.
		width_S.valueProperty().addListener((o, ov, newValue) -> {
			if (node instanceof Rectangle) {
				((Rectangle) node).setWidth(newValue.intValue());
			} else {
				((Circle) node).setRadius(newValue.intValue());
			}
		});

		// Height rectangle slider functionality
		height_S.valueProperty().addListener((o, ov, newValue) -> {
			if (node instanceof Rectangle) {
				((Rectangle) node).setHeight(newValue.intValue());
			}
		});
	}
}
