package application.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import application.model.Model;
import application.model.User;
import application.utility.Picture;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class BoardController implements Controller {
	
	public BoardController() {
	}

	private Stage boardStage;
	private Stage parentStage;
	
	// Obtain User singleton.
	User user = User.getInstance();
	
	// Obtain Model singleton.
	Model model = Model.getInstance();

	// Left top menu nodes
	@FXML private MenuItem newCanvas_MI;
	@FXML private MenuItem clearCanvas_MI;
	@FXML private MenuItem saveCanvas_MI;
	@FXML private MenuItem editCanvas_MI;
	@FXML private MenuItem deleteElement_MI;
	@FXML private MenuItem about_MI;

	// Right top menu nodes
	@FXML private ImageView profilePic_IMG;
	@FXML private Label username_LBL;
	@FXML private Button profile_BTN;
	@FXML private Button logout_BTN;
	
	// Left shape/text nodes
	@FXML private Button text_BTN;
	@FXML private Button image_BTN;
	@FXML private Button rectangle_BTN;
	@FXML private Button circle_BTN;
	@FXML private Button canvas_BTN;
	
	// Premium member templates
	@FXML private Button template1_BTN;
	@FXML private Button template2_BTN;
	@FXML private Button template3_BTN;
	
	// Board pane nodes
	@FXML private SplitPane splitPane_P;
	@FXML private VBox vBox_P;
	@FXML private Pane canvas_P;
	@FXML private BorderPane optionsPane_P;
	
	// Bottom nodes
	@FXML private Label zoom_LBL;
	@FXML private Slider slider_S;
	@FXML private Label position_LBL;
	@FXML private Label message_LBL;
	
	// Initialize node handler inner-class
	NodeEventHandlers nodeEventHandlers = new NodeEventHandlers();
	

	/**
	 * Main canvas functionality. Logic for board and canvas.
	 */
	@FXML
	public void initialize() {

		// Initial settings
		canvas_P.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		model.setCanvasChange(false);
		canvas_P.setVisible(false);

		slider_S.setVisible(false);
		zoom_LBL.setVisible(false);
		editCanvas_MI.setDisable(true);

		// Reset the user's information upon showing the board stage.
		boardStage.setOnShowing(event -> {
			// Set user's first and last name.
			username_LBL.setText(user.getFirstName() + " " + user.getLastName());

			// Set profile picture.
			if (user.getProfilePic() != null && user.getProfilePic().length > 0) {
				Image img = new Image(new ByteArrayInputStream(user.getProfilePic()));
				profilePic_IMG.setImage(img);
			}

			// If a new canvas is created, update width, height and show canvas.
			if (model.getCanvasChange()) {
				canvas_P.setPrefWidth(model.getCanvasWidth());
				canvas_P.setMinWidth(model.getCanvasWidth());
				canvas_P.setMaxWidth(model.getCanvasWidth());
				canvas_P.setPrefHeight(model.getCanvasHeight());
				canvas_P.setVisible(true);

				// Reset the flag to false;
				model.setCanvasChange(false);

				// Reset zoom to 100
				setPaneZoom(100);
				slider_S.setValue(100);

				// Show zoom slider
				slider_S.setVisible(true);
				zoom_LBL.setVisible(true);

				if (canvas_P.isVisible()) {
					newCanvas_MI.setDisable(true);
					editCanvas_MI.setDisable(false);
				}
			}
			
			// Initialize setting user's premium status.
			int premium = model.getIsPremium();
			if (premium == 1) {
				template1_BTN.setVisible(true);
				template2_BTN.setVisible(true);
				template3_BTN.setVisible(true);
			} else {
				template1_BTN.setVisible(false);
				template2_BTN.setVisible(false);
				template3_BTN.setVisible(false);
			}
		});

		// Apply event handlers to canvas.
		nodeEventHandlers.nodeFunctionality(canvas_P);

		// Add a new text to the canvas.
		text_BTN.setOnAction(event -> {
			if (canvas_P.isVisible()) {
				Label text = new Label("Text");
				text.setFont(Font.font("System", 22));
				text.setTextFill(Color.BLACK);
				text.setWrapText(true);
				// Assign event handlers to node.
				nodeEventHandlers.nodeFunctionality(text);
				// Add node to node ArrayList.
				model.getNode().add(text);
				// Add the node to the canvas
				canvas_P.getChildren().add(text);
			}
		});

		// Add a new rectangle to the canvas.
		rectangle_BTN.setOnAction(event -> {
			if (canvas_P.isVisible()) {
				Rectangle rectangle = new Rectangle(100, 100, Color.web("#B0C4DE"));
				rectangle.setStroke(Color.web("#4b79b4"));
				// Position nodes to the middle of the canvas.
				rectangle.setX(xCenterNode(rectangle));
				rectangle.setY(yCenterNode(rectangle));
				// Assign event handlers to node.
				nodeEventHandlers.nodeFunctionality(rectangle);
				// Add node to node ArrayList.
				model.getNode().add(rectangle);
				// Add the node to the canvas
				canvas_P.getChildren().add(rectangle);
			}
		});

		// Add a new circle to the canvas.
		circle_BTN.setOnAction(event -> {
			System.out.println(model.getIsPremium());
			if (canvas_P.isVisible()) {
				Circle circle = new Circle(50, Color.web("#DDA0DD"));
				circle.setStroke(Color.web("#bb44bb"));
				// Position nodes to the middle of the canvas.
				circle.setCenterX(xCenterNode(circle));
				circle.setCenterY(yCenterNode(circle));
				// Assign event handlers to node.
				nodeEventHandlers.nodeFunctionality(circle);
				// Add node to node ArrayList.
				model.getNode().add(circle);
				// Add the node to the canvas
				canvas_P.getChildren().add(circle);
			}
		});

		// Add a new image to the canvas.
		image_BTN.setOnAction(event -> {
			if (canvas_P.isVisible()) {
				ImageView image = null;
				try {
					Image img = new Image(new ByteArrayInputStream(Picture.convertPicture(boardStage)));
					image = new ImageView(img);
					// Position nodes to the middle of the canvas.
					image.setX(xCenterNode(image));
					image.setY(yCenterNode(image));
					// Assign event handlers to node.
					nodeEventHandlers.nodeFunctionality(image);
					// Add node to node ArrayList.
					model.getNode().add(image);
					// Add the node to the canvas
					canvas_P.getChildren().add(image);
				} catch (NullPointerException e) {
					message_LBL.setText("Error: Unable to convert selected image to byte array.");
					message_LBL.setTextFill(Color.RED);
				} catch (IOException e) {
					message_LBL.setText("Error: Please select an image before closing the dialog box.");
					message_LBL.setTextFill(Color.RED);
				}
			}
		});

		// Open canvas option sub menu.
		editCanvas_MI.setOnAction(event -> {
			try {
				model.setSelectedNode(canvas_P);
				optionPaneSelector(canvas_P);
			} catch (IOException e) {
				message_LBL.setText(e.getMessage());
				message_LBL.setTextFill(Color.RED);
			}
		});

		// Controls deleting a node on the canvas with delete keyboard button.
		boardStage.addEventHandler(KeyEvent.ANY, keyEvent -> {
			if (keyEvent.getCode() == KeyCode.DELETE) {
				// Remove the selected node from the canvas pane.
				canvas_P.getChildren().remove(model.getSelectedNode());
			}
		});
		
		// Controls deleting a node on the canvas.
		deleteElement_MI.setOnAction(event -> {
			if (model.getSelectedNode() != null) {
				canvas_P.getChildren().remove(model.getSelectedNode());
			}
		});

		// About me page
		about_MI.setOnAction(event -> {
			boardStage.close();

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/AboutPageView.fxml"));

				// Pass the current stage and model a new NewProfileController class
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new AboutPageController(boardStage);
				};
				loader.setControllerFactory(controllerFactory);
				AnchorPane pane = loader.load();

				// Return the controller associated with the pane object.
				AboutPageController aboutPageController = loader.getController();
				aboutPageController.showStage(pane);
				boardStage.close();
			} catch (IOException e) {
				// Display the exception
				message_LBL.setText(e.getMessage());
			}
		});

		// Clear canvas
		clearCanvas_MI.setOnAction(event -> {
			canvas_P.setVisible(false);
			slider_S.setVisible(false);
			zoom_LBL.setVisible(false);
			// Remove child nodes from canvas.
			canvas_P.getChildren().clear();
			// Allow a new canvas to be created and modified.
			newCanvas_MI.setDisable(false);
			editCanvas_MI.setDisable(true);
			
			// Set canvas back to white color.
			canvas_P.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			
			// Initialize empty pane for reseting options sub menu.
			Pane emptyPane = new Pane();
			// Reset the options sub menu.
			optionsPane_P.setCenter(emptyPane);
			// Reset positioning upon clear canvas.
			position_LBL.setVisible(false);
		});

		// Zoom slider functionality
		slider_S.valueProperty().addListener((o, ov, newValue) -> {
			setPaneZoom(newValue.intValue());
		});

		// Canvas Controls (Left side bar)
		canvas_BTN.setOnAction(event -> {
			newCanvas();
		});

		// 2nd Canvas controls (File menu button)
		newCanvas_MI.setOnAction(event -> {
			newCanvas();
		});

		// Logout button functionality.
		logout_BTN.setOnAction(event -> {
			boardStage.close();
			parentStage.show();
		});

		// Profile button functionality.
		profile_BTN.setOnAction(event -> {
			boardStage.close();

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/EditProfileView.fxml"));

				// Pass the current stage and model a new NewProfileController class
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new EditProfileController(boardStage);
				};
				loader.setControllerFactory(controllerFactory);
				AnchorPane pane = loader.load();

				// Return the controller associated with the pane object.
				EditProfileController editProfileController = loader.getController();
				editProfileController.showStage(pane);
				boardStage.close();
			} catch (IOException e) {
				// Display the exception
				message_LBL.setText(e.getMessage());
			}
		});
		
		// Premium template 1
		template1_BTN.setOnAction(event -> {
			// Remove existing child nodes from canvas.
			canvas_P.getChildren().clear();
			canvas_P.setPrefWidth(560);
			canvas_P.setPrefHeight(460);
			canvas_P.setVisible(true);
			
			Rectangle rect1 = new Rectangle(80, 50, 400, 80);
			rect1.setFill(Color.AQUA);
			Rectangle rect2 = new Rectangle(80, 150, 400, 80);
			rect2.setFill(Color.GREEN);
			Rectangle rect3 = new Rectangle(80, 250, 400, 80);
			rect3.setFill(Color.ORANGE);
			Rectangle rect4 = new Rectangle(80, 350, 400, 80);
			rect4.setFill(Color.YELLOW);
			
			// Assign event handlers to node.
			nodeEventHandlers.nodeFunctionality(rect1);
			nodeEventHandlers.nodeFunctionality(rect2);
			nodeEventHandlers.nodeFunctionality(rect3);
			nodeEventHandlers.nodeFunctionality(rect4);
			// Add node to node ArrayList.
			model.getNode().add(rect1);
			model.getNode().add(rect2);
			model.getNode().add(rect3);
			model.getNode().add(rect4);
			// Add the node to the canvas
			canvas_P.getChildren().add(rect1);
			canvas_P.getChildren().add(rect2);
			canvas_P.getChildren().add(rect3);
			canvas_P.getChildren().add(rect4);
			
		});
		
		// Premium template 2
		template2_BTN.setOnAction(event -> {
			// Remove existing child nodes from canvas.
			canvas_P.getChildren().clear();
			canvas_P.setPrefWidth(560);
			canvas_P.setPrefHeight(460);
			canvas_P.setVisible(true);
			
			Circle circle1 = new Circle(100, 50, 20, Color.PINK);
			Circle circle2 = new Circle(250, 150, 90, Color.HONEYDEW);
			Circle circle3 = new Circle(120, 280, 120, Color.PAPAYAWHIP);
			Circle circle4 = new Circle(400, 300, 140, Color.TOMATO);
			Circle circle5 = new Circle(300, 380, 60, Color.FUCHSIA);
			
			// Assign event handlers to node.
			nodeEventHandlers.nodeFunctionality(circle1);
			nodeEventHandlers.nodeFunctionality(circle2);
			nodeEventHandlers.nodeFunctionality(circle3);
			nodeEventHandlers.nodeFunctionality(circle4);
			nodeEventHandlers.nodeFunctionality(circle5);
			// Add node to node ArrayList.
			model.getNode().add(circle1);
			model.getNode().add(circle2);
			model.getNode().add(circle3);
			model.getNode().add(circle4);
			model.getNode().add(circle5);
			// Add the node to the canvas
			canvas_P.getChildren().add(circle1);
			canvas_P.getChildren().add(circle2);
			canvas_P.getChildren().add(circle3);
			canvas_P.getChildren().add(circle4);
			canvas_P.getChildren().add(circle5);
		});
		
		// Premium template 3
		template3_BTN.setOnAction(event -> {
			// Remove existing child nodes from canvas.
			canvas_P.getChildren().clear();
			canvas_P.setPrefWidth(560);
			canvas_P.setPrefHeight(460);
			canvas_P.setVisible(true);
			
			Rectangle rect1 = new Rectangle(80, 40, 400, 400);
			rect1.setFill(Color.LIGHTGRAY);
			Rectangle rect2 = new Rectangle(100, 120, 360, 50);
			rect2.setFill(Color.GRAY);
			Rectangle rect3 = new Rectangle(80, 400, 400, 30);
			rect3.setFill(Color.BEIGE);
			Label label1 = new Label("Edit This Title!");
			label1.setFont(Font.font("Times New Roman", FontWeight.BOLD, 35));
			label1.relocate(160,40);
			
			// Assign event handlers to node.
			nodeEventHandlers.nodeFunctionality(rect1);
			nodeEventHandlers.nodeFunctionality(rect2);
			nodeEventHandlers.nodeFunctionality(rect3);
			nodeEventHandlers.nodeFunctionality(label1);
			// Add node to node ArrayList.
			model.getNode().add(rect1);
			model.getNode().add(rect2);
			model.getNode().add(rect3);
			model.getNode().add(label1);
			// Add the node to the canvas
			canvas_P.getChildren().add(rect1);
			canvas_P.getChildren().add(rect2);
			canvas_P.getChildren().add(rect3);
			canvas_P.getChildren().add(label1);
		});
		
		// Save canvas as image to user's device.
		saveCanvas_MI.setOnAction(event -> {
			// Create a new snapshot of the canvas.
			WritableImage snapshot = canvas_P.snapshot(new SnapshotParameters(), null);
			saveImage(snapshot, boardStage, message_LBL);
		});
	}

	
	public static void saveImage(WritableImage snapshot, Stage primaryStage, Label message_LBL) {
		
		Image image = snapshot;
		//Obtain image dimensions
		int imgWidth = (int) image.getWidth();
		int imgHeight = (int) image.getHeight();
		int pixels[] = new int[imgWidth * imgHeight];
		
		// Convert the image's information into an array.
		image.getPixelReader().getPixels(0, 0, imgWidth, imgHeight,
				(WritablePixelFormat<IntBuffer>) image.getPixelReader().getPixelFormat(), pixels, 0, imgWidth);

		// Open file chooser menu and ask user to select file name and location.
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*.png", "*.jpg", "*.jpeg");
		fileChooser.getExtensionFilters().add(extFilter);
		File selectedDirectory = fileChooser.showSaveDialog(primaryStage);

		BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		
		// Convert WritableImage to BufferedImage WITHOUT SwingFXUtils.
		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				// Covert RGB values from image.
				var pixel = pixels[y * imgWidth + x];
				int r = (pixel & 0xFF0000) >> 16;
				int g = (pixel & 0xFF00) >> 8;
				int b = (pixel & 0xFF) >> 0;
				
				// Add values to bufferedImage.
				bufferedImage.getRaster().setPixel(x, y, new int[] { r, g, b });
			}
		}

		try {
			// Write bufferedImage to file
			ImageIO.write(bufferedImage, "png", selectedDirectory);
		} catch (IOException e) {
			// Display the exception
			message_LBL.setText(e.getMessage());
		}
	}
	
	
	/**
	 * Center nodes on the canvas - 'x' position Return the 'x' midpoint of a node with the midpoint of
	 * the canvas.
	 */
	private double xCenterNode(Node node) {
		double xMidNode = 0;
		try {
			double xCanvasMid = canvas_P.getWidth() / 2;
			xMidNode = xCanvasMid - (node.getBoundsInParent().getWidth() / 2);
			return xMidNode;
		} catch (NullPointerException e) {
			message_LBL.setText("Error: Please select an image before closing the dialog box.");
			message_LBL.setTextFill(Color.RED);
		}
		return xMidNode;
	}

	/**
	 * Center nodes on the canvas - 'y' position Return the 'y' midpoint of a node with the midpoint of
	 * the canvas.
	 */
	private double yCenterNode(Node node) {
		double yCanvasMid = canvas_P.getHeight() / 2;
		double yMidNode = yCanvasMid - (node.getBoundsInParent().getHeight() / 2);
		return yMidNode;
	}

	/**
	 * Controls canvas zoom.
	 */
	private void setPaneZoom(int percentage) {
		if (canvas_P != null) {
			canvas_P.setScaleX(percentage / 100.0);
			canvas_P.setScaleY(percentage / 100.0);
			zoom_LBL.setText("Zoom: " + percentage + "%");
		}
	}

	/**
	 * Opens new canvas stage.
	 */
	private void newCanvas() {

		if (!canvas_P.isVisible()) {
			boardStage.close();

			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/NewCanvasView.fxml"));

				// Pass the current stage and model a new NewProfileController class
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new NewCanvasController(boardStage);
				};
				loader.setControllerFactory(controllerFactory);
				AnchorPane pane = loader.load();

				// Return the controller associated with the pane object.
				NewCanvasController newCanvasController = loader.getController();
				newCanvasController.showStage(pane);
				boardStage.close();
			} catch (IOException e) {
				// Display the exception
				message_LBL.setText(e.getMessage());
			}
		}
	}

	public void optionPaneSelector(Node node) throws IOException {
		// If node is a shape, load the shape options sub menu.
		if (node instanceof Rectangle || node instanceof Circle) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/views/ShapeOptionsView.fxml"));
			Pane subMenu = loader.load();
			// Set the appropriate sub menu.
			optionsPane_P.setCenter(subMenu);

			// If node is text, load the text options sub menu.
		} else if (node instanceof Label) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/views/TextOptionsView.fxml"));
			Pane subMenu = loader.load();
			// Set the appropriate sub menu.
			optionsPane_P.setCenter(subMenu);

			// If node is an image, load the image options sub menu.
		} else if (node instanceof ImageView) {
			model.setCurrentStage(boardStage);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/views/ImageOptionsView.fxml"));
			Pane subMenu = loader.load();
			// Set the appropriate sub menu.
			optionsPane_P.setCenter(subMenu);

			// If node is the canvas (pane), load the canvas options sub menu.
		} else if (node instanceof Pane) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/resources/views/CanvasOptionsView.fxml"));
			Pane subMenu = loader.load();
			// Set the appropriate sub menu.
			optionsPane_P.setCenter(subMenu);
		}
	}

	
	/**
	 * Inner controller class specifically used for node event handling.
	 */
	private class NodeEventHandlers {

		// Connect inner class controller instance to BoardView FXML.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/BoardView.fxml"));
		NodeEventHandlers nodeEventHandlers = loader.getController();

		// Assign event handlers to nodes.
		public void nodeFunctionality(Node node) {
			node.setOnMousePressed(onMousePressedEH);
			node.setOnMouseDragged(onMouseDraggedEH);
			node.setOnMouseReleased(onMouseReleasedEH);
		}

		// Initialize original coordinates of nodes. Used for moving nodes.
		private double originX;
		private double originY;

		// Initialize empty pane for reseting options sub menu.
		Pane emptyPane = new Pane();

		/**
		 * Control clicks on node & bottom left position/size label.
		 */
		EventHandler<MouseEvent> onMousePressedEH = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Node node = (Node) e.getSource();

				// If user clicks on the canvas
				if (node instanceof Pane) {
					position_LBL.setVisible(false);
					unselectNodes();

					// Reset error message.
					message_LBL.setText("");

					// Reset the options sub menu
					optionsPane_P.setCenter(emptyPane);

					// If user clicks on text, image, rectangle or circle.
				} else {
					unselectNodes();
					// Select current node
					model.setSelectedNode(node);
					node.setOpacity(0.80);

					// Display the correct option sub-menu depending on the type of node.
					try {
						optionPaneSelector(node);
					} catch (IOException e1) {
						message_LBL.setText(e1.getMessage());
						message_LBL.setTextFill(Color.RED);
					}

					// Get position and size of node.
					originX = e.getX();
					originY = e.getY();
					double nodeWidth = node.getBoundsInParent().getWidth();
					double nodeHeight = node.getBoundsInParent().getHeight();
					double rotation = node.getRotate();

					// Set bottom left position and size label.
					position_LBL.setVisible(true);

					// If node is a circle, don't display the angle.
					if (node instanceof Circle) {
						position_LBL.setText(String.format("x: %.2f y: %.2f w: %.0f h: %.0f", originX, originY,
								nodeWidth, nodeHeight));

					} else {
						position_LBL.setText(String.format("x: %.2f y: %.2f w: %.0f h: %.0f angle: %.0f", originX,
								originY, nodeWidth, nodeHeight, rotation));
					}
				}
				// Stops propagation of event to other listeners.
				e.consume();
			}
		};

		// Remove selection visual effect from all nodes.
		public void unselectNodes() {
			for (Node node : model.getNode()) {
				if (node instanceof Shape || node instanceof Label || node instanceof ImageView) {
					// Remove node as currently selected node.
					model.setSelectedNode(null);
					node.setOpacity(1);
					node.setCursor(Cursor.DEFAULT);
				}
			}
		}

		
		/**
		 * Control the dragging of nodes to move them.
		 */
		EventHandler<MouseEvent> onMouseDraggedEH = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Node node = (Node) e.getSource();

				// Get node's position.
				double dx = e.getX() - originX;
				double dy = e.getY() - originY;

				// Move node.
				double x = node.getBoundsInParent().getMinX();
				double y = node.getBoundsInParent().getMinY();
				node.relocate(x + dx, y + dy);

				// Only allow canvas elements to change cursor.
				if (!(node instanceof Pane)) {
					node.setCursor(Cursor.CLOSED_HAND);
				}
				
				// Stops propagation of event to other listeners.
				e.consume();
			}
		};

		
		/**
		 * Control the release of clicks on nodes.
		 */
		EventHandler<MouseEvent> onMouseReleasedEH = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				// Set cursor back to default when user releases their click.
				for (Node node : model.getNode()) {
					node.setCursor(Cursor.DEFAULT);
				}
				// Stops the event from propagating to other listeners.
				e.consume();
			}
		};
	}

	
	/**
	 * Obtain the previous stage and model from other classes.
	 */
	public BoardController(Stage parentStage) {
		this.boardStage = new Stage();
		this.parentStage = parentStage;
	}

	
	/**
	 * Create a new board scene and set stage settings
	 */
	@Override
	public void showStage(Pane root) {
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/resources/stylesheets/Board.css").toExternalForm());
		boardStage.setScene(scene);
		boardStage.setResizable(false);
		boardStage.setTitle("SmartCanvas");
		boardStage.show();
	}

}
