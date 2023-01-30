package application.controllers;

import java.util.ArrayList;

import application.model.Model;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class TextOptionsController {
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	// Text field contents
	@FXML private TextField text_TF;

	// Text fonts
	@FXML private MenuButton font_MB;
	@FXML private MenuItem system_MI;
	@FXML private MenuItem calibri_MI;
	@FXML private MenuItem timesNewRoman_MI;
	@FXML private MenuItem comicSans_MI;

	// Additional text options
	@FXML private TextField fontSize_TF;
	@FXML private Button bold_BTN;
	@FXML private Button italics_BTN;
	@FXML private ColorPicker textColour_CP;
	@FXML private Button leftAlign_BTN;
	@FXML private Button centerAlign_BTN;
	@FXML private Button rightAlign_BTN;
	
	// Font border
	@FXML private ColorPicker borderColour_CP;
	@FXML private TextField borderWidth_TF;
	
	// Text background
	@FXML private ColorPicker backgroundColour_CP;
	
	// Text rotation and sizing
	@FXML private Slider rotation_S;
	@FXML private Slider width_S;
	@FXML private Slider height_S;

	// Error message
	@FXML private Label textMessage_LBL;
	
	// Initialize values
	String currentFont;
	int fontSize;
	boolean fontBold;
	boolean fontItalic;
	FontWeight bold;
	FontPosture italic;
	Color textColour;
	String borderColour;
	String backgroundColour;
	int borderWidth;
	Color bColour;
	Color bgColour;

	
	/**
	 * Event handlers and logic for text options.
	 */
	@FXML
	public void initialize() {

		// Obtain the current node.
		Node node = model.getSelectedNode();

		// Initialize error message.
		textMessage_LBL.setText("");
		textMessage_LBL.setTextFill(Color.RED);

		// Initialize text with current text.
		String text = ((Label) node).getText();
		text_TF.setText(text);

		// Initialize font family menu button.
		String fontFamily = ((Label) node).getFont().getFamily();
		font_MB.setText(fontFamily);

		// Initialize font size with current text font.
		double fontSizeDouble = ((Label) node).getFont().getSize();
		int fontSizeInt = (int) fontSizeDouble;
		fontSize_TF.setText("" + fontSizeInt);
		fontSize = fontSizeInt;

		// Initialize text colour.
		Color fill = (Color) ((Label) node).getTextFill();
		textColour_CP.setValue(fill);

		// Initialize border colour, width and background colour.
		ArrayList<Object> arrayResult = (ArrayList) ((Label) node).getUserData();
		if (arrayResult != null) {
			for (int i = 0; i < arrayResult.size(); i++) {

				// Border width
				borderWidth_TF.setText("" + (int) arrayResult.get(0));
				borderWidth = (int) arrayResult.get(0);

				// Border colour
				borderColour_CP.setValue((Color) arrayResult.get(1));
				bColour = (Color) arrayResult.get(1);
				if (bColour != null) {
					// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
					borderColour = "rgb(" + (int) (bColour.getRed() * 255) + ", " + (int) (bColour.getGreen() * 255)
							+ ", " + (int) (bColour.getBlue() * 255) + ")";
				}

				// Background colour
				backgroundColour_CP.setValue((Color) arrayResult.get(2));
				bgColour = (Color) arrayResult.get(2);
				if (bgColour != null) {
					// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
					backgroundColour = "rgb(" + (int) (bgColour.getRed() * 255) + ", "
							+ (int) (bgColour.getGreen() * 255) + ", " + (int) (bgColour.getBlue() * 255) + ")";
				}
			}
		}

		// Initialize rotation, width and height sliders.
		rotation_S.setValue(((Label) node).getRotate());
		width_S.setValue(((Label) node).getPrefWidth());
		height_S.setValue(((Label) node).getPrefHeight());

		// Change textfield's text.
		text_TF.setOnAction(e -> {
			((Label) node).setText(text_TF.getText());
		});

		// Obtain current text's font.
		currentFont = ((Label) node).getFont().getFamily();

		// Change text font to system.
		system_MI.setOnAction(e -> {
			font_MB.setText("System");
			currentFont = "System";
			setFont(node);
		});

		// Change text font to system.
		calibri_MI.setOnAction(e -> {
			font_MB.setText("Calibri");
			currentFont = "Calibri";
			setFont(node);
		});

		// Change text font to system.
		timesNewRoman_MI.setOnAction(e -> {
			font_MB.setText("Times New Roman");
			currentFont = "Times New Roman";
			setFont(node);
		});

		// Change text font to system.
		comicSans_MI.setOnAction(e -> {
			font_MB.setText("Comic Sans");
			currentFont = "Comic Sans MS";
			setFont(node);
		});

		// Change textfield's font size.
		fontSize_TF.setOnAction(e -> {
			try {
				int number = Integer.parseInt(fontSize_TF.getText());
				fontSize = number;
				setFont(node);
			} catch (NumberFormatException ex) {
				textMessage_LBL.setText("Error: Please enter numbers only.");
				fontSize_TF.setText("");
			}
		});

		// Change textfield's font weight (bold).
		bold_BTN.setOnAction(e -> {
			if (!fontBold) {
				bold = FontWeight.BOLD;
				fontBold = true;
				setFont(node);
			} else if (fontBold) {
				bold = FontWeight.NORMAL;
				fontBold = false;
				setFont(node);
			}
		});

		// Change textfield's font posture (italics).
		italics_BTN.setOnAction(e -> {
			if (!fontItalic) {
				italic = FontPosture.ITALIC;
				fontItalic = true;
				setFont(node);
			} else if (fontItalic) {
				italic = FontPosture.REGULAR;
				fontItalic = false;
				setFont(node);
			}
		});

		// Change text colour.
		textColour_CP.setOnAction(e -> {
			textColour = (Color) textColour_CP.getValue();
			((Label) node).setTextFill(textColour);
		});

		// Change text to left align.
		leftAlign_BTN.setOnAction(e -> {
			((Label) node).setTextAlignment(TextAlignment.LEFT);
		});

		// Change text to center align.
		centerAlign_BTN.setOnAction(e -> {
			((Label) node).setTextAlignment(TextAlignment.CENTER);
		});

		// Change text to right align.
		rightAlign_BTN.setOnAction(e -> {
			((Label) node).setTextAlignment(TextAlignment.RIGHT);
		});

		// Change text's border colour.
		borderColour_CP.setOnAction(e -> {
			bColour = (Color) borderColour_CP.getValue();
			// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
			borderColour = "rgb(" + (int) (bColour.getRed() * 255) + ", " + (int) (bColour.getGreen() * 255) + ", "
					+ (int) (bColour.getBlue() * 255) + ")";
			setFontEffects(node);
			setInitialisation(node);
		});

		// Change text's border width.
		borderWidth_TF.setOnAction(e -> {
			try {
				borderWidth = Integer.parseInt(borderWidth_TF.getText());
				setFontEffects(node);
				setInitialisation(node);
			} catch (NumberFormatException ex) {
				textMessage_LBL.setText("Error: Please enter numbers only.");
				fontSize_TF.setText("");
			}
		});

		// Change text's background colour.
		backgroundColour_CP.setOnAction(e -> {
			bgColour = (Color) backgroundColour_CP.getValue();
			// Convert ColorPicker colour to CSS colour format "rgb(,,,)".
			backgroundColour = "rgb(" + (int) (bgColour.getRed() * 255) + ", " + (int) (bgColour.getGreen() * 255)
					+ ", " + (int) (bgColour.getBlue() * 255) + ")";
			setFontEffects(node);
			setInitialisation(node);
		});

		// Rotation slider functionality
		rotation_S.valueProperty().addListener((o, ov, newValue) -> {
			((Label) node).setRotate(newValue.intValue());
		});

		// Width slider functionality
		width_S.valueProperty().addListener((o, ov, newValue) -> {
			((Label) node).setPrefWidth(newValue.intValue());
		});

		// Height slider functionality
		height_S.valueProperty().addListener((o, ov, newValue) -> {
			((Label) node).setPrefHeight(newValue.intValue());
		});
	}

	
	/**
	 * Set text font, weight, posture and size.
	 */
	public void setFont(Node node) {
		((Label) node).setFont(Font.font(currentFont, bold, italic, fontSize));
	}

	
	/**
	 * Set text border colour, width, and background colour.
	 */
	public void setFontEffects(Node node) {
		((Label) node).setStyle("-fx-border-width: " + borderWidth + "; -fx-border-color: " + borderColour
				+ "; -fx-background-color: " + backgroundColour + ";");
	}

	
	/**
	 * Set initialization data inside node, as data cannot be accessed from node itself.
	 */
	public void setInitialisation(Node node) {
		ArrayList<Object> initialisation = new ArrayList();
		initialisation.clear();
		initialisation.add(borderWidth);
		initialisation.add(bColour);
		initialisation.add(bgColour);
		((Label) node).setUserData(initialisation);
	}
}
